package vn.edu.hust.itss.group15.app.ui;

import java.util.LinkedHashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.FormFieldFactory;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.ImportSite;

public class SiteView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<ImportSite> table = new TableView<>();
  private final TextField siteCode = FormFieldFactory.text("SITE-SEA-01");
  private final TextField siteName = FormFieldFactory.text("Site name");
  private final TextField shipDays = FormFieldFactory.number("Ship days");
  private final TextField airDays = FormFieldFactory.number("Air days");
  private final VBox catalogChecks = new VBox(4);

  public SiteView(AppSession session) {
    this.session = session;
    getStyleClass().add("view");
    table.getColumns().addAll(java.util.List.of(
        UiSupport.column("Site", ImportSite::siteCode, 130),
        UiSupport.column("Name", ImportSite::siteName, 220),
        UiSupport.column("Ship days", site -> String.valueOf(site.deliveryDaysByShip()), 100),
        UiSupport.column("Air days", site -> String.valueOf(site.deliveryDaysByAir()), 100),
        UiSupport.column("Catalog", site -> String.join(", ", site.merchandiseCatalog()), 360)
    ));
    table.getStyleClass().add("data-table");
    table.getSelectionModel().selectedItemProperty().addListener((obs, old, site) -> fill(site));

    session.facade().store().merchandiseCatalog().forEach(code -> catalogChecks.getChildren().add(new CheckBox(code)));
    HBox checkWrap = new HBox(12, catalogChecks);

    var update = UiSupport.primary("Update Site");
    update.setOnAction(event -> run());
    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Site code", siteCode);
    UiSupport.row(form, 1, "Site name", siteName);
    UiSupport.row(form, 2, "Ship days", shipDays);
    UiSupport.row(form, 3, "Air days", airDays);
    UiSupport.row(form, 4, "Catalog", checkWrap);
    form.add(UiSupport.actions(update), 1, 5);
    setTop(UiSupport.panel(UiSupport.title("Site information"), UiSupport.subtitle("Maintain delivery lead times and merchandise catalogs."), form));
    setCenter(table);
    refresh();
  }

  @Override
  public void refresh() {
    table.setItems(FXCollections.observableArrayList(session.facade().sites().listSites()));
  }

  private void fill(ImportSite site) {
    if (site == null) {
      return;
    }
    siteCode.setText(site.siteCode());
    siteName.setText(site.siteName());
    shipDays.setText(String.valueOf(site.deliveryDaysByShip()));
    airDays.setText(String.valueOf(site.deliveryDaysByAir()));
    for (var node : catalogChecks.getChildren()) {
      if (node instanceof CheckBox checkBox) {
        checkBox.setSelected(site.merchandiseCatalog().contains(checkBox.getText()));
      }
    }
  }

  private void run() {
    try {
      session.facade().sites().updateSite(siteCode.getText(), siteName.getText(), UiSupport.intValue(shipDays), UiSupport.intValue(airDays), selectedCatalog());
      refresh();
      session.refresh();
      Toast.info("Site saved");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }

  private Set<String> selectedCatalog() {
    Set<String> selected = new LinkedHashSet<>();
    for (var node : catalogChecks.getChildren()) {
      if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
        selected.add(checkBox.getText());
      }
    }
    return selected;
  }
}
