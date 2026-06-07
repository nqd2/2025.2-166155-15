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

  @Test
  void tcUc00203CreatesSiteAndMerchandise() {
    ImportOrderingFacade facade = facade();

    // 1. Create a new merchandise
    String formatted = facade.sites().createMerchandise("MH-015");
    assertEquals("MH-015", formatted);
    assertTrue(facade.store().merchandiseCatalog().contains("MH-015"));

    // 2. Create a new site utilizing the new merchandise
    var site = facade.sites().createSite("SITE-US-05", "USA Site", 25, 6, Set.of("MH-001", "MH-015"));
    assertEquals("USA Site", site.siteName());
    assertEquals(25, site.deliveryDaysByShip());
    assertEquals(6, site.deliveryDaysByAir());
    assertTrue(site.merchandiseCatalog().contains("MH-015"));

    // 3. Reject duplicate site code
    assertThrows(BusinessException.class, () -> facade.sites().createSite("SITE-US-05", "Duplicate", 10, 2, Set.of("MH-001")));

    // 4. Reject duplicate merchandise code
    assertThrows(BusinessException.class, () -> facade.sites().createMerchandise("MH-015"));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }
}
