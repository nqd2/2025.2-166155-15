package vn.edu.hust.itss.group15.app.ui.components;

import javafx.scene.control.Alert;

public final class Toast {
  private Toast() {}

  public static void info(String message) {
    show(Alert.AlertType.INFORMATION, "Done", message);
  }

  public static void error(String message) {
    show(Alert.AlertType.ERROR, "Error", message);
  }

  private static void show(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(title);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
