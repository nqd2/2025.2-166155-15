package vn.edu.hust.itss.group15.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.application.BusinessException;
import vn.edu.hust.itss.group15.application.ImportOrderingFacade;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.GoodsReceipt;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.infrastructure.persistence.memory.InMemoryStore;

class Uc006GoodsReceiptTest {
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
