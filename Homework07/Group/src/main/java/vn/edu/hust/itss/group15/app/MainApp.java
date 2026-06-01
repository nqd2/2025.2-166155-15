package vn.edu.hust.itss.group15.app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vn.edu.hust.itss.group15.app.ui.DashboardView;
import vn.edu.hust.itss.group15.app.ui.LoginView;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.infrastructure.persistence.supabase.Dotenv;
import vn.edu.hust.itss.group15.infrastructure.persistence.supabase.SupabaseConfig;
import vn.edu.hust.itss.group15.infrastructure.persistence.supabase.SupabaseStore;

public class MainApp extends Application {
  private Stage stage;
  private AppSession session;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    stage.setTitle(Dotenv.get("IMPORT_APP_NAME", "Nexus Import Portal"));
    try {
      session = new AppSession(new ImportOrderingFacade(new SupabaseStore(SupabaseConfig.fromEnvironment())));
      showLogin();
    } catch (RuntimeException exception) {
      showSetupError(exception.getMessage());
    }
    stage.show();
  }

  private void showLogin() {
    Scene scene = new Scene(new LoginView(session, user -> showDashboard()), 1180, 760);
    applyCss(scene);
    stage.setScene(scene);
  }

  private void showDashboard() {
    Scene scene = new Scene(new DashboardView(session, this::showLogin), 1280, 820);
    applyCss(scene);
    stage.setScene(scene);
  }

  private void showSetupError(String message) {
    Label title = new Label("Database setup required");
    title.getStyleClass().add("login-title");
    Label body = new Label("""
        Set these variables in a .env file or environment, then run the app again:
        IMPORT_SUPABASE_URL=https://<project-ref>.supabase.co
        IMPORT_SUPABASE_KEY=<supabase anon or service role key>

        Run src/main/resources/db/schema.sql in Supabase SQL Editor before first launch.

        Error: %s
        """.formatted(message));
    body.getStyleClass().add("login-subtitle");
    VBox card = new VBox(18, title, body);
    card.setMaxWidth(760);
    card.setPadding(new Insets(28));
    card.getStyleClass().add("login-card");
    BorderPane root = new BorderPane(card);
    root.setPadding(new Insets(40));
    BorderPane.setAlignment(card, Pos.CENTER);
    root.getStyleClass().add("login-root");
    Scene scene = new Scene(root, 980, 520);
    applyCss(scene);
    stage.setScene(scene);
  }

  private void applyCss(Scene scene) {
    scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
  }
}
