package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import java.io.IOException;
import java.util.Map;

public interface SupabaseTransport {
  SupabaseResponse send(String method, String pathAndQuery, String body, Map<String, String> extraHeaders)
      throws IOException, InterruptedException;
}
