package vn.edu.hust.itss.group15.domain;

import java.time.LocalDateTime;
import java.util.List;

public record ImportRequest(
    String requestId,
    RequestStatus status,
    LocalDateTime createdAt,
    List<ImportRequestItem> items) {

  public ImportRequest {
    if (requestId == null || requestId.isBlank()) {
      throw new IllegalArgumentException("requestId is required");
    }
    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("items are required");
    }
    items = List.copyOf(items);
  }

  public ImportRequest withStatus(RequestStatus newStatus) {
    return new ImportRequest(requestId, newStatus, createdAt, items);
  }

  public ImportRequest withItems(List<ImportRequestItem> newItems) {
    return new ImportRequest(requestId, RequestStatus.UPDATED, createdAt, newItems);
  }
}
