package vn.edu.hust.itss.group15.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.OrderStatus;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class Uc005OrderTransmissionTest {
  @Test
  void tcUc00501GeneratesSiteGroupedTransmittedOrders() {
    ImportOrderingFacade facade = facade();
    AllocationPlan plan = allocationPlanAcrossTwoSites(facade);

    List<OverseasOrder> orders = facade.orders().generateOrders(plan.planId());

    assertEquals(2, orders.size());
    assertTrue(orders.stream().allMatch(order -> order.status() == OrderStatus.TRANSMITTED));
    assertTrue(orders.stream().anyMatch(order -> order.siteCode().equals("SITE-SEA-01")));
    assertTrue(orders.stream().anyMatch(order -> order.siteCode().equals("SITE-EU-04")));
  }

  @Test
  void tcUc00502RecordsSiteAcknowledgementAndRejection() {
    ImportOrderingFacade facade = facade();
    List<OverseasOrder> orders = facade.orders().generateOrders(allocationPlanAcrossTwoSites(facade).planId());

    OverseasOrder acknowledged = facade.orders().acknowledge(orders.get(0).orderId());
    OverseasOrder rejected = facade.orders().reject(orders.get(1).orderId());

    assertEquals(OrderStatus.ACKNOWLEDGED, acknowledged.status());
    assertTrue(acknowledged.acknowledgementToken().startsWith("ACK-"));
    assertEquals(OrderStatus.REJECTED, rejected.status());
    assertTrue(rejected.acknowledgementToken().startsWith("REJ-"));
  }

  private ImportOrderingFacade facade() {
    return new ImportOrderingFacade(new InMemoryStore());
  }

  private ImportRequestItem item(String merchandiseCode, int quantity, int daysFromNow) {
    return new ImportRequestItem(merchandiseCode, quantity, "box", LocalDate.now().plusDays(daysFromNow));
  }

  private AllocationPlan allocationPlanAcrossTwoSites(ImportOrderingFacade facade) {
    ImportRequest request = facade.importRequests().create(List.of(item("MH-001", 10, 25)));
    facade.inventory().recordStock("SITE-SEA-01", "MH-001", 5, "box");
    facade.inventory().recordStock("SITE-EU-04", "MH-001", 20, "box");
    return facade.allocation().plan(request.requestId());
  }
}
