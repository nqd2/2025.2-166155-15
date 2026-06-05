package vn.edu.hust.itss.group15.domain;

public record InventoryRecord(String siteCode, String merchandiseCode, int inStockQuantity, String unit) {
  public InventoryRecord {
    if (siteCode == null || siteCode.isBlank()) {
      throw new IllegalArgumentException("siteCode is required");
    }
    if (merchandiseCode == null || merchandiseCode.isBlank()) {
      throw new IllegalArgumentException("merchandiseCode is required");
    }
    if (unit == null || unit.isBlank()) {
      throw new IllegalArgumentException("unit is required");
    }
    if (inStockQuantity < 0) {
      throw new IllegalArgumentException("inStockQuantity must be non-negative");
    }
  }
}
