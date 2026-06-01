package vn.edu.hust.itss.group15.app.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.FormFieldFactory;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.InventoryRecord;

public class InventoryView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<InventoryRecord> table = new TableView<>();
  private final ComboBox<String> site = new ComboBox<>();
  private final ComboBox<String> merchandise;
  private final TextField quantity = FormFieldFactory.number("In stock");
  private final ComboBox<String> unit;

  public InventoryView(AppSession session) {
    this.session = session;
    merchandise = FormFieldFactory.combo(session.facade().store().merchandiseCatalog());
    unit = FormFieldFactory.combo(session.facade().store().units());
    getStyleClass().add("view");
    table.getColumns().addAll(java.util.List.of(
        UiSupport.column("Site", InventoryRecord::siteCode, 140),
        UiSupport.column("Merchandise", InventoryRecord::merchandiseCode, 140),
        UiSupport.column("Stock", record -> String.valueOf(record.inStockQuantity()), 110),
        UiSupport.column("Unit", InventoryRecord::unit, 100)
    ));
    table.getStyleClass().add("data-table");

    var record = UiSupport.primary("Record stock");
    record.setOnAction(event -> run());
    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Site", site);
    UiSupport.row(form, 1, "Merchandise", merchandise);
    UiSupport.row(form, 2, "Stock", quantity);
    UiSupport.row(form, 3, "Unit", unit);
    form.add(UiSupport.actions(record), 1, 4);

    setTop(UiSupport.panel(UiSupport.title("Inventory responses"), UiSupport.subtitle("Record Site stock replies, including zero stock."), form));
    setCenter(table);
    refresh();
  }

  @Override
  public void refresh() {
    site.getItems().setAll(session.facade().sites().listSites().stream().map(item -> item.siteCode()).toList());
    if (!site.getItems().isEmpty() && site.getValue() == null) {
      site.getSelectionModel().selectFirst();
    }
    table.setItems(FXCollections.observableArrayList(session.facade().inventory().list()));
  }

  private void run() {
    try {
      session.facade().inventory().recordStock(site.getValue(), merchandise.getValue(), UiSupport.intValue(quantity), unit.getValue());
      refresh();
      session.refresh();
      Toast.info("Inventory recorded");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }
}
