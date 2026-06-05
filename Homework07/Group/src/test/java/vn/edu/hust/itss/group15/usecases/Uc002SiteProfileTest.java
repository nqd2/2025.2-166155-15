package vn.edu.hust.itss.group15.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class Uc002SiteProfileTest {
  @Test
  void tcUc00201UpdatesValidSiteProfile() {
    ImportOrderingFacade facade = facade();

    var site = facade.sites().updateSite("SITE-SEA-01", "Singapore Fast Site", 12, 3, Set.of("MH-001", "MH-002"));

    assertEquals("Singapore Fast Site", site.siteName());
    assertEquals(12, site.deliveryDaysByShip());
    assertEquals(3, site.deliveryDaysByAir());
    assertTrue(site.merchandiseCatalog().contains("MH-002"));
  }

  @Test
  void tcUc00202RejectsInvalidTransitTimeOrCatalog() {
    ImportOrderingFacade facade = facade();

    assertThrows(BusinessException.class, () -> facade.sites().updateSite("SITE-SEA-01", "Site", 0, 5, Set.of("MH-001")));
    assertThrows(BusinessException.class, () -> facade.sites().updateSite("SITE-SEA-01", "Site", 10, 5, Set.of("UNKNOWN")));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }
}
