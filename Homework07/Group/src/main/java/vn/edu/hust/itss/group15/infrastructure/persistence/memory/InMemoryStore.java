package vn.edu.hust.itss.group15.infrastructure.persistence.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import vn.edu.hust.itss.group15.application.port.Store;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.GoodsReceipt;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportSite;
import vn.edu.hust.itss.group15.domain.InventoryRecord;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.domain.WarehouseStock;

public class InMemoryStore implements Store {
  private final Map<String, ImportRequest> importRequests = new LinkedHashMap<>();
  private final Map<String, ImportSite> sites = new LinkedHashMap<>();
  private final Map<String, InventoryRecord> inventory = new LinkedHashMap<>();
  private final Map<String, AllocationPlan> allocationPlans = new LinkedHashMap<>();
  private final Map<String, OverseasOrder> orders = new LinkedHashMap<>();
  private final Map<String, GoodsReceipt> receipts = new LinkedHashMap<>();
  private final Map<String, WarehouseStock> warehouseStock = new LinkedHashMap<>();
  private final Map<String, UserAccount> users = new LinkedHashMap<>();
  private final List<OperationLog> logs = new ArrayList<>();
  private final Map<String, AtomicInteger> sequences = new ConcurrentHashMap<>();
  private final Set<String> merchandiseCatalog = new java.util.LinkedHashSet<>(java.util.List.of("MH-001", "MH-002", "MH-003", "MH-004", "MH-005", "MH-014"));
  private final Set<String> units = Set.of("box", "piece", "kg");

  public InMemoryStore() {
    saveSite(new ImportSite("SITE-SEA-01", "Singapore Sea Site", 18, 5, Set.of("MH-001", "MH-002", "MH-005")));
    saveSite(new ImportSite("SITE-EU-04", "Europe Import Site", 28, 7, Set.of("MH-001", "MH-003", "MH-005", "MH-014")));
    saveSite(new ImportSite("SITE-AIR-02", "Air Express Site", 35, 4, Set.of("MH-002", "MH-004", "MH-014")));
  }

  @Override
  public Set<String> merchandiseCatalog() {
    return merchandiseCatalog;
  }

  @Override
  public Set<String> units() {
    return units;
  }

  @Override
  public String nextId(String sequenceName, String prefix) {
    AtomicInteger sequence = sequences.computeIfAbsent(sequenceName, ignored -> new AtomicInteger(1));
    return "%s-%04d".formatted(prefix, sequence.getAndIncrement());
  }

  @Override
  public void saveImportRequest(ImportRequest request) {
    importRequests.put(request.requestId(), request);
  }

  @Override
  public Optional<ImportRequest> findImportRequest(String requestId) {
    return Optional.ofNullable(importRequests.get(requestId));
  }

  @Override
  public List<ImportRequest> importRequests() {
    return List.copyOf(importRequests.values());
  }

  @Override
  public void saveSite(ImportSite site) {
    sites.put(site.siteCode(), site);
  }

  @Override
  public Optional<ImportSite> findSite(String siteCode) {
    return Optional.ofNullable(sites.get(siteCode));
  }

  @Override
  public List<ImportSite> sites() {
    return List.copyOf(sites.values());
  }

  @Override
  public void saveInventory(InventoryRecord record) {
    inventory.put(record.siteCode() + "::" + record.merchandiseCode(), record);
  }

  @Override
  public List<InventoryRecord> inventory() {
    return List.copyOf(inventory.values());
  }

  @Override
  public void saveAllocationPlan(AllocationPlan plan) {
    allocationPlans.put(plan.planId(), plan);
  }

  @Override
  public Optional<AllocationPlan> findAllocationPlan(String planId) {
    return Optional.ofNullable(allocationPlans.get(planId));
  }

  @Override
  public List<AllocationPlan> allocationPlans() {
    return List.copyOf(allocationPlans.values());
  }

  @Override
  public void saveOrder(OverseasOrder order) {
    orders.put(order.orderId(), order);
  }

  @Override
  public Optional<OverseasOrder> findOrder(String orderId) {
    return Optional.ofNullable(orders.get(orderId));
  }

  @Override
  public List<OverseasOrder> orders() {
    return List.copyOf(orders.values());
  }

  @Override
  public void saveReceipt(GoodsReceipt receipt) {
    receipts.put(receipt.receiptId(), receipt);
  }

  @Override
  public List<GoodsReceipt> receipts() {
    return List.copyOf(receipts.values());
  }

  @Override
  public void addWarehouseStock(String merchandiseCode, int quantity) {
    warehouseStock.merge(merchandiseCode, new WarehouseStock(merchandiseCode, quantity), (oldStock, newStock) -> oldStock.add(quantity));
  }

  @Override
  public List<WarehouseStock> warehouseStock() {
    return List.copyOf(warehouseStock.values());
  }

  @Override
  public void saveUser(UserAccount user) {
    users.put(user.userId(), user);
  }

  @Override
  public Optional<UserAccount> findUserByUsername(String username) {
    return users.values().stream()
        .filter(user -> user.username().equalsIgnoreCase(username))
        .findFirst();
  }

  @Override
  public List<UserAccount> users() {
    return List.copyOf(users.values());
  }

  @Override
  public void appendLog(OperationLog log) {
    logs.add(log);
  }

  @Override
  public void saveMerchandise(String merchandiseCode) {
    merchandiseCatalog.add(merchandiseCode);
  }

  @Override
  public List<OperationLog> logs() {
    return List.copyOf(logs);
  }
}
