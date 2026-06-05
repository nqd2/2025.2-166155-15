package vn.edu.hust.itss.group15.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.DeliveryMeans;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.RequestStatus;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class Uc004AllocationTest {
  @Test
  void tcUc00401PlansAllocationByShipBeforeAirAndMarksRequestAllocated() {
    ImportOrderingFacade facade = facade();
    ImportRequest request = facade.importRequests().create(List.of(item("MH-001", 10, 25)));
    facade.inventory().recordStock("SITE-SEA-01", "MH-001", 5, "box");
    facade.inventory().recordStock("SITE-EU-04", "MH-001", 20, "box");

    AllocationPlan plan = facade.allocation().plan(request.requestId());

    assertEquals(2, plan.lines().size());
    assertEquals("SITE-SEA-01", plan.lines().get(0).siteCode());
    assertEquals(DeliveryMeans.SHIP, plan.lines().get(0).deliveryMeans());
    assertEquals("SITE-EU-04", plan.lines().get(1).siteCode());
    assertEquals(DeliveryMeans.AIR, plan.lines().get(1).deliveryMeans());
    assertEquals(RequestStatus.ALLOCATED, facade.importRequests().find(request.requestId()).status());
  }

  @Test
  void tcUc00402FailsAllocationWhenSupplyIsInsufficient() {
    ImportOrderingFacade facade = facade();
    ImportRequest request = facade.importRequests().create(List.of(item("MH-004", 10, 40)));
    facade.inventory().recordStock("SITE-AIR-02", "MH-004", 5, "box");

    assertThrows(BusinessException.class, () -> facade.allocation().plan(request.requestId()));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }

  private ImportRequestItem item(String merchandiseCode, int quantity, int daysFromNow) {
    return new ImportRequestItem(merchandiseCode, quantity, "box", LocalDate.now().plusDays(daysFromNow));
  }
}
