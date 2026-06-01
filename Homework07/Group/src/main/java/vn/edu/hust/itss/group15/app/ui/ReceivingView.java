package vn.edu.hust.itss.group15.app.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.FormFieldFactory;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.GoodsReceipt;
import vn.edu.hust.itss.group15.domain.OrderLine;
import vn.edu.hust.itss.group15.domain.OverseasOrder;

public class ReceivingView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<GoodsReceipt> receiptTable = new TableView<>();
  private final ComboBox<String> order = new ComboBox<>();
  private final TextField merchandise = FormFieldFactory.text("MH-001");
  private final TextField receivedQty = FormFieldFactory.number("Received quantity");
  private final TextArea note = new TextArea();

  public ReceivingView(AppSession session) {
    this.session = session;
    getStyleClass().add("view");
    note.setPrefRowCount(3);
    receiptTable.getColumns().addAll(java.util.List.of(
        UiSupport.column("Receipt", GoodsReceipt::receiptId, 120),
        UiSupport.column("Order", GoodsReceipt::orderReference, 120),
        UiSupport.column("Discrepancy", receipt -> receipt.hasDiscrepancy() ? "YES" : "NO", 120),
        UiSupport.column("Lines", receipt -> receipt.lines().toString(), 620)
    ));
    receiptTable.getStyleClass().add("data-table");

    order.setOnAction(event -> fillFirstLine());
    var receive = UiSupport.primary("Save receipt");
    receive.setOnAction(event -> run());
    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Order", order);
    UiSupport.row(form, 1, "Merchandise", merchandise);
    UiSupport.row(form, 2, "Received quantity", receivedQty);
    UiSupport.row(form, 3, "Discrepancy note", note);
    form.add(UiSupport.actions(receive), 1, 4);
    setTop(UiSupport.panel(UiSupport.title("Receiving"), UiSupport.subtitle("Compare ordered and received goods. Notes are required for mismatches."), form));
    setCenter(receiptTable);
    refresh();
  }

  @Override
  public void refresh() {
    order.getItems().setAll(session.facade().orders().list().stream().map(item -> item.orderId()).toList());
    if (!order.getItems().isEmpty() && order.getValue() == null) {
      order.getSelectionModel().selectFirst();
    }
    fillFirstLine();
    receiptTable.setItems(FXCollections.observableArrayList(session.facade().receipts().list()));
  }

  private void fillFirstLine() {
    if (order.getValue() == null) {
      return;
    }
    session.facade().orders().list().stream()
        .filter(item -> item.orderId().equals(order.getValue()))
        .findFirst()
        .flatMap(item -> item.orderLines().stream().findFirst())
        .ifPresent(line -> {
          merchandise.setText(line.merchandiseCode());
          receivedQty.setText(String.valueOf(line.quantityOrdered()));
        });
  }

  private void run() {
    try {
      OverseasOrder selected = session.facade().orders().list().stream()
          .filter(item -> item.orderId().equals(order.getValue()))
          .findFirst()
          .orElseThrow(() -> new IllegalArgumentException("select an order first"));
      Map<String, Integer> received = new LinkedHashMap<>();
      Map<String, String> notes = new LinkedHashMap<>();
      for (OrderLine line : selected.orderLines()) {
        int qty = line.merchandiseCode().equals(merchandise.getText()) ? UiSupport.intValue(receivedQty) : line.quantityOrdered();
        received.put(line.merchandiseCode(), qty);
        if (!note.getText().isBlank()) {
          notes.put(line.merchandiseCode(), note.getText());
        }
      }
      session.facade().receipts().receive(selected.orderId(), received, notes);
      refresh();
      session.refresh();
      Toast.info("Receipt saved");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }
}
