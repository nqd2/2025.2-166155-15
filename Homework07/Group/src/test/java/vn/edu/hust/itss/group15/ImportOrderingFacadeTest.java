package vn.edu.hust.itss.group15;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.DeliveryMeans;
import vn.edu.hust.itss.group15.domain.GoodsReceipt;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.InventoryRecord;
import vn.edu.hust.itss.group15.domain.OrderStatus;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.domain.RequestStatus;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class ImportOrderingFacadeTest {
  private final ImportOrderingFacade facade = new ImportOrderingFacade(new InMemoryStore());

  @Test
  void createsUpdatesAndCancelsImportRequest() {
    ImportRequest request = facade.importRequests().create(List.of(item("MH-001", 10, 30)));

    assertEquals("IR-0001", request.requestId());
    assertEquals(RequestStatus.SUBMITTED, request.status());

    ImportRequest updated = facade.importRequests().update(request.requestId(), List.of(item("MH-002", 7, 25)));
    assertEquals(RequestStatus.UPDATED, updated.status());
    assertEquals("MH-002", updated.items().get(0).merchandiseCode());

    ImportRequest cancelled = facade.importRequests().cancel(request.requestId());
    assertEquals(RequestStatus.CANCELLED, cancelled.status());
    assertThrows(BusinessException.class, () -> facade.importRequests().update(request.requestId(), List.of(item("MH-001", 1, 20))));
  }

  @Test
  void rejectsInvalidImportRequestInput() {
    assertThrows(BusinessException.class, () -> facade.importRequests().create(List.of(item("UNKNOWN", 10, 30))));
    assertThrows(BusinessException.class, () -> facade.importRequests().create(List.of(new ImportRequestItem("MH-001", 1, "ton", LocalDate.now()))));
    assertThrows(BusinessException.class, () -> facade.importRequests().create(List.of(new ImportRequestItem("MH-001", 1, "box", LocalDate.now().minusDays(1)))));
    assertThrows(BusinessException.class, () -> facade.importRequests().create(List.of(new ImportRequestItem("MH-001", 1, "box", LocalDate.now()))));
  }

  @Test
  void updatesSiteProfileAndValidatesCatalog() {
    assertThrows(BusinessException.class, () -> facade.sites().updateSite("SITE-SEA-01", "Site", 0, 5, Set.of("MH-001")));
    assertThrows(BusinessException.class, () -> facade.sites().updateSite("SITE-SEA-01", "Site", 10, 5, Set.of("UNKNOWN")));

    var site = facade.sites().updateSite("SITE-SEA-01", "Singapore Fast Site", 12, 3, Set.of("MH-001", "MH-002"));

    assertEquals("Singapore Fast Site", site.siteName());
    assertEquals(12, site.deliveryDaysByShip());
  }

  @Test
  void findsCandidateSitesAndRecordsInventoryIncludingZero() {
    ImportRequest request = facade.importRequests().create(List.of(item("MH-014", 3, 20)));

    assertEquals(2, facade.inventory().findCandidateSites(request).size());

    InventoryRecord zeroStock = facade.inventory().recordStock("SITE-EU-04", "MH-014", 0, "box");
    assertEquals(0, zeroStock.inStockQuantity());
    assertThrows(BusinessException.class, () -> facade.inventory().recordStock("SITE-SEA-01", "MH-014", 5, "box"));
  }

  @Test
  void allocatesByShipBeforeAirThenByLargerStock() {
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
  void failsAllocationWhenSupplyIsNotEnough() {
    ImportRequest request = facade.importRequests().create(List.of(item("MH-004", 10, 40)));
    facade.inventory().recordStock("SITE-AIR-02", "MH-004", 5, "box");

    assertThrows(BusinessException.class, () -> facade.allocation().plan(request.requestId()));
  }

  @Test
  void generatesSiteGroupedOrdersAndHandlesResponses() {
    AllocationPlan plan = allocationPlanAcrossTwoSites();

    List<OverseasOrder> orders = facade.orders().generateOrders(plan.planId());

    assertEquals(2, orders.size());
    assertTrue(orders.stream().allMatch(order -> order.status() == OrderStatus.TRANSMITTED));
    assertEquals(OrderStatus.ACKNOWLEDGED, facade.orders().acknowledge(orders.get(0).orderId()).status());
    assertEquals(OrderStatus.REJECTED, facade.orders().reject(orders.get(1).orderId()).status());
  }

  @Test
  void receivesGoodsRequiresDiscrepancyNoteAndUpdatesWarehouseStock() {
    AllocationPlan plan = allocationPlanAcrossTwoSites();
    OverseasOrder order = facade.orders().generateOrders(plan.planId()).get(0);
    String merchandiseCode = order.orderLines().get(0).merchandiseCode();
    int orderedQuantity = order.orderLines().get(0).quantityOrdered();

    assertThrows(BusinessException.class, () -> facade.receipts().receive(
        order.orderId(),
        Map.of(merchandiseCode, orderedQuantity - 1),
        Map.of()));
    assertTrue(facade.store().warehouseStock().isEmpty());

    GoodsReceipt receipt = facade.receipts().receive(
        order.orderId(),
        Map.of(merchandiseCode, orderedQuantity - 1),
        Map.of(merchandiseCode, "Supplier shipped one box short"));

    assertTrue(receipt.hasDiscrepancy());
    assertEquals(orderedQuantity - 1, facade.store().warehouseStock().get(0).quantityInStock());
  }

  @Test
  void managesUsersBackupScheduleAndLogs() {
    assertThrows(BusinessException.class, () -> facade.admin().createUser("bad", "bad@example.com", Set.of("BadRole")));
    assertThrows(IllegalArgumentException.class, () -> facade.admin().createUser("bad-email", "bad-email", Set.of("SystemAdministrator")));

    UserAccount user = facade.admin().createUser("admin01", "admin01@example.com", Set.of("SystemAdministrator"));
    String schedule = facade.admin().updateBackupSchedule("Hourly");

    assertEquals("USR-0002", user.userId());
    assertEquals("Hourly", schedule);
    assertFalse(facade.admin().logs().isEmpty());
  }

  private AllocationPlan allocationPlanAcrossTwoSites() {
    ImportRequest request = facade.importRequests().create(List.of(item("MH-001", 10, 25)));
    facade.inventory().recordStock("SITE-SEA-01", "MH-001", 5, "box");
    facade.inventory().recordStock("SITE-EU-04", "MH-001", 20, "box");
    return facade.allocation().plan(request.requestId());
  }

  private ImportRequestItem item(String merchandiseCode, int quantity, int daysFromNow) {
    return new ImportRequestItem(merchandiseCode, quantity, "box", LocalDate.now().plusDays(daysFromNow));
  }
}
