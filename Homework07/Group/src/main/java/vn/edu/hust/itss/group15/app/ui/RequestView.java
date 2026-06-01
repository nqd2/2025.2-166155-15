package vn.edu.hust.itss.group15.app.ui;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.ConfirmDialog;
import vn.edu.hust.itss.group15.app.ui.components.FormFieldFactory;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;

public class RequestView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<ImportRequest> table = new TableView<>();
  private final ComboBox<String> merchandise;
  private final TextField quantity = FormFieldFactory.number("Quantity");
  private final ComboBox<String> unit;
  private final DatePicker desiredDate = FormFieldFactory.futureDate();

  public RequestView(AppSession session) {
    this.session = session;
    merchandise = FormFieldFactory.combo(session.facade().store().merchandiseCatalog());
    unit = FormFieldFactory.combo(session.facade().store().units());
    getStyleClass().add("view");
    table.getColumns().addAll(
        UiSupport.column("Request", ImportRequest::requestId, 120),
        UiSupport.column("Status", request -> request.status().name(), 120),
        UiSupport.column("Items", request -> request.items().toString(), 520),
        UiSupport.column("Created", request -> request.createdAt().toString(), 190));
    table.getStyleClass().add("data-table");

    var create = UiSupport.primary("Create request");
    create.setOnAction(event -> run(() -> session.facade().importRequests().create(List.of(item()))));
    var update = UiSupport.secondary("Update selected");
    update.setOnAction(event -> run(() -> session.facade().importRequests().update(selected().requestId(), List.of(item()))));
    var cancel = UiSupport.danger("Cancel selected");
    cancel.setOnAction(event -> {
      ImportRequest request = selected();
      if (ConfirmDialog.confirm("Cancel request", "Cancel " + request.requestId() + "?")) {
        run(() -> session.facade().importRequests().cancel(request.requestId()));
      }
    });

    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Merchandise", merchandise);
    UiSupport.row(form, 1, "Quantity", quantity);
    UiSupport.row(form, 2, "Unit", unit);
    UiSupport.row(form, 3, "Desired date", desiredDate);
    form.add(UiSupport.actions(create, update, cancel), 1, 4);

    setTop(UiSupport.panel(UiSupport.title("Import requests"), UiSupport.subtitle("Create, update, and cancel import demand from Sales."), form));
    setCenter(table);
    refresh();
  }

  @Override
  public void refresh() {
    table.setItems(FXCollections.observableArrayList(session.facade().importRequests().list()));
  }

  private ImportRequestItem item() {
    LocalDate date = desiredDate.getValue() == null ? LocalDate.now() : desiredDate.getValue();
    return new ImportRequestItem(merchandise.getValue(), UiSupport.intValue(quantity), unit.getValue(), date);
  }

  private ImportRequest selected() {
    ImportRequest request = table.getSelectionModel().getSelectedItem();
    if (request == null) {
      throw new IllegalArgumentException("select a request first");
    }
    return request;
  }

  private void run(Runnable action) {
    try {
      action.run();
      refresh();
      session.refresh();
      Toast.info("Request data saved");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }
}
