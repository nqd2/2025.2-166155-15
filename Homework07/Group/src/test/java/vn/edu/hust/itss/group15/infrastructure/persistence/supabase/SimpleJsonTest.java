package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SimpleJsonTest {

  @Test
  void stringifyHandlesVariousTypes() {
    Map<String, Object> data = Map.of(
        "name", "Test Portal",
        "value", 42,
        "active", true,
        "items", List.of("item1", "item2")
    );
    String json = SimpleJson.stringify(data);
    
    assertTrue(json.contains("\"name\":\"Test Portal\""));
    assertTrue(json.contains("\"value\":42"));
    assertTrue(json.contains("\"active\":true"));
    assertTrue(json.contains("\"items\":[\"item1\",\"item2\"]"));
  }

  @Test
  void parseArrayOfObjectsHandlesValidJson() {
    String json = "[{\"id\":\"IR-001\",\"status\":\"SUBMITTED\"},{\"id\":\"IR-002\",\"status\":\"ALLOCATED\"}]";
    List<Map<String, Object>> result = SimpleJson.parseArrayOfObjects(json);

    assertEquals(2, result.size());
    assertEquals("IR-001", result.get(0).get("id"));
    assertEquals("SUBMITTED", result.get(0).get("status"));
    assertEquals("IR-002", result.get(1).get("id"));
    assertEquals("ALLOCATED", result.get(1).get("status"));
  }

  @Test
  void parseArrayOfObjectsHandlesNullOrBlank() {
    assertTrue(SimpleJson.parseArrayOfObjects(null).isEmpty());
    assertTrue(SimpleJson.parseArrayOfObjects("").isEmpty());
    assertTrue(SimpleJson.parseArrayOfObjects("   ").isEmpty());
  }

  @Test
  void parseArrayOfObjectsThrowsForInvalidJson() {
    assertThrows(IllegalArgumentException.class, () -> SimpleJson.parseArrayOfObjects("invalid-json"));
    assertThrows(IllegalArgumentException.class, () -> SimpleJson.parseArrayOfObjects("{\"id\":\"not-an-array\"}"));
  }
}
