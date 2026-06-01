package vn.edu.hust.itss.group15.domain;

public enum DeliveryMeans {
  SHIP("ship delivery"),
  AIR("air delivery");

  private final String label;

  DeliveryMeans(String label) {
    this.label = label;
  }

  public String label() {
    return label;
  }

  public static DeliveryMeans fromLabel(String value) {
    for (DeliveryMeans means : values()) {
      if (means.name().equalsIgnoreCase(value) || means.label.equalsIgnoreCase(value)) {
        return means;
      }
    }
    throw new IllegalArgumentException("deliveryMeans must be ship delivery or air delivery");
  }
}
