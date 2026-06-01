package vn.edu.hust.itss.group15.app.ui;

import java.util.LinkedHashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.FormFieldFactory;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.UserAccount;

public class AdminView extends BorderPane implements DashboardView.Refreshable {
  private final AppSession session;
  private final TableView<UserAccount> users = new TableView<>();
  private final TableView<OperationLog> logs = new TableView<>();
  private final TextField username = FormFieldFactory.text("username");
  private final TextField email = FormFieldFactory.text("email@example.com");
  private final PasswordField password = new PasswordField();
  private final TextField backup = FormFieldFactory.text("Daily 23:00");
  private final VBox roleChecks = new VBox(4);

  public AdminView(AppSession session) {
    this.session = session;
    getStyleClass().add("view");
    password.setPromptText("At least 8 characters");
    session.facade().admin().validRoles().forEach(role -> roleChecks.getChildren().add(new CheckBox(role)));

    users.getColumns().addAll(
        UiSupport.column("User", UserAccount::userId, 100),
        UiSupport.column("Username", UserAccount::username, 160),
        UiSupport.column("Email", UserAccount::email, 240),
        UiSupport.column("Roles", user -> String.join(", ", user.actorRoles()), 420));
    users.getStyleClass().add("data-table");

    logs.getColumns().addAll(
        UiSupport.column("Time", log -> log.timestamp().toString(), 180),
        UiSupport.column("Operator", OperationLog::operatorId, 120),
        UiSupport.column("Action", OperationLog::actionType, 180),
        UiSupport.column("Details", OperationLog::details, 360));
    logs.getStyleClass().add("data-table");

    var create = UiSupport.primary("Create user");
    create.setOnAction(event -> createUser());
    var schedule = UiSupport.secondary("Update backup");
    schedule.setOnAction(event -> updateBackup());
    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Username", username);
    UiSupport.row(form, 1, "Email", email);
    UiSupport.row(form, 2, "Password", password);
    UiSupport.row(form, 3, "Roles", roleChecks);
    UiSupport.row(form, 4, "Backup schedule", backup);
    form.add(UiSupport.actions(create, schedule), 1, 5);

    TabPane tabs = new TabPane(new Tab("Users", users), new Tab("Logs", logs));
    tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
    setTop(UiSupport.panel(UiSupport.title("Administration"), UiSupport.subtitle("Manage operators, roles, audit trail, and backup settings."), form));
    setCenter(tabs);
    refresh();
  }

  @Override
  public void refresh() {
    users.setItems(FXCollections.observableArrayList(session.facade().admin().users()));
    logs.setItems(FXCollections.observableArrayList(session.facade().admin().logs()));
  }

  private void createUser() {
    try {
      session.facade().admin().createUser(username.getText(), email.getText(), selectedRoles(), password.getText());
      refresh();
      session.refresh();
      Toast.info("User created");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }

  private void updateBackup() {
    try {
      session.facade().admin().updateBackupSchedule(backup.getText());
      refresh();
      session.refresh();
      Toast.info("Backup schedule updated");
    } catch (RuntimeException exception) {
      Toast.error(exception.getMessage());
    }
  }

  private Set<String> selectedRoles() {
    Set<String> selected = new LinkedHashSet<>();
    for (var node : roleChecks.getChildren()) {
      if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
        selected.add(checkBox.getText());
      }
    }
    return selected;
  }
}
