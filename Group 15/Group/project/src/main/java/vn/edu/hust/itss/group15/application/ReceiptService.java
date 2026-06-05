package vn.edu.hust.itss.group15.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import vn.edu.hust.itss.group15.domain.GoodsReceipt;
import vn.edu.hust.itss.group15.domain.OrderLine;
import vn.edu.hust.itss.group15.domain.OrderStatus;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.ReceiptLine;
import vn.edu.hust.itss.group15.application.port.Store;

public class ReceiptService {
  private final Store store;
  public ReceiptService(Store store) {
    this.store = store;
  }

  public GoodsReceipt receive(String orderReference, Map<String, Integer> receivedQuantities, Map<String, String> notes) {
    OverseasOrder order = store.findOrder(orderReference).orElseThrow(() -> new BusinessException("order not found: " + orderReference));
    if (order.status() == OrderStatus.REJECTED) {
      throw new BusinessException("rejected order cannot be received");
    }

    List<ReceiptLine> lines = new ArrayList<>();
    for (OrderLine orderLine : order.orderLines()) {
      int received = receivedQuantities.getOrDefault(orderLine.merchandiseCode(), 0);
      int discrepancy = received - orderLine.quantityOrdered();
      String type = discrepancy == 0 ? "MATCHED" : discrepancy < 0 ? "DEFICIT" : "SURPLUS";
      String note = notes.getOrDefault(orderLine.merchandiseCode(), "");
      if (discrepancy != 0 && note.isBlank()) {
        throw new BusinessException("discrepancyNote is required for " + orderLine.merchandiseCode());
      }
      lines.add(new ReceiptLine(orderLine.merchandiseCode(), orderLine.quantityOrdered(), received, discrepancy, type, note));
    }

    GoodsReceipt receipt = new GoodsReceipt(store.nextId("goods_receipts", "GR"), orderReference, LocalDateTime.now(), lines);
    for (ReceiptLine line : lines) {
      store.addWarehouseStock(line.merchandiseCode(), line.receivedQuantity());
    }
    store.saveReceipt(receipt);
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "RECEIVE_GOODS", LocalDateTime.now(), receipt.receiptId()));
    return receipt;
  }

  public List<GoodsReceipt> list() {
    return store.receipts();
  }
}
