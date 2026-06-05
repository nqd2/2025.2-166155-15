package vn.edu.hust.itss.group15;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.domain.AllocationPolicy;
import vn.edu.hust.itss.group15.domain.DeliveryMeans;
import vn.edu.hust.itss.group15.domain.DomainException;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.ImportSite;
import vn.edu.hust.itss.group15.domain.InventoryRecord;
import vn.edu.hust.itss.group15.domain.RequestStatus;

class AllocationPolicyTest {
  private final AllocationPolicy policy = new AllocationPolicy();
  private final LocalDate today = LocalDate.of(2026, 6, 1);

  @Test
  void prefersShipBeforeAirAndThenLargerStock() {
    ImportRequest request = request("MH-001", 12, today.plusDays(25));
    List<InventoryRecord> inventory = List.of(
        new InventoryRecord("SEA", "MH-001", 5, "box"),
        new InventoryRecord("AIR", "MH-001", 20, "box"));
    Map<String, ImportSite> sites = Map.of(
        "SEA", new ImportSite("SEA", "Sea Site", 18, 5, Set.of("MH-001")),
        "AIR", new ImportSite("AIR", "Air Site", 35, 4, Set.of("MH-001")));

    var lines = policy.allocate(request, inventory, sites, today);

    assertEquals("SEA", lines.get(0).siteCode());
    assertEquals(DeliveryMeans.SHIP, lines.get(0).deliveryMeans());
    assertEquals("AIR", lines.get(1).siteCode());
    assertEquals(DeliveryMeans.AIR, lines.get(1).deliveryMeans());
  }

  @Test
  void rejectsInsufficientSupplyInsideDomain() {
    ImportRequest request = request("MH-001", 12, today.plusDays(25));
    List<InventoryRecord> inventory = List.of(new InventoryRecord("SEA", "MH-001", 5, "box"));
    Map<String, ImportSite> sites = Map.of("SEA", new ImportSite("SEA", "Sea Site", 18, 5, Set.of("MH-001")));

    assertThrows(DomainException.class, () -> policy.allocate(request, inventory, sites, today));
  }

  @Test
  void rejectsDesiredDateIsToday() {
    ImportRequest request = request("MH-001", 12, today);
    List<InventoryRecord> inventory = List.of(new InventoryRecord("SEA", "MH-001", 20, "box"));
    Map<String, ImportSite> sites = Map.of("SEA", new ImportSite("SEA", "Sea Site", 18, 5, Set.of("MH-001")));

    assertThrows(DomainException.class, () -> policy.allocate(request, inventory, sites, today));
  }

  private ImportRequest request(String merchandiseCode, int quantity, LocalDate desiredDate) {
    return new ImportRequest(
        "IR-0001",
        RequestStatus.SUBMITTED,
        LocalDateTime.of(2026, 6, 1, 9, 0),
        List.of(new ImportRequestItem(merchandiseCode, quantity, "box", desiredDate)));
  }
}
