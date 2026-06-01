package vn.edu.hust.itss.group15.app.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.AllocationPlan;

public class AllocationView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<AllocationPlan> table = new TableView<>();
  private final ComboBox<String> request = new ComboBox<>();
  private final TextArea preview = new TextArea();

  public AllocationView(AppSession session) {
    this.session = session;
    getStyleClass().add("view");
    preview.setEditable(false);
    preview.setPrefRowCount(5);
    table.getColumns().addAll(
        UiSupport.column("Plan", AllocationPlan::planId, 120),
        UiSupport.column("Request", AllocationPlan::requestId, 120),
        UiSupport.column("Lines", plan -> plan.lines().toString(), 680));
    table.getStyleClass().add("data-table");

    var previewButton = UiSupport.secondary("Preview");
    previewButton.setOnAction(event -> runPreview());
    var save = UiSupport.primary("Commit allocation");
    save.setOnAction(event -> runSave());
    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Request", request);
    UiSupport.row(form, 1, "Preview", preview);
    form.add(UiSupport.actions(previewButton, save), 1, 2);
    setTop(UiSupport.panel(UiSupport.title("Allocation planning"), UiSupport.subtitle("Prefer ship, larger stock, and fewer Sites where possible."), form));
    setCenter(table);
    refresh();
  }

  @Override
  public void refresh() {
    request.getItems().setAll(session.facade().importRequests().list().stream().map(item -> item.requestId()).toList());
    if (!request.getItems().isEmpty() && request.getValue() == null) {
      request.getSelectionModel().selectFirst();
    }
    table.setItems(FXCollections.observableArrayList(session.facade().allocation().list()));
  }

  private void runPreview() {
    try {
      AllocationPlan plan = session.facade().allocation().preview(request.getValue());
      preview.setText(plan.lines().toString());
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }

  private void runSave() {
    try {
      AllocationPlan plan = session.facade().allocation().plan(request.getValue());
      preview.setText(plan.lines().toString());
      refresh();
      session.refresh();
      Toast.info("Allocation committed");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }
}
