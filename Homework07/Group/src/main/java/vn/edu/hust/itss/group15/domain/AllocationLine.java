package vn.edu.hust.itss.group15.domain;

public record AllocationLine(
    String requestId,
    String merchandiseCode,
    String siteCode,
    int quantityOrdered,
    String unit,
    DeliveryMeans deliveryMeans) {

  public AllocationLine {
    if (quantityOrdered <= 0) {
      throw new IllegalArgumentException("quantityOrdered must be positive");
    }
  }
}
