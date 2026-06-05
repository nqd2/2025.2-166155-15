package vn.edu.hust.itss.group15.app;

import java.util.Set;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;

public class AppSession {
  private final ImportOrderingFacade facade;
  private final ObjectProperty<UserAccount> currentUser = new SimpleObjectProperty<>();
  private Runnable refreshHandler = () -> {};

  public AppSession(ImportOrderingFacade facade) {
    this.facade = facade;
  }

  public ImportOrderingFacade facade() {
    return facade;
  }

  public UserAccount currentUser() {
    return currentUser.get();
  }

  public ObjectProperty<UserAccount> currentUserProperty() {
    return currentUser;
  }

  public void login(UserAccount user) {
    currentUser.set(user);
  }

  public void logout() {
    currentUser.set(null);
  }

  public boolean canAccess(String section) {
    UserAccount user = currentUser();
    if (user == null) {
      return false;
    }
    Set<String> roles = user.actorRoles();
    if (roles.contains("SystemAdministrator")) {
      return true;
    }
    return switch (section) {
      case "Requests" -> roles.contains("SalesDepartment");
      case "Sites", "Inventory", "Allocation", "Orders" -> roles.contains("InternationalOrderingDepartment");
      case "Receiving" -> roles.contains("WarehouseManagementDepartment");
      case "Overview" -> true;
      default -> false;
    };
  }

  public void onRefresh(Runnable refreshHandler) {
    this.refreshHandler = refreshHandler;
  }

  public void refresh() {
    refreshHandler.run();
  }
}
