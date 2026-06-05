package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.InventoryRecord;

class SupabaseStoreTest {
  @Test
  void nextIdReadsAndUpsertsSequenceOverRest() {
    FakeTransport transport = new FakeTransport();
    transport.responses.add(new SupabaseResponse(200, "[{\"next_value\":7}]"));
    transport.responses.add(new SupabaseResponse(201, ""));

    String id = new SupabaseStore(transport).nextId("import_requests", "IR");

    assertEquals("IR-0007", id);
    assertEquals("GET", transport.requests.get(0).method());
    assertEquals("/app_sequences?select=next_value&sequence_name=eq.import_requests&limit=1", transport.requests.get(0).path());
    assertEquals("POST", transport.requests.get(1).method());
    assertEquals("/app_sequences?on_conflict=sequence_name", transport.requests.get(1).path());
    assertTrue(transport.requests.get(1).body().contains("\"next_value\":8"));
  }

  @Test
  void saveInventoryUsesSupabaseCompositeUpsert() {
    FakeTransport transport = new FakeTransport();
    transport.responses.add(new SupabaseResponse(201, ""));

    new SupabaseStore(transport).saveInventory(new InventoryRecord("SITE-SEA-01", "MH-001", 12, "box"));

    Request request = transport.requests.get(0);
    assertEquals("POST", request.method());
    assertEquals("/inventory_records?on_conflict=site_code%2Cmerchandise_code", request.path());
    assertEquals("resolution=merge-duplicates,return=minimal", request.headers().get("Prefer"));
    assertTrue(request.body().contains("\"site_code\":\"SITE-SEA-01\""));
    assertTrue(request.body().contains("\"in_stock_quantity\":12"));
  }

  @Test
  void importRequestsHydratesChildItems() {
    FakeTransport transport = new FakeTransport();
    transport.responses.add(new SupabaseResponse(200,
        "[{\"request_id\":\"IR-0001\",\"status\":\"SUBMITTED\",\"created_at\":\"2026-06-01T08:00:00\"}]"));
    transport.responses.add(new SupabaseResponse(200,
        "[{\"merchandise_code\":\"MH-001\",\"quantity_ordered\":10,\"unit\":\"box\",\"desired_delivery_date\":\"2026-06-30\"}]"));

    List<ImportRequest> requests = new SupabaseStore(transport).importRequests();

    assertEquals(1, requests.size());
    assertEquals("IR-0001", requests.get(0).requestId());
    assertEquals("MH-001", requests.get(0).items().get(0).merchandiseCode());
    assertEquals("/import_request_items?select=*&request_id=eq.IR-0001&order=line_no", transport.requests.get(1).path());
  }

  private static class FakeTransport implements SupabaseTransport {
    private final ArrayDeque<SupabaseResponse> responses = new ArrayDeque<>();
    private final List<Request> requests = new ArrayList<>();

    @Override
    public SupabaseResponse send(String method, String pathAndQuery, String body, Map<String, String> extraHeaders) {
      requests.add(new Request(method, pathAndQuery, body == null ? "" : body, extraHeaders));
      return responses.removeFirst();
    }
  }

  private record Request(String method, String path, String body, Map<String, String> headers) {}
}
