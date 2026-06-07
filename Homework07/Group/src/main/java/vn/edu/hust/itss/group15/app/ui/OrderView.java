package vn.edu.hust.itss.group15.app.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.OverseasOrder;

public class OrderView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<OverseasOrder> table = new TableView<>();
  private final ComboBox<String> plan = new ComboBox<>();

  public OrderView(AppSession session) {
    this.session = session;
    getStyleClass().add("view");
    table.getColumns().addAll(java.util.List.of(
        UiSupport.column("Order", OverseasOrder::orderId, 120),
        UiSupport.column("Site", OverseasOrder::siteCode, 140),
        UiSupport.column("Status", order -> order.status().name(), 130),
        UiSupport.column("Ack", OverseasOrder::acknowledgementToken, 160),
        UiSupport.column("Lines", order -> order.orderLines().size() + " line(s) (Double-click to view)", 560)
    ));
    table.getStyleClass().add("data-table");
    table.setRowFactory(tv -> {
      javafx.scene.control.TableRow<OverseasOrder> row = new javafx.scene.control.TableRow<>();
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
          showOrderLinesPopup(row.getItem());
        }
      });
      return row;
    });

    var generate = UiSupport.primary("Generate orders");
    generate.setOnAction(event -> run(() -> session.facade().orders().generateOrders(plan.getValue()), "Orders generated"));
    var acknowledge = UiSupport.secondary("Acknowledge selected");
    acknowledge.setOnAction(event -> run(() -> session.facade().orders().acknowledge(selected().orderId()), "Order acknowledged"));
    var reject = UiSupport.danger("Reject selected");
    reject.setOnAction(event -> run(() -> session.facade().orders().reject(selected().orderId()), "Order rejected"));
    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Allocation plan", plan);
    form.add(UiSupport.actions(generate, acknowledge, reject), 1, 1);
    setTop(UiSupport.panel(UiSupport.title("Overseas orders"), UiSupport.subtitle("Group allocation lines by Site and track Site responses."), form));
    setCenter(table);
    refresh();
  }

  @Override
  public void refresh() {
    plan.getItems().setAll(session.facade().allocation().list().stream().map(item -> item.planId()).toList());
    if (!plan.getItems().isEmpty() && plan.getValue() == null) {
      plan.getSelectionModel().selectFirst();
    }
    table.setItems(FXCollections.observableArrayList(session.facade().orders().list()));
  }

  private OverseasOrder selected() {
    OverseasOrder order = table.getSelectionModel().getSelectedItem();
    if (order == null) {
      throw new IllegalArgumentException("select an order first");
    }
    return order;
  }

  private void showOrderLinesPopup(OverseasOrder order) {
    UiSupport.showDetailsDialog(
        "Order Lines - " + order.orderId(),
        "Lines for Order " + order.orderId(),
        order.orderLines(),
        java.util.List.of(
            UiSupport.column("Merchandise", vn.edu.hust.itss.group15.domain.OrderLine::merchandiseCode, 150),
            UiSupport.column("Quantity", line -> String.valueOf(line.quantityOrdered()), 110),
            UiSupport.column("Unit", vn.edu.hust.itss.group15.domain.OrderLine::unit, 100),
            UiSupport.column("Means", line -> line.deliveryMeans().name(), 120)
        )
    );
  }

  private void run(Runnable action, String message) {
    try {
      action.run();
      refresh();
      session.refresh();
      Toast.info(message);
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }
}
