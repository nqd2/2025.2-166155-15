package vn.edu.hust.itss.group15.app.ui;

import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import vn.edu.hust.itss.group15.app.AppSession;
import vn.edu.hust.itss.group15.app.ui.components.Toast;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.infrastructure.persistence.supabase.Dotenv;

public class LoginView extends BorderPane {
  public LoginView(AppSession session, Consumer<UserAccount> onLogin) {
    getStyleClass().add("login-root");

    boolean firstAdmin = session.facade().auth().needsFirstAdmin();
    Label title = new Label(firstAdmin ? "Create first admin" : Dotenv.get("IMPORT_APP_NAME", "Nexus Import Portal"));
    title.getStyleClass().add("login-title");
    Label subtitle = new Label(firstAdmin ? "Bootstrap the first SystemAdministrator account." : "Sign in to continue.");
    subtitle.getStyleClass().add("login-subtitle");

    TextField username = new TextField();
    username.setPromptText("Username");
    TextField email = new TextField();
    email.setPromptText("Email");
    email.setVisible(firstAdmin);
    email.setManaged(firstAdmin);
    PasswordField password = new PasswordField();
    password.setPromptText("Password");

    Button submit = UiSupport.primary(firstAdmin ? "Create admin" : "Sign in");
    submit.setMaxWidth(Double.MAX_VALUE);
    submit.setOnAction(event -> {
      try {
        UserAccount user = firstAdmin
            ? session.facade().auth().createFirstAdmin(username.getText(), email.getText(), password.getText())
            : session.facade().auth().login(username.getText(), password.getText());
        session.login(user);
        onLogin.accept(user);
      } catch (RuntimeException exception) {
        Toast.error(exception.getMessage());
      }
    });

    GridPane form = UiSupport.form();
    UiSupport.row(form, 0, "Username", username);
    if (firstAdmin) {
      UiSupport.row(form, 1, "Email", email);
    }
    UiSupport.row(form, firstAdmin ? 2 : 1, "Password", password);
    form.add(submit, 1, firstAdmin ? 3 : 2);

    VBox card = new VBox(14, title, subtitle, form);
    card.setMaxWidth(480);
    card.setAlignment(Pos.CENTER_LEFT);
    card.setPadding(new Insets(28));
    card.getStyleClass().add("login-card");
    setCenter(card);
  }
}
