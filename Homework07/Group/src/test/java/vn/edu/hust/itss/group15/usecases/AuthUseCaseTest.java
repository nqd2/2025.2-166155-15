package vn.edu.hust.itss.group15.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class AuthUseCaseTest {
  @Test
  void tcAuth01CreatesFirstAdminAndLogsIn() {
    ImportOrderingFacade facade = facade();

    UserAccount admin = facade.auth().createFirstAdmin("admin01", "admin01@example.com", "Secret123!");
    UserAccount loggedIn = facade.auth().login("admin01", "Secret123!");

    assertEquals(admin.userId(), loggedIn.userId());
    assertTrue(loggedIn.actorRoles().contains("SystemAdministrator"));
    assertFalse(facade.auth().needsFirstAdmin());
  }

  @Test
  void tcAuth02RejectsWrongPassword() {
    ImportOrderingFacade facade = facade();
    facade.auth().createFirstAdmin("admin01", "admin01@example.com", "Secret123!");

    assertThrows(BusinessException.class, () -> facade.auth().login("admin01", "wrong-password"));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }
}
