package vn.edu.hust.itss.group15.app.ui.components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class KpiCard extends VBox {
  private final Label value = new Label("0");
  private final Label caption = new Label();

  public KpiCard(String title) {
    getStyleClass().add("kpi-card");
    setPadding(new Insets(16));
    setSpacing(6);
    Label titleLabel = new Label(title);
    titleLabel.getStyleClass().add("kpi-title");
    value.getStyleClass().add("kpi-value");
    caption.getStyleClass().add("kpi-caption");
    getChildren().addAll(titleLabel, value, caption);
  }

  public void setValue(String value, String caption) {
    this.value.setText(value);
    this.caption.setText(caption);
  }
}
