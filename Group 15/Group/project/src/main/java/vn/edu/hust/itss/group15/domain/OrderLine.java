package vn.edu.hust.itss.group15.domain;

public record OrderLine(String merchandiseCode, int quantityOrdered, String unit, DeliveryMeans deliveryMeans) {
  public OrderLine {
    if (quantityOrdered <= 0) {
      throw new IllegalArgumentException("quantityOrdered must be positive");
    }
  }
}
