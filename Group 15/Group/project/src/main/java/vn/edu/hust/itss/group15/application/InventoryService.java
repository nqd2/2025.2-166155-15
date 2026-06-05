package vn.edu.hust.itss.group15.application;

import java.util.List;
import java.time.LocalDateTime;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.ImportSite;
import vn.edu.hust.itss.group15.domain.InventoryRecord;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.application.port.Store;

public class InventoryService {
  private final Store store;

  public InventoryService(Store store) {
    this.store = store;
  }

  public List<ImportSite> findCandidateSites(ImportRequest request) {
    return store.sites().stream()
        .filter(site -> request.items().stream().map(ImportRequestItem::merchandiseCode).anyMatch(site::trades))
        .toList();
  }

  public InventoryRecord recordStock(String siteCode, String merchandiseCode, int inStockQuantity, String unit) {
    ImportSite site = store.findSite(siteCode).orElseThrow(() -> new BusinessException("site not found: " + siteCode));
    if (!site.trades(merchandiseCode)) {
      throw new BusinessException("site does not trade merchandise: " + merchandiseCode);
    }
    if (!store.units().contains(unit)) {
      throw new BusinessException("invalid unit: " + unit);
    }
    InventoryRecord record = new InventoryRecord(siteCode, merchandiseCode, inStockQuantity, unit);
    store.saveInventory(record);
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "RECORD_STOCK", LocalDateTime.now(), siteCode + ":" + merchandiseCode));
    return record;
  }

  public List<InventoryRecord> recordsFor(String merchandiseCode) {
    return store.inventory().stream()
        .filter(record -> record.merchandiseCode().equals(merchandiseCode))
        .toList();
  }

  public List<InventoryRecord> list() {
    return store.inventory();
  }
}
