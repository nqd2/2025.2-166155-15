package vn.edu.hust.itss.group15.domain;

import java.util.Set;

public record ImportSite(
    String siteCode,
    String siteName,
    int deliveryDaysByShip,
    int deliveryDaysByAir,
    Set<String> merchandiseCatalog) {

  public ImportSite {
    siteCode = requireText(siteCode, "siteCode");
    siteName = requireText(siteName, "siteName");
    if (deliveryDaysByShip <= 0 || deliveryDaysByAir <= 0) {
      throw new IllegalArgumentException("delivery days must be positive");
    }
    if (merchandiseCatalog == null || merchandiseCatalog.isEmpty()) {
      throw new IllegalArgumentException("merchandiseCatalog is required");
    }
    merchandiseCatalog = Set.copyOf(merchandiseCatalog);
  }

  public boolean trades(String merchandiseCode) {
    return merchandiseCatalog.contains(merchandiseCode);
  }

  private static String requireText(String value, String field) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(field + " is required");
    }
    return value.trim();
  }
}
