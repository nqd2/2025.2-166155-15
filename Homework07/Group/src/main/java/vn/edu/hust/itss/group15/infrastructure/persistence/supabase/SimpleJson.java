package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class SimpleJson {
  private static final Gson gson = new GsonBuilder()
      .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
      .create();
      
  private static final Type listMapType = new TypeToken<List<Map<String, Object>>>() {}.getType();

  private SimpleJson() {}

  static String stringify(Object value) {
    return gson.toJson(value);
  }

  static List<Map<String, Object>> parseArrayOfObjects(String json) {
    if (json == null || json.isBlank()) {
      return new ArrayList<>();
    }
    try {
      List<Map<String, Object>> result = gson.fromJson(json, listMapType);
      return result != null ? result : new ArrayList<>();
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to parse JSON array: " + e.getMessage(), e);
    }
  }
}
