package vn.edu.hust.itss.group15.application;

import java.util.Set;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.domain.UserStatus;
import vn.edu.hust.itss.group15.application.port.Store;

public class AuthService {
  private final Store store;
  private final AdminService adminService;

  public AuthService(Store store, AdminService adminService) {
    this.store = store;
    this.adminService = adminService;
  }

  public boolean needsFirstAdmin() {
    return store.users().isEmpty();
  }

  public UserAccount createFirstAdmin(String username, String email, String password) {
    if (!needsFirstAdmin()) {
      throw new BusinessException("first admin already exists");
    }
    return adminService.createUser(username, email, Set.of("SystemAdministrator"), password);
  }

  public UserAccount login(String username, String password) {
    UserAccount user = store.findUserByUsername(username)
        .orElseThrow(() -> new BusinessException("invalid username or password"));
    if (user.status() != UserStatus.ACTIVE || !PasswordHasher.matches(password, user.passwordHash(), user.passwordSalt())) {
      throw new BusinessException("invalid username or password");
    }
    return user;
  }
}
