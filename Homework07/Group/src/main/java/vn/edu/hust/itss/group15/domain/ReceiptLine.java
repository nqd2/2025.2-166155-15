package vn.edu.hust.itss.group15.domain;

public record ReceiptLine(
    String merchandiseCode,
    int orderedQuantity,
    int receivedQuantity,
    int discrepancyQuantity,
    String discrepancyType,
    String discrepancyNote) {

  public ReceiptLine {
    if (orderedQuantity <= 0) {
      throw new IllegalArgumentException("orderedQuantity must be positive");
    }
    if (receivedQuantity < 0) {
      throw new IllegalArgumentException("receivedQuantity must be non-negative");
    }
  }

  public boolean hasDiscrepancy() {
    return discrepancyQuantity != 0;
  }
}
