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
import vn.edu.hust.itss.group15.domain.RequestStatus;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class Uc001ImportRequestTest {
  @Test
  void tcUc00101CreatesUpdatesAndCancelsImportRequest() {
    ImportOrderingFacade facade = facade();

    ImportRequest request = facade.importRequests().create(List.of(item("MH-001", 10, 30)));
    ImportRequest updated = facade.importRequests().update(request.requestId(), List.of(item("MH-002", 7, 25)));
    ImportRequest cancelled = facade.importRequests().cancel(request.requestId());

    assertEquals(RequestStatus.SUBMITTED, request.status());
    assertEquals(RequestStatus.UPDATED, updated.status());
    assertEquals("MH-002", updated.items().getFirst().merchandiseCode());
    assertEquals(RequestStatus.CANCELLED, cancelled.status());
    assertThrows(BusinessException.class, () -> facade.importRequests().update(request.requestId(), List.of(item("MH-001", 1, 20))));
  }

  @Test
  void tcUc00102RejectsInvalidImportRequestInput() {
    ImportOrderingFacade facade = facade();

    assertThrows(BusinessException.class, () -> facade.importRequests().create(List.of(item("UNKNOWN", 10, 30))));
    assertThrows(BusinessException.class, () -> facade.importRequests().create(List.of(new ImportRequestItem("MH-001", 1, "ton", LocalDate.now().plusDays(10)))));
    assertThrows(BusinessException.class, () -> facade.importRequests().create(List.of(new ImportRequestItem("MH-001", 1, "box", LocalDate.now()))));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }

  private ImportRequestItem item(String merchandiseCode, int quantity, int daysFromNow) {
    return new ImportRequestItem(merchandiseCode, quantity, "box", LocalDate.now().plusDays(daysFromNow));
  }
}
