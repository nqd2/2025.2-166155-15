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
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
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
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class FeatureTestMatrixTest {
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

  @Test
  void tcUc00601ReceivesMatchedGoodsAndUpdatesWarehouseStock() {
    ImportOrderingFacade facade = facade();
    OverseasOrder order = facade.orders().generateOrders(allocationPlanAcrossTwoSites(facade).planId()).getFirst();
    String merchandiseCode = order.orderLines().getFirst().merchandiseCode();
    int orderedQuantity = order.orderLines().getFirst().quantityOrdered();

    GoodsReceipt receipt = facade.receipts().receive(order.orderId(), Map.of(merchandiseCode, orderedQuantity), Map.of());

    assertFalse(receipt.hasDiscrepancy());
    assertEquals(orderedQuantity, facade.store().warehouseStock().getFirst().quantityInStock());
  }

  @Test
  void tcUc00602RequiresDiscrepancyNoteForShortOrExcessReceipt() {
    ImportOrderingFacade facade = facade();
    OverseasOrder deficitOrder = facade.orders().generateOrders(allocationPlanAcrossTwoSites(facade).planId()).getFirst();
    String deficitCode = deficitOrder.orderLines().getFirst().merchandiseCode();
    int deficitOrdered = deficitOrder.orderLines().getFirst().quantityOrdered();

    assertThrows(BusinessException.class, () -> facade.receipts().receive(
        deficitOrder.orderId(),
        Map.of(deficitCode, deficitOrdered - 1),
        Map.of()));

    GoodsReceipt surplusReceipt = facade.receipts().receive(
        deficitOrder.orderId(),
        Map.of(deficitCode, deficitOrdered + 1),
        Map.of(deficitCode, "Supplier shipped one extra box"));
    assertTrue(surplusReceipt.hasDiscrepancy());
    assertEquals("SURPLUS", surplusReceipt.lines().getFirst().discrepancyType());
  }

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
