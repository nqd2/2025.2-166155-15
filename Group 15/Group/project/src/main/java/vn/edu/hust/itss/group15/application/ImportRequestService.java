package vn.edu.hust.itss.group15.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportRequestItem;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.RequestStatus;
import vn.edu.hust.itss.group15.application.port.Store;

public class ImportRequestService {
  private final Store store;
  public ImportRequestService(Store store) {
    this.store = store;
  }

  public ImportRequest create(List<ImportRequestItem> items) {
    validateItems(items);
    String requestId = store.nextId("import_requests", "IR");
    ImportRequest request = new ImportRequest(requestId, RequestStatus.SUBMITTED, LocalDateTime.now(), items);
    store.saveImportRequest(request);
    log("CREATE_IMPORT_REQUEST", requestId);
    return request;
  }

  public ImportRequest update(String requestId, List<ImportRequestItem> items) {
    validateItems(items);
    ImportRequest current = find(requestId);
    if (current.status() == RequestStatus.CANCELLED || current.status() == RequestStatus.ALLOCATED) {
      throw new BusinessException("request cannot be updated in status " + current.status());
    }
    ImportRequest updated = current.withItems(items);
    store.saveImportRequest(updated);
    log("UPDATE_IMPORT_REQUEST", requestId);
    return updated;
  }

  public ImportRequest cancel(String requestId) {
    ImportRequest cancelled = find(requestId).withStatus(RequestStatus.CANCELLED);
    store.saveImportRequest(cancelled);
    log("CANCEL_IMPORT_REQUEST", requestId);
    return cancelled;
  }

  public ImportRequest find(String requestId) {
    return store.findImportRequest(requestId).orElseThrow(() -> new BusinessException("import request not found: " + requestId));
  }

  public List<ImportRequest> list() {
    return store.importRequests();
  }

  private void validateItems(List<ImportRequestItem> items) {
    if (items == null || items.isEmpty()) {
      throw new BusinessException("import request must contain at least one item");
    }
    for (ImportRequestItem item : items) {
      if (!store.merchandiseCatalog().contains(item.merchandiseCode())) {
        throw new BusinessException("unknown merchandiseCode: " + item.merchandiseCode());
      }
      if (!store.units().contains(item.unit())) {
        throw new BusinessException("invalid unit: " + item.unit());
      }
      if (!item.desiredDeliveryDate().isAfter(LocalDate.now())) {
        throw new BusinessException("desiredDeliveryDate must be in the future");
      }
    }
  }

  private void log(String actionType, String details) {
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", actionType, LocalDateTime.now(), details));
  }
}
