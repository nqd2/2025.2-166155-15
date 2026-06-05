package vn.edu.hust.itss.group15.app.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import vn.edu.hust.itss.group15.app.AppSession;

public class DashboardView extends BorderPane {
  private final AppSession session;
  private final StackPane content = new StackPane();
  private final Map<String, Node> views = new LinkedHashMap<>();
  private final VBox nav = new VBox(6);
  private final Label userLabel = new Label();

  public DashboardView(AppSession session, Runnable onLogout) {
    this.session = session;
    getStyleClass().add("app-root");
    buildViews();
    setLeft(sidebar());
    setTop(topbar(onLogout));
    setCenter(content);
    session.onRefresh(this::refreshCurrent);
    show("Overview");
  }

  private void buildViews() {
    views.put("Overview", new OverviewView(session));
    views.put("Requests", new RequestView(session));
    views.put("Sites", new SiteView(session));
    views.put("Inventory", new InventoryView(session));
    views.put("Allocation", new AllocationView(session));
    views.put("Orders", new OrderView(session));
    views.put("Receiving", new ReceivingView(session));
    views.put("Admin", new AdminView(session));
  }

  private Node sidebar() {
    VBox sidebar = new VBox(18);
    sidebar.setPadding(new Insets(18));
    sidebar.getStyleClass().add("sidebar");
    Label brand = new Label("ImportOps");
    brand.getStyleClass().add("brand");
    Label caption = new Label("Ordering Control");
    caption.getStyleClass().add("brand-caption");
    nav.getStyleClass().add("nav");
    views.keySet().forEach(name -> {
      Button button = new Button(name);
      button.getStyleClass().add("nav-button");
      button.setMaxWidth(Double.MAX_VALUE);
      button.setDisable(!session.canAccess(name));
      button.setOnAction(event -> show(name));
      nav.getChildren().add(button);
    });
    sidebar.getChildren().addAll(brand, caption, nav);
    return sidebar;
  }

  private Node topbar(Runnable onLogout) {
    HBox topbar = new HBox(12);
    topbar.setAlignment(Pos.CENTER_LEFT);
    topbar.setPadding(new Insets(14, 18, 14, 18));
    topbar.getStyleClass().add("topbar");
    Label db = new Label("Supabase REST connected");
    db.getStyleClass().add("db-pill");
    refreshUserLabel();
    Button refresh = UiSupport.secondary("Refresh");
    refresh.setOnAction(event -> refreshCurrent());
    Button logout = UiSupport.secondary("Logout");
    logout.setOnAction(event -> {
      session.logout();
      onLogout.run();
    });
    HBox spacer = new HBox();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    topbar.getChildren().addAll(userLabel, db, spacer, refresh, logout);
    return topbar;
  }

  private void show(String name) {
    Node view = views.get(name);
    if (view == null || !session.canAccess(name)) {
      return;
    }
    content.getChildren().setAll(view);
    refreshCurrent();
    for (Node node : nav.getChildren()) {
      node.getStyleClass().remove("nav-button-active");
      if (node instanceof Button button && button.getText().equals(name)) {
        node.getStyleClass().add("nav-button-active");
      }
    }
  }

  private void refreshCurrent() {
    refreshUserLabel();
    for (Node node : content.getChildren()) {
      if (node instanceof Refreshable refreshable) {
        refreshable.refresh();
      }
    }
  }

  private void refreshUserLabel() {
    userLabel.setText(session.currentUser().username() + " | " + String.join(", ", session.currentUser().actorRoles()));
    userLabel.getStyleClass().add("user-label");
  }

  interface Refreshable {
    void refresh();
  }
}
