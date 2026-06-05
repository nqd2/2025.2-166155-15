package vn.edu.hust.itss.group15.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.InventoryRecord;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class Uc003InventoryTest {
  @Test
  void tcUc00301FindsCandidateSitesAndRecordsZeroStock() {
    ImportOrderingFacade facade = facade();
    ImportRequest request = facade.importRequests().create(List.of(item("MH-014", 3, 20)));

    List<String> candidateCodes = facade.inventory().findCandidateSites(request).stream()
        .map(site -> site.siteCode())
        .toList();
    InventoryRecord zeroStock = facade.inventory().recordStock("SITE-EU-04", "MH-014", 0, "box");

    assertEquals(List.of("SITE-EU-04", "SITE-AIR-02"), candidateCodes);
    assertEquals(0, zeroStock.inStockQuantity());
    assertEquals("MH-014", zeroStock.merchandiseCode());
  }

  @Test
  void tcUc00302RejectsStockForUntradedMerchandise() {
    ImportOrderingFacade facade = facade();

    assertThrows(BusinessException.class, () -> facade.inventory().recordStock("SITE-SEA-01", "MH-014", 5, "box"));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }

  private ImportRequestItem item(String merchandiseCode, int quantity, int daysFromNow) {
    return new ImportRequestItem(merchandiseCode, quantity, "box", LocalDate.now().plusDays(daysFromNow));
  }
}
