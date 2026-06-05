package vn.edu.hust.itss.group15.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.domain.UserStatus;
import vn.edu.hust.itss.group15.application.port.Store;

public class AdminService {
  private static final Set<String> VALID_ROLES = Set.of(
      "SalesDepartment",
      "InternationalOrderingDepartment",
      "OverseasImportSite",
      "WarehouseManagementDepartment",
      "SystemAdministrator");

  private final Store store;
  private String backupSchedule = "Daily 23:00";

  public AdminService(Store store) {
    this.store = store;
  }

  public UserAccount createUser(String username, String email, Set<String> roles) {
    return createUser(username, email, roles, "");
  }

  public UserAccount createUser(String username, String email, Set<String> roles, String password) {
    if (!VALID_ROLES.containsAll(roles)) {
      throw new BusinessException("actorRole is not valid");
    }
    PasswordHasher.PasswordCredential credential = password == null || password.isBlank()
        ? new PasswordHasher.PasswordCredential("", "")
        : PasswordHasher.hash(password);
    UserAccount user = new UserAccount(
        store.nextId("user_accounts", "USR"),
        username,
        email,
        UserStatus.ACTIVE,
        roles,
        credential.hash(),
        credential.salt());
    store.saveUser(user);
    log("admin", "CREATE_USER", "Created " + username);
    return user;
  }

  public String updateBackupSchedule(String schedule) {
    if (schedule == null || schedule.isBlank()) {
      throw new BusinessException("backupSchedule is required");
    }
    backupSchedule = schedule.trim();
    log("admin", "UPDATE_BACKUP_SCHEDULE", backupSchedule);
    return backupSchedule;
  }

  public OperationLog log(String operatorId, String actionType, String details) {
    OperationLog log = new OperationLog(store.nextId("operation_logs", "LOG"), operatorId, actionType, LocalDateTime.now(), details);
    store.appendLog(log);
    return log;
  }

  public List<UserAccount> users() {
    return store.users();
  }

  public List<OperationLog> logs() {
    return store.logs();
  }

  public Set<String> validRoles() {
    return VALID_ROLES;
  }
}
