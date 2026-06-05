package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import vn.edu.hust.itss.group15.application.port.Store;
import vn.edu.hust.itss.group15.domain.AllocationLine;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.DeliveryMeans;
import vn.edu.hust.itss.group15.domain.GoodsReceipt;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.ImportSite;
import vn.edu.hust.itss.group15.domain.InventoryRecord;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.OrderLine;
import vn.edu.hust.itss.group15.domain.OrderStatus;
import vn.edu.hust.itss.group15.domain.OverseasOrder;
import vn.edu.hust.itss.group15.domain.ReceiptLine;
import vn.edu.hust.itss.group15.domain.RequestStatus;
import vn.edu.hust.itss.group15.domain.UserAccount;
import vn.edu.hust.itss.group15.domain.UserStatus;
import vn.edu.hust.itss.group15.domain.WarehouseStock;

public class SupabaseStore implements Store {
  private static final Map<String, String> MERGE_HEADERS = Map.of("Prefer", "resolution=merge-duplicates,return=minimal");
  private static final Map<String, String> MINIMAL_HEADERS = Map.of("Prefer", "return=minimal");
  private final SupabaseTransport transport;

  public SupabaseStore(SupabaseTransport transport) {
    this.transport = transport;
  }

  public SupabaseStore(SupabaseConfig config) {
    this(new HttpSupabaseTransport(config));
  }

  @Override
  public Set<String> merchandiseCatalog() {
    return rows("merchandise_catalog", "select=merchandise_code&order=merchandise_code")
        .stream()
        .map(row -> string(row, "merchandise_code"))
        .collect(LinkedHashSet::new, Set::add, Set::addAll);
  }

  @Override
  public Set<String> units() {
    return rows("units", "select=unit_code&order=unit_code")
        .stream()
        .map(row -> string(row, "unit_code"))
        .collect(LinkedHashSet::new, Set::add, Set::addAll);
  }

  @Override
  public String nextId(String sequenceName, String prefix) {
    List<Map<String, Object>> rows = rows("app_sequences", "select=next_value&sequence_name=eq." + encode(sequenceName) + "&limit=1");
    int value = rows.isEmpty() ? 1 : integer(rows.get(0), "next_value");
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("sequence_name", sequenceName);
    row.put("next_value", value + 1);
    upsert("app_sequences", "sequence_name", row);
    return "%s-%04d".formatted(prefix, value);
  }

