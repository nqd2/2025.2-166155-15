package vn.edu.hust.itss.group15.domain;

import java.time.LocalDate;
import java.util.Objects;

public record ImportRequestItem(
    String merchandiseCode,
    int quantityOrdered,
    String unit,
    LocalDate desiredDeliveryDate) {

  public ImportRequestItem {
    merchandiseCode = requireText(merchandiseCode, "merchandiseCode");
    unit = requireText(unit, "unit");
    if (quantityOrdered <= 0) {
      throw new IllegalArgumentException("quantityOrdered must be greater than 0");
    }
    Objects.requireNonNull(desiredDeliveryDate, "desiredDeliveryDate is required");
  }

  private static String requireText(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(field + " is required");
    }
    return value.trim();
  }
}
