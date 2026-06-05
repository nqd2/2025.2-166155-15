package vn.edu.hust.itss.group15.app.ui.components;

import java.time.LocalDate;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public final class FormFieldFactory {
  private FormFieldFactory() {}

  public static TextField text(String prompt) {
    TextField field = new TextField();
    field.setPromptText(prompt);
    field.setMaxWidth(Double.MAX_VALUE);
    return field;
  }

  public static TextField number(String prompt) {
    TextField field = text(prompt);
    field.textProperty().addListener((observable, oldValue, newValue) -> {
      if (!newValue.matches("\\d*")) {
        field.setText(oldValue);
      }
    });
    return field;
  }

  public static DatePicker futureDate() {
    DatePicker picker = new DatePicker(LocalDate.now().plusDays(30));
    picker.setMaxWidth(Double.MAX_VALUE);
    return picker;
  }

  public static ComboBox<String> combo(Iterable<String> values) {
    ComboBox<String> comboBox = new ComboBox<>();
    values.forEach(comboBox.getItems()::add);
    comboBox.setMaxWidth(Double.MAX_VALUE);
    if (!comboBox.getItems().isEmpty()) {
      comboBox.getSelectionModel().selectFirst();
    }
    return comboBox;
  }
}
