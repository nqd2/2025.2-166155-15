package vn.edu.hust.itss.group15.app.ui;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

final class UiSupport {
  private UiSupport() {}

  static Label title(String text) {
    Label label = new Label(text);
    label.getStyleClass().add("view-title");
    return label;
  }

  static Label subtitle(String text) {
    Label label = new Label(text);
    label.getStyleClass().add("view-subtitle");
    return label;
  }

  static Button primary(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("primary-button");
    return button;
  }

  static Button secondary(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("secondary-button");
    return button;
  }

  static Button danger(String text) {
    Button button = new Button(text);
    button.getStyleClass().add("danger-button");
    return button;
  }

  static GridPane form() {
    GridPane grid = new GridPane();
    grid.setHgap(12);
    grid.setVgap(10);
    grid.setPadding(new Insets(12));
    grid.getStyleClass().add("form-panel");
    return grid;
  }

  static void row(GridPane grid, int row, String label, javafx.scene.Node node) {
    Label labelNode = new Label(label);
    labelNode.getStyleClass().add("field-label");
    grid.add(labelNode, 0, row);
    grid.add(node, 1, row);
    GridPane.setHgrow(node, Priority.ALWAYS);
  }

  static HBox actions(Button... buttons) {
    HBox hBox = new HBox(8, buttons);
    hBox.getStyleClass().add("action-row");
    return hBox;
  }

  static VBox panel(javafx.scene.Node... nodes) {
    VBox box = new VBox(12, nodes);
    box.setPadding(new Insets(18));
    box.getStyleClass().add("content-panel");
    return box;
  }

  static <T> TableColumn<T, String> column(String title, java.util.function.Function<T, String> mapper, int width) {
    TableColumn<T, String> column = new TableColumn<>(title);
    column.setCellValueFactory(data -> new ReadOnlyStringWrapper(mapper.apply(data.getValue())));
    column.setPrefWidth(width);
    return column;
  }

  static int intValue(TextField field) {
    if (field.getText() == null || field.getText().isBlank()) {
      return 0;
    }
    return Integer.parseInt(field.getText().trim());
  }

  static <T> void showDetailsDialog(String title, String header, java.util.List<T> items, java.util.List<TableColumn<T, String>> columns) {
    javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
    dialog.setTitle(title);
    dialog.setHeaderText(header);

    javafx.scene.control.TableView<T> detailsTable = new javafx.scene.control.TableView<>();
    detailsTable.setItems(javafx.collections.FXCollections.observableArrayList(items));
    detailsTable.getColumns().addAll(columns);
    detailsTable.setPrefWidth(640);
    detailsTable.setPrefHeight(320);
    detailsTable.getStyleClass().add("data-table");

    dialog.getDialogPane().setContent(detailsTable);
    dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.CLOSE);

    dialog.getDialogPane().getStylesheets().addAll(
        UiSupport.class.getResource("/styles/app.css").toExternalForm()
    );
    dialog.showAndWait();
  }
}
