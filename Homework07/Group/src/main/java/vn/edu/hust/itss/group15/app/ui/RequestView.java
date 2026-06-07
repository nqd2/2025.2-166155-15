package vn.edu.hust.itss.group15.app.ui;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.ConfirmDialog;
import vn.edu.hust.itss.group15.app.ui.components.FormFieldFactory;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;

public class RequestView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<ImportRequest> table = new TableView<>();

  private static record RowControls(
      javafx.scene.control.CheckBox check,
      javafx.scene.control.TextField qtyField,
      javafx.scene.control.ComboBox<String> unitCombo,
      javafx.scene.control.DatePicker datePicker,
      String merchandiseCode) {}

  public RequestView(AppSession session) {
    this.session = session;
    getStyleClass().add("view");
    table.getColumns().addAll(java.util.List.of(
        UiSupport.column("Request", ImportRequest::requestId, 120),
        UiSupport.column("Status", request -> request.status().name(), 120),
        UiSupport.column("Items", request -> request.items().size() + " item(s) (Double-click to view)", 520),
        UiSupport.column("Created", request -> request.createdAt().toString(), 190)
    ));
    table.getStyleClass().add("data-table");
    table.setRowFactory(tv -> {
      javafx.scene.control.TableRow<ImportRequest> row = new javafx.scene.control.TableRow<>();
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
          showItemsPopup(row.getItem());
        }
      });
      return row;
    });

    var create = UiSupport.primary("Create request");
    create.setOnAction(event -> runCreate());
    var update = UiSupport.secondary("Update selected");
    update.setOnAction(event -> runUpdate());
    var cancel = UiSupport.danger("Cancel selected");
    cancel.setOnAction(event -> {
      try {
        ImportRequest request = selected();
        if (ConfirmDialog.confirm("Cancel request", "Cancel " + request.requestId() + "?")) {
          run(() -> session.facade().importRequests().cancel(request.requestId()));
        }
      } catch (RuntimeException exception) {
        Toast.error(exception.getMessage());
      }
    });

    setTop(UiSupport.panel(
        UiSupport.title("Import requests"),
        UiSupport.subtitle("Create, update, and cancel import demand from Sales."),
        UiSupport.actions(create, update, cancel)
    ));
    setCenter(table);
    refresh();
  }

  @Override
  public void refresh() {
    table.setItems(FXCollections.observableArrayList(session.facade().importRequests().list()));
  }

  private void runCreate() {
    List<ImportRequestItem> items = showRequestFormDialog(
        "Create Import Request",
        "Select merchandise, quantities, units, and delivery dates for the new request.",
        null
    );
    if (items != null) {
      run(() -> session.facade().importRequests().create(items));
    }
  }

  private void runUpdate() {
    try {
      ImportRequest request = selected();
      List<ImportRequestItem> items = showRequestFormDialog(
          "Update Import Request " + request.requestId(),
          "Modify items for request " + request.requestId() + ".",
          request.items()
      );
      if (items != null) {
        run(() -> session.facade().importRequests().update(request.requestId(), items));
      }
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }

  private List<ImportRequestItem> showRequestFormDialog(String title, String header, List<ImportRequestItem> initialItems) {
    javafx.scene.control.Dialog<List<ImportRequestItem>> dialog = new javafx.scene.control.Dialog<>();
    dialog.setTitle(title);
    dialog.setHeaderText(header);

    dialog.getDialogPane().getButtonTypes().addAll(
        javafx.scene.control.ButtonType.OK,
        javafx.scene.control.ButtonType.CANCEL
    );

    dialog.getDialogPane().getStylesheets().addAll(
        UiSupport.class.getResource("/styles/app.css").toExternalForm()
    );

    VBox rowsContainer = new VBox(8);
    rowsContainer.setPadding(new Insets(10));
    
    List<RowControls> controlsList = new java.util.ArrayList<>();
    java.util.Collection<String> merchandiseCatalog = session.facade().store().merchandiseCatalog();
    java.util.Collection<String> units = session.facade().store().units();

    for (String mCode : merchandiseCatalog) {
      javafx.scene.control.CheckBox check = new javafx.scene.control.CheckBox(mCode);
      check.setPrefWidth(120);

      javafx.scene.control.TextField qtyField = FormFieldFactory.number("Qty");
      qtyField.setPrefWidth(100);
      qtyField.setDisable(true);

      javafx.scene.control.ComboBox<String> unitCombo = FormFieldFactory.combo(units);
      unitCombo.setPrefWidth(120);
      unitCombo.setDisable(true);
      if (!units.isEmpty()) {
        unitCombo.getSelectionModel().selectFirst();
      }

      javafx.scene.control.DatePicker datePicker = FormFieldFactory.futureDate();
      datePicker.setPrefWidth(150);
      datePicker.setDisable(true);
      datePicker.setValue(LocalDate.now().plusDays(7)); // Default to some near future date

      qtyField.disableProperty().bind(check.selectedProperty().not());
      unitCombo.disableProperty().bind(check.selectedProperty().not());
      datePicker.disableProperty().bind(check.selectedProperty().not());

      if (initialItems != null) {
        for (ImportRequestItem initialItem : initialItems) {
          if (initialItem.merchandiseCode().equals(mCode)) {
            check.setSelected(true);
            qtyField.setText(String.valueOf(initialItem.quantityOrdered()));
            unitCombo.setValue(initialItem.unit());
            datePicker.setValue(initialItem.desiredDeliveryDate());
            break;
          }
        }
      }

      javafx.scene.layout.HBox rowBox = new javafx.scene.layout.HBox(10);
      rowBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
      rowBox.setPadding(new Insets(4, 0, 4, 0));
      rowBox.getChildren().addAll(check, qtyField, unitCombo, datePicker);
      
      rowsContainer.getChildren().add(rowBox);
      controlsList.add(new RowControls(check, qtyField, unitCombo, datePicker, mCode));
    }

    javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(rowsContainer);
    scrollPane.setFitToWidth(true);
    scrollPane.setPrefViewportHeight(350);
    scrollPane.setPrefViewportWidth(550);
    scrollPane.getStyleClass().add("scroll-pane");

    dialog.getDialogPane().setContent(scrollPane);

    javafx.scene.control.Button okButton = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
    okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
      boolean hasSelected = false;
      for (RowControls row : controlsList) {
        if (row.check().isSelected()) {
          hasSelected = true;
          String qtyText = row.qtyField().getText();
          int qty = 0;
          try {
            qty = qtyText == null || qtyText.isBlank() ? 0 : Integer.parseInt(qtyText.trim());
          } catch (NumberFormatException e) {
            // will catch below
          }
          if (qty <= 0) {
            Toast.error("Mặt hàng " + row.merchandiseCode() + " có số lượng phải là số nguyên dương");
            event.consume();
            return;
          }
          if (row.unitCombo().getValue() == null || row.unitCombo().getValue().isBlank()) {
            Toast.error("Mặt hàng " + row.merchandiseCode() + " chưa chọn đơn vị");
            event.consume();
            return;
          }
          if (row.datePicker().getValue() == null) {
            Toast.error("Mặt hàng " + row.merchandiseCode() + " chưa chọn ngày nhận mong muốn");
            event.consume();
            return;
          }
        }
      }

      if (!hasSelected) {
        Toast.error("Vui lòng chọn ít nhất một mặt hàng");
        event.consume();
      }
    });

    dialog.setResultConverter(buttonType -> {
      if (buttonType == javafx.scene.control.ButtonType.OK) {
        List<ImportRequestItem> result = new java.util.ArrayList<>();
        for (RowControls row : controlsList) {
          if (row.check().isSelected()) {
            int qty = Integer.parseInt(row.qtyField().getText().trim());
            result.add(new ImportRequestItem(
                row.merchandiseCode(),
                qty,
                row.unitCombo().getValue(),
                row.datePicker().getValue()
            ));
          }
        }
        return result;
      }
      return null;
    });

    java.util.Optional<List<ImportRequestItem>> result = dialog.showAndWait();
    return result.orElse(null);
  }

  private ImportRequest selected() {
    ImportRequest request = table.getSelectionModel().getSelectedItem();
    if (request == null) {
      throw new IllegalArgumentException("select a request first");
    }
    return request;
  }

  private void showItemsPopup(ImportRequest request) {
    UiSupport.showDetailsDialog(
        "Request Items - " + request.requestId(),
        "Items for Import Request " + request.requestId(),
        request.items(),
        List.of(
            UiSupport.column("Merchandise", ImportRequestItem::merchandiseCode, 150),
            UiSupport.column("Quantity", item -> String.valueOf(item.quantityOrdered()), 100),
            UiSupport.column("Unit", ImportRequestItem::unit, 100),
            UiSupport.column("Desired Date", item -> item.desiredDeliveryDate().toString(), 250)
        )
    );
  }

  private void run(Runnable action) {
    try {
      action.run();
      refresh();
      session.refresh();
      Toast.info("Request data saved");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }
}
