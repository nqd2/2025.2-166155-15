package vn.edu.hust.itss.group15.app.ui;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.AllocationPlan;

public class AllocationView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<AllocationPlan> table = new TableView<>();
  private final ComboBox<String> request = new ComboBox<>();
  private final TextArea preview = new TextArea();

  public AllocationView(AppSession session) {
    this.session = session;
    getStyleClass().add("view");
    preview.setEditable(false);
    preview.setPrefRowCount(5);
    table.getColumns().addAll(java.util.List.of(
        UiSupport.column("Plan", AllocationPlan::planId, 120),
        UiSupport.column("Request", AllocationPlan::requestId, 120),
        UiSupport.column("Lines", plan -> plan.lines().size() + " line(s) (Double-click to view)", 680)
    ));
    table.getStyleClass().add("data-table");
    table.setRowFactory(tv -> {
      javafx.scene.control.TableRow<AllocationPlan> row = new javafx.scene.control.TableRow<>();
      row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (!row.isEmpty())) {
          showLinesPopup(row.getItem());
        }
      });
      return row;
    });

    var previewButton = UiSupport.secondary("Preview");
    previewButton.setOnAction(event -> runPreview());
    var save = UiSupport.primary("Commit allocation");
    save.setOnAction(event -> runSave());
    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Request", request);
    UiSupport.row(form, 1, "Preview", preview);
    form.add(UiSupport.actions(previewButton, save), 1, 2);
    setTop(UiSupport.panel(UiSupport.title("Allocation planning"), UiSupport.subtitle("Prefer ship, larger stock, and fewer Sites where possible."), form));
    setCenter(table);
    refresh();
  }

  @Override
  public void refresh() {
    request.getItems().setAll(session.facade().importRequests().list().stream().map(item -> item.requestId()).toList());
    if (!request.getItems().isEmpty() && request.getValue() == null) {
      request.getSelectionModel().selectFirst();
    }
    table.setItems(FXCollections.observableArrayList(session.facade().allocation().list()));
  }

  private void showLinesPopup(AllocationPlan plan) {
    UiSupport.showDetailsDialog(
        "Allocation Lines - " + plan.planId(),
        "Lines for Allocation Plan " + plan.planId(),
        plan.lines(),
        java.util.List.of(
            UiSupport.column("Request", vn.edu.hust.itss.group15.domain.AllocationLine::requestId, 100),
            UiSupport.column("Merchandise", vn.edu.hust.itss.group15.domain.AllocationLine::merchandiseCode, 120),
            UiSupport.column("Site", vn.edu.hust.itss.group15.domain.AllocationLine::siteCode, 120),
            UiSupport.column("Quantity", line -> String.valueOf(line.quantityOrdered()), 100),
            UiSupport.column("Unit", vn.edu.hust.itss.group15.domain.AllocationLine::unit, 80),
            UiSupport.column("Means", line -> line.deliveryMeans().name(), 100)
        )
    );
  }

  private String formatLines(java.util.List<vn.edu.hust.itss.group15.domain.AllocationLine> lines) {
    StringBuilder sb = new StringBuilder();
    for (var line : lines) {
      sb.append(line.merchandiseCode())
        .append(": ")
        .append(line.quantityOrdered())
        .append(" ")
        .append(line.unit())
        .append(" from ")
        .append(line.siteCode())
        .append(" via ")
        .append(line.deliveryMeans())
        .append("\n");
    }
    return sb.toString();
  }

  private void runPreview() {
    try {
      AllocationPlan plan = session.facade().allocation().preview(request.getValue());
      preview.setText(formatLines(plan.lines()));
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }

  private void runSave() {
    try {
      AllocationPlan plan = session.facade().allocation().plan(request.getValue());
      preview.setText(formatLines(plan.lines()));
      refresh();
      session.refresh();
      Toast.info("Allocation committed");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }
}
