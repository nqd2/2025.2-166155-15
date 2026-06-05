package vn.edu.hust.itss.group15.domain;

public record WarehouseStock(String merchandiseCode, int quantityInStock) {
  public WarehouseStock {
    if (quantityInStock < 0) {
      throw new IllegalArgumentException("quantityInStock must be non-negative");
    }
  }

  public WarehouseStock add(int quantity) {
    return new WarehouseStock(merchandiseCode, quantityInStock + quantity);
  }
}
