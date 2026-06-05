package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import vn.edu.hust.itss.group15.application.BusinessException;

public record SupabaseConfig(String restUrl, String apiKey) {
  public static SupabaseConfig fromEnvironment() {
    String url = Dotenv.get("IMPORT_SUPABASE_URL");
    String key = Dotenv.get("IMPORT_SUPABASE_KEY");
    if (isBlank(url) || isBlank(key)) {
      throw new BusinessException("Set IMPORT_SUPABASE_URL and IMPORT_SUPABASE_KEY in a .env file or environment before starting the app");
    }
    String normalized = url.endsWith("/rest/v1") ? url : url.replaceAll("/+$", "") + "/rest/v1";
    return new SupabaseConfig(normalized, key);
  }

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }
}