  @Override
  public void saveImportRequest(ImportRequest request) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("request_id", request.requestId());
    row.put("status", request.status().name());
    row.put("created_at", request.createdAt().toString());
    upsert("import_requests", "request_id", row);
    delete("import_request_items", "request_id=eq." + encode(request.requestId()));
    int lineNo = 1;
    for (ImportRequestItem item : request.items()) {
      Map<String, Object> itemRow = new LinkedHashMap<>();
      itemRow.put("request_id", request.requestId());
      itemRow.put("line_no", lineNo++);
      itemRow.put("merchandise_code", item.merchandiseCode());
      itemRow.put("quantity_ordered", item.quantityOrdered());
      itemRow.put("unit", item.unit());
      itemRow.put("desired_delivery_date", item.desiredDeliveryDate().toString());
      insert("import_request_items", itemRow);
    }
  }

  @Override
  public Optional<ImportRequest> findImportRequest(String requestId) {
    return importRequests().stream().filter(request -> request.requestId().equals(requestId)).findFirst();
  }

  @Override
  public List<ImportRequest> importRequests() {
    List<ImportRequest> requests = new ArrayList<>();
    for (Map<String, Object> row : rows("import_requests", "select=*&order=created_at.desc")) {
      String requestId = string(row, "request_id");
      requests.add(new ImportRequest(
          requestId,
          RequestStatus.valueOf(string(row, "status")),
          dateTime(row, "created_at"),
          requestItems(requestId)));
    }
    return requests;
  }

  @Override
  public void saveSite(ImportSite site) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("site_code", site.siteCode());
    row.put("site_name", site.siteName());
    row.put("delivery_days_by_ship", site.deliveryDaysByShip());
    row.put("delivery_days_by_air", site.deliveryDaysByAir());
    upsert("import_sites", "site_code", row);
    delete("site_catalog", "site_code=eq." + encode(site.siteCode()));
    for (String merchandiseCode : site.merchandiseCatalog()) {
      Map<String, Object> catalogRow = new LinkedHashMap<>();
      catalogRow.put("site_code", site.siteCode());
      catalogRow.put("merchandise_code", merchandiseCode);
      insert("site_catalog", catalogRow);
    }
  }

  @Override
  public Optional<ImportSite> findSite(String siteCode) {
    return sites().stream().filter(site -> site.siteCode().equals(siteCode)).findFirst();
  }

  @Override
  public List<ImportSite> sites() {
    List<ImportSite> sites = new ArrayList<>();
    for (Map<String, Object> row : rows("import_sites", "select=*&order=site_code")) {
      String siteCode = string(row, "site_code");
      sites.add(new ImportSite(
          siteCode,
          string(row, "site_name"),
          integer(row, "delivery_days_by_ship"),
          integer(row, "delivery_days_by_air"),
          siteCatalog(siteCode)));
    }
    return sites;
  }

  @Override
  public void saveInventory(InventoryRecord record) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("site_code", record.siteCode());
    row.put("merchandise_code", record.merchandiseCode());
    row.put("in_stock_quantity", record.inStockQuantity());
    row.put("unit", record.unit());
    upsert("inventory_records", "site_code,merchandise_code", row);
  }

  @Override
  public List<InventoryRecord> inventory() {
    List<InventoryRecord> inventory = new ArrayList<>();
    for (Map<String, Object> row : rows("inventory_records", "select=*&order=site_code,merchandise_code")) {
      inventory.add(new InventoryRecord(
          string(row, "site_code"),
          string(row, "merchandise_code"),
          integer(row, "in_stock_quantity"),
          string(row, "unit")));
    }
    return inventory;
  }

  @Override
  public void saveAllocationPlan(AllocationPlan plan) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("plan_id", plan.planId());
    row.put("request_id", plan.requestId());
    upsert("allocation_plans", "plan_id", row);
    delete("allocation_lines", "plan_id=eq." + encode(plan.planId()));
    int lineNo = 1;
    for (AllocationLine line : plan.lines()) {
      Map<String, Object> lineRow = new LinkedHashMap<>();
      lineRow.put("plan_id", plan.planId());
      lineRow.put("line_no", lineNo++);
      lineRow.put("request_id", line.requestId());
      lineRow.put("merchandise_code", line.merchandiseCode());
      lineRow.put("site_code", line.siteCode());
      lineRow.put("quantity_ordered", line.quantityOrdered());
      lineRow.put("unit", line.unit());
      lineRow.put("delivery_means", line.deliveryMeans().name());
      insert("allocation_lines", lineRow);
    }
  }

  @Override
  public Optional<AllocationPlan> findAllocationPlan(String planId) {
    return allocationPlans().stream().filter(plan -> plan.planId().equals(planId)).findFirst();
  }

  @Override
  public List<AllocationPlan> allocationPlans() {
    List<AllocationPlan> plans = new ArrayList<>();
    for (Map<String, Object> row : rows("allocation_plans", "select=*&order=plan_id.desc")) {
      String planId = string(row, "plan_id");
      plans.add(new AllocationPlan(planId, string(row, "request_id"), allocationLines(planId)));
    }
    return plans;
  }

  @Override
  public void saveOrder(OverseasOrder order) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("order_id", order.orderId());
    row.put("site_code", order.siteCode());
    row.put("status", order.status().name());
    row.put("acknowledgement_token", order.acknowledgementToken());
    upsert("overseas_orders", "order_id", row);
    delete("order_lines", "order_id=eq." + encode(order.orderId()));
    int lineNo = 1;
    for (OrderLine line : order.orderLines()) {
      Map<String, Object> lineRow = new LinkedHashMap<>();
      lineRow.put("order_id", order.orderId());
      lineRow.put("line_no", lineNo++);
      lineRow.put("merchandise_code", line.merchandiseCode());
      lineRow.put("quantity_ordered", line.quantityOrdered());
      lineRow.put("unit", line.unit());
      lineRow.put("delivery_means", line.deliveryMeans().name());
      insert("order_lines", lineRow);
    }
  }

  @Override
  public Optional<OverseasOrder> findOrder(String orderId) {
    return orders().stream().filter(order -> order.orderId().equals(orderId)).findFirst();
  }

  @Override
  public List<OverseasOrder> orders() {
    List<OverseasOrder> orders = new ArrayList<>();
    for (Map<String, Object> row : rows("overseas_orders", "select=*&order=order_id.desc")) {
      String orderId = string(row, "order_id");
      orders.add(new OverseasOrder(
          orderId,
          string(row, "site_code"),
          OrderStatus.valueOf(string(row, "status")),
          string(row, "acknowledgement_token"),
          orderLines(orderId)));
    }
    return orders;
  }

  @Override
  public void saveReceipt(GoodsReceipt receipt) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("receipt_id", receipt.receiptId());
    row.put("order_reference", receipt.orderReference());
    row.put("received_at", receipt.receivedAt().toString());
    upsert("goods_receipts", "receipt_id", row);
    delete("receipt_lines", "receipt_id=eq." + encode(receipt.receiptId()));
    int lineNo = 1;
    for (ReceiptLine line : receipt.lines()) {
      Map<String, Object> lineRow = new LinkedHashMap<>();
      lineRow.put("receipt_id", receipt.receiptId());
      lineRow.put("line_no", lineNo++);
      lineRow.put("merchandise_code", line.merchandiseCode());
      lineRow.put("ordered_quantity", line.orderedQuantity());
      lineRow.put("received_quantity", line.receivedQuantity());
      lineRow.put("discrepancy_quantity", line.discrepancyQuantity());
      lineRow.put("discrepancy_type", line.discrepancyType());
      lineRow.put("discrepancy_note", line.discrepancyNote());
      insert("receipt_lines", lineRow);
    }
  }

  @Override
  public List<GoodsReceipt> receipts() {
    List<GoodsReceipt> receipts = new ArrayList<>();
    for (Map<String, Object> row : rows("goods_receipts", "select=*&order=received_at.desc")) {
      String receiptId = string(row, "receipt_id");
      receipts.add(new GoodsReceipt(
          receiptId,
          string(row, "order_reference"),
          dateTime(row, "received_at"),
          receiptLines(receiptId)));
    }
    return receipts;
  }

  @Override
  public void addWarehouseStock(String merchandiseCode, int quantity) {
    List<Map<String, Object>> existing = rows("warehouse_stock", "select=quantity_in_stock&merchandise_code=eq." + encode(merchandiseCode) + "&limit=1");
    int current = existing.isEmpty() ? 0 : integer(existing.get(0), "quantity_in_stock");
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("merchandise_code", merchandiseCode);
    row.put("quantity_in_stock", current + quantity);
    upsert("warehouse_stock", "merchandise_code", row);
  }

  @Override
  public List<WarehouseStock> warehouseStock() {
    List<WarehouseStock> stock = new ArrayList<>();
    for (Map<String, Object> row : rows("warehouse_stock", "select=*&order=merchandise_code")) {
      stock.add(new WarehouseStock(string(row, "merchandise_code"), integer(row, "quantity_in_stock")));
    }
    return stock;
  }

  @Override
  public void saveUser(UserAccount user) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("user_id", user.userId());
    row.put("username", user.username());
    row.put("email", user.email());
    row.put("status", user.status().name());
    row.put("actor_roles", String.join(",", user.actorRoles()));
    row.put("password_hash", user.passwordHash());
    row.put("password_salt", user.passwordSalt());
    upsert("user_accounts", "user_id", row);
  }

  @Override
  public Optional<UserAccount> findUserByUsername(String username) {
    return users().stream().filter(user -> user.username().equalsIgnoreCase(username)).findFirst();
  }

  @Override
  public List<UserAccount> users() {
    List<UserAccount> users = new ArrayList<>();
    for (Map<String, Object> row : rows("user_accounts", "select=*&order=username")) {
      users.add(new UserAccount(
          string(row, "user_id"),
          string(row, "username"),
          string(row, "email"),
          UserStatus.valueOf(string(row, "status")),
          splitSet(string(row, "actor_roles")),
          string(row, "password_hash"),
          string(row, "password_salt")));
    }
    return users;
  }

  @Override
  public void appendLog(OperationLog log) {
    Map<String, Object> row = new LinkedHashMap<>();
    row.put("log_id", log.logId());
    row.put("operator_id", log.operatorId());
    row.put("action_type", log.actionType());
    row.put("timestamp", log.timestamp().toString());
    row.put("details", log.details());
    insert("operation_logs", row);
  }

  @Override
  public List<OperationLog> logs() {
    List<OperationLog> logs = new ArrayList<>();
    for (Map<String, Object> row : rows("operation_logs", "select=*&order=timestamp.desc")) {
      logs.add(new OperationLog(
          string(row, "log_id"),
          string(row, "operator_id"),
          string(row, "action_type"),
          dateTime(row, "timestamp"),
          string(row, "details")));
    }
    return logs;
  }

  private List<ImportRequestItem> requestItems(String requestId) {
    List<ImportRequestItem> items = new ArrayList<>();
    for (Map<String, Object> row : rows("import_request_items", "select=*&request_id=eq." + encode(requestId) + "&order=line_no")) {
      items.add(new ImportRequestItem(
          string(row, "merchandise_code"),
          integer(row, "quantity_ordered"),
          string(row, "unit"),
          LocalDate.parse(string(row, "desired_delivery_date"))));
    }
    return items;
  }

  private Set<String> siteCatalog(String siteCode) {
    return rows("site_catalog", "select=merchandise_code&site_code=eq." + encode(siteCode) + "&order=merchandise_code")
        .stream()
        .map(row -> string(row, "merchandise_code"))
        .collect(LinkedHashSet::new, Set::add, Set::addAll);
  }

  private List<AllocationLine> allocationLines(String planId) {
    List<AllocationLine> lines = new ArrayList<>();
    for (Map<String, Object> row : rows("allocation_lines", "select=*&plan_id=eq." + encode(planId) + "&order=line_no")) {
      lines.add(new AllocationLine(
          string(row, "request_id"),
          string(row, "merchandise_code"),
          string(row, "site_code"),
          integer(row, "quantity_ordered"),
          string(row, "unit"),
          DeliveryMeans.valueOf(string(row, "delivery_means"))));
    }
    return lines;
  }

  private List<OrderLine> orderLines(String orderId) {
    List<OrderLine> lines = new ArrayList<>();
    for (Map<String, Object> row : rows("order_lines", "select=*&order_id=eq." + encode(orderId) + "&order=line_no")) {
      lines.add(new OrderLine(
          string(row, "merchandise_code"),
          integer(row, "quantity_ordered"),
          string(row, "unit"),
          DeliveryMeans.valueOf(string(row, "delivery_means"))));
    }
    return lines;
  }

  private List<ReceiptLine> receiptLines(String receiptId) {
    List<ReceiptLine> lines = new ArrayList<>();
    for (Map<String, Object> row : rows("receipt_lines", "select=*&receipt_id=eq." + encode(receiptId) + "&order=line_no")) {
      lines.add(new ReceiptLine(
          string(row, "merchandise_code"),
          integer(row, "ordered_quantity"),
          integer(row, "received_quantity"),
          integer(row, "discrepancy_quantity"),
          string(row, "discrepancy_type"),
          string(row, "discrepancy_note")));
    }
    return lines;
  }

  private void insert(String table, Map<String, Object> row) {
    request("POST", "/" + table, SimpleJson.stringify(row), MINIMAL_HEADERS);
  }

  private void upsert(String table, String onConflict, Map<String, Object> row) {
    request("POST", "/" + table + "?on_conflict=" + encode(onConflict), SimpleJson.stringify(row), MERGE_HEADERS);
  }

  private void delete(String table, String query) {
    request("DELETE", "/" + table + "?" + query, null, MINIMAL_HEADERS);
  }

  private List<Map<String, Object>> rows(String table, String query) {
    SupabaseResponse response = request("GET", "/" + table + "?" + query, null, Map.of());
    if (response.body() == null || response.body().isBlank()) {
      return List.of();
    }
    return SimpleJson.parseArrayOfObjects(response.body());
  }

  private SupabaseResponse request(String method, String pathAndQuery, String body, Map<String, String> headers) {
    try {
      SupabaseResponse response = transport.send(method, pathAndQuery, body, headers);
      if (response.statusCode() < 200 || response.statusCode() >= 300) {
        throw new IllegalStateException("Supabase REST " + response.statusCode() + ": " + response.body());
      }
      return response;
    } catch (IOException exception) {
      throw new IllegalStateException("Supabase REST request failed", exception);
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Supabase REST request interrupted", exception);
    }
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
  }

  private String string(Map<String, Object> row, String key) {
    Object value = row.get(key);
    return value == null ? "" : String.valueOf(value);
  }

  private int integer(Map<String, Object> row, String key) {
    Object value = row.get(key);
    if (value instanceof Number number) {
      return number.intValue();
    }
    return Integer.parseInt(String.valueOf(value));
  }

  private LocalDateTime dateTime(Map<String, Object> row, String key) {
    String value = string(row, key).replace(" ", "T");
    if (value.endsWith("Z") || value.contains("+")) {
      return OffsetDateTime.parse(value).toLocalDateTime();
    }
    return LocalDateTime.parse(value);
  }

  private Set<String> splitSet(String value) {
    if (value == null || value.isBlank()) {
      return Set.of();
    }
    Set<String> values = new LinkedHashSet<>();
    for (String part : value.split(",")) {
      String trimmed = part.trim();
      if (!trimmed.isEmpty()) {
        values.add(trimmed);
      }
    }
    return values;
  }
}
