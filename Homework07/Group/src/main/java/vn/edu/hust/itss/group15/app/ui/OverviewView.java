package vn.edu.hust.itss.group15.app.ui;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.KpiCard;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.OrderStatus;
import vn.edu.hust.itss.group15.domain.RequestStatus;

public class OverviewView extends VBox implements DashboardView.Refreshable {
  private final AppSession session;
  private final KpiCard requests = new KpiCard("Active requests");
  private final KpiCard stock = new KpiCard("Available stock");
  private final KpiCard plans = new KpiCard("Allocation plans");
  private final KpiCard orders = new KpiCard("Transmitted orders");
  private final KpiCard discrepancies = new KpiCard("Receipt discrepancies");
  private final TableView<OperationLog> logTable = new TableView<>();

  public OverviewView(AppSession session) {
    this.session = session;
    setSpacing(16);
    getStyleClass().add("view");

    GridPane grid = new GridPane();
    grid.setHgap(12);
    grid.setVgap(12);
    grid.add(requests, 0, 0);
    grid.add(stock, 1, 0);
    grid.add(plans, 2, 0);
    grid.add(orders, 3, 0);
    grid.add(discrepancies, 4, 0);
    GridPane.setHgrow(requests, Priority.ALWAYS);
    GridPane.setHgrow(stock, Priority.ALWAYS);
    GridPane.setHgrow(plans, Priority.ALWAYS);
    GridPane.setHgrow(orders, Priority.ALWAYS);
    GridPane.setHgrow(discrepancies, Priority.ALWAYS);

    logTable.getColumns().addAll(
        UiSupport.column("Time", log -> log.timestamp().toString(), 180),
        UiSupport.column("Action", OperationLog::actionType, 190),
        UiSupport.column("Details", OperationLog::details, 360));
    logTable.getStyleClass().add("data-table");

    getChildren().addAll(UiSupport.title("Operations dashboard"), UiSupport.subtitle("Live workflow state from Supabase REST."), grid,
        UiSupport.panel(UiSupport.subtitle("Recent activity"), logTable));
    refresh();
  }

  @Override
  public void refresh() {
    var facade = session.facade();
    long active = facade.importRequests().list().stream().filter(request -> request.status() != RequestStatus.CANCELLED).count();
    int stockQuantity = facade.store().warehouseStock().stream().mapToInt(item -> item.quantityInStock()).sum();
    long transmitted = facade.orders().list().stream().filter(order -> order.status() == OrderStatus.TRANSMITTED).count();
    long discrepancyCount = facade.receipts().list().stream().filter(receipt -> receipt.hasDiscrepancy()).count();
    requests.setValue(String.valueOf(active), "Submitted, updated, allocated");
    stock.setValue(String.valueOf(stockQuantity), "Units received into warehouse");
    plans.setValue(String.valueOf(facade.allocation().list().size()), "Saved allocation plans");
    orders.setValue(String.valueOf(transmitted), "Waiting for Site response");
    discrepancies.setValue(String.valueOf(discrepancyCount), "Receipts with mismatch");
    List<OperationLog> logs = facade.admin().logs();
    logTable.setItems(FXCollections.observableArrayList(logs.stream().limit(30).toList()));
  }
}
