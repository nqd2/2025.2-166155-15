package vn.edu.hust.itss.group15.app.ui.components;

import javafx.scene.control.Label;

public class StatusBadge extends Label {
  public StatusBadge(String status) {
    super(status);
    getStyleClass().addAll("status-badge", "status-" + status.toLowerCase().replace('_', '-'));
  }
}
