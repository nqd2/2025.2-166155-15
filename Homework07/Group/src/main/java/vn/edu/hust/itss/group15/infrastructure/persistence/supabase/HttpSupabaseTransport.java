package vn.edu.hust.itss.group15.infrastructure.persistence.supabase;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class HttpSupabaseTransport implements SupabaseTransport {
  private final SupabaseConfig config;
  private final HttpClient httpClient = HttpClient.newHttpClient();

  public HttpSupabaseTransport(SupabaseConfig config) {
    this.config = config;
  }

  @Override
  public SupabaseResponse send(String method, String pathAndQuery, String body, Map<String, String> extraHeaders)
      throws IOException, InterruptedException {
    HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(config.restUrl() + pathAndQuery))
        .header("apikey", config.apiKey())
        .header("Authorization", "Bearer " + config.apiKey())
        .header("Accept", "application/json")
        .header("Content-Type", "application/json");
    extraHeaders.forEach(builder::header);
    HttpRequest.BodyPublisher publisher = body == null
        ? HttpRequest.BodyPublishers.noBody()
        : HttpRequest.BodyPublishers.ofString(body);
    HttpRequest request = builder.method(method, publisher).build();
    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return new SupabaseResponse(response.statusCode(), response.body());
  }
}
