package vn.edu.hust.itss.group15.application.port;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.GoodsReceipt;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportSite;
import vn.edu.hust.itss.group15.domain.InventoryRecord;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.domain.WarehouseStock;

public interface Store {
  Set<String> merchandiseCatalog();

  void saveMerchandise(String merchandiseCode);

  Set<String> units();

  String nextId(String sequenceName, String prefix);

  void saveImportRequest(ImportRequest request);

  Optional<ImportRequest> findImportRequest(String requestId);

  List<ImportRequest> importRequests();

  void saveSite(ImportSite site);

  Optional<ImportSite> findSite(String siteCode);

  List<ImportSite> sites();

  void saveInventory(InventoryRecord record);

  List<InventoryRecord> inventory();

  void saveAllocationPlan(AllocationPlan plan);

  Optional<AllocationPlan> findAllocationPlan(String planId);

  List<AllocationPlan> allocationPlans();

  void saveOrder(OverseasOrder order);

  Optional<OverseasOrder> findOrder(String orderId);

  List<OverseasOrder> orders();

  void saveReceipt(GoodsReceipt receipt);

  List<GoodsReceipt> receipts();

  void addWarehouseStock(String merchandiseCode, int quantity);

  List<WarehouseStock> warehouseStock();

  void saveUser(UserAccount user);

  Optional<UserAccount> findUserByUsername(String username);

  List<UserAccount> users();

  void appendLog(OperationLog log);

  List<OperationLog> logs();
}
