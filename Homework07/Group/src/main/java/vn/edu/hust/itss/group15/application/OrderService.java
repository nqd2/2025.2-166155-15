package vn.edu.hust.itss.group15.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import vn.edu.hust.itss.group15.domain.AllocationLine;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.OrderLine;
import vn.edu.hust.itss.group15.domain.OrderStatus;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.application.port.Store;

public class OrderService {
  private final Store store;
  public OrderService(Store store) {
    this.store = store;
  }

  public List<OverseasOrder> generateOrders(String planId) {
    AllocationPlan plan = store.findAllocationPlan(planId).orElseThrow(() -> new BusinessException("allocation plan not found: " + planId));
    Map<String, List<AllocationLine>> bySite = plan.lines().stream().collect(Collectors.groupingBy(AllocationLine::siteCode));
    List<OverseasOrder> orders = new ArrayList<>();
    for (Map.Entry<String, List<AllocationLine>> entry : bySite.entrySet()) {
      List<OrderLine> lines = entry.getValue().stream()
          .map(line -> new OrderLine(line.merchandiseCode(), line.quantityOrdered(), line.unit(), line.deliveryMeans()))
          .toList();
      OverseasOrder order = new OverseasOrder(store.nextId("overseas_orders", "PO"), entry.getKey(), OrderStatus.TRANSMITTED, "", lines);
      store.saveOrder(order);
      store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "GENERATE_ORDER", LocalDateTime.now(), order.orderId()));
      orders.add(order);
    }
    return orders;
  }

  public OverseasOrder acknowledge(String orderId) {
    return updateStatus(orderId, OrderStatus.ACKNOWLEDGED, "ACK-" + orderId);
  }

  public OverseasOrder reject(String orderId) {
    return updateStatus(orderId, OrderStatus.REJECTED, "REJ-" + orderId);
  }

  private OverseasOrder updateStatus(String orderId, OrderStatus status, String token) {
    OverseasOrder order = store.findOrder(orderId).orElseThrow(() -> new BusinessException("order not found: " + orderId));
    OverseasOrder updated = order.withStatus(status, token);
    store.saveOrder(updated);
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "UPDATE_ORDER_STATUS", LocalDateTime.now(), orderId + ":" + status));
    return updated;
  }

  public List<OverseasOrder> list() {
    return store.orders();
  }
}
