package vn.edu.hust.itss.group15.domain;

import java.util.List;

public record OverseasOrder(
    String orderId,
    String siteCode,
    OrderStatus status,
    String acknowledgementToken,
    List<OrderLine> orderLines) {

  public OverseasOrder {
    if (orderId == null || orderId.isBlank()) {
      throw new IllegalArgumentException("orderId is required");
    }
    if (siteCode == null || siteCode.isBlank()) {
      throw new IllegalArgumentException("siteCode is required");
    }
    if (orderLines == null || orderLines.isEmpty()) {
      throw new IllegalArgumentException("orderLines are required");
    }
    orderLines = List.copyOf(orderLines);
  }

  public OverseasOrder withStatus(OrderStatus newStatus, String token) {
    return new OverseasOrder(orderId, siteCode, newStatus, token, orderLines);
  }
}
