package vn.edu.hust.itss.group15.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class Uc007AdminTest {
  @Test
  void tcUc00701CreatesUserAssignsRoleAndWritesOperationLog() {
    ImportOrderingFacade facade = facade();

    UserAccount user = facade.admin().createUser("sales01", "sales01@example.com", Set.of("SalesDepartment"));

    assertEquals("sales01", user.username());
    assertTrue(user.actorRoles().contains("SalesDepartment"));
    assertTrue(facade.admin().logs().stream().anyMatch(log -> log.actionType().equals("CREATE_USER")));
  }

  @Test
  void tcUc00702RejectsInvalidUserDataAndUpdatesBackupSchedule() {
    ImportOrderingFacade facade = facade();

    assertThrows(BusinessException.class, () -> facade.admin().createUser("invalid-role", "user@example.com", Set.of("BadRole")));
    assertThrows(IllegalArgumentException.class, () -> facade.admin().createUser("bad-email", "bad-email", Set.of("SystemAdministrator")));

    assertEquals("Daily 23:00", facade.admin().updateBackupSchedule("  Daily 23:00  "));
    assertThrows(BusinessException.class, () -> facade.admin().updateBackupSchedule(" "));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }
}
