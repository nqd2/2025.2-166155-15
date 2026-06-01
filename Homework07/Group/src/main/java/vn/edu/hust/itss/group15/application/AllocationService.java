package vn.edu.hust.itss.group15.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import vn.edu.hust.itss.group15.application.port.Store;
import vn.edu.hust.itss.group15.domain.AllocationLine;
import vn.edu.hust.itss.group15.domain.AllocationPlan;
import vn.edu.hust.itss.group15.domain.AllocationPolicy;
import vn.edu.hust.itss.group15.domain.DomainException;
import vn.edu.hust.itss.group15.domain.ImportRequest;
import vn.edu.hust.itss.group15.domain.ImportSite;
import vn.edu.hust.itss.group15.domain.OperationLog;
import vn.edu.hust.itss.group15.domain.RequestStatus;

public class AllocationService {
  private final Store store;
  private final AllocationPolicy allocationPolicy = new AllocationPolicy();

  public AllocationService(Store store) {
    this.store = store;
  }

  public AllocationPlan plan(String requestId) {
    ImportRequest request = requestForAllocation(requestId);
    List<AllocationLine> lines = linesForRequest(request);
    AllocationPlan plan = new AllocationPlan(store.nextId("allocation_plans", "AP"), request.requestId(), lines);
    store.saveAllocationPlan(plan);
    store.saveImportRequest(request.withStatus(RequestStatus.ALLOCATED));
    store.appendLog(new OperationLog(store.nextId("operation_logs", "LOG"), "system", "PLAN_ALLOCATION", LocalDateTime.now(), plan.planId()));
    return plan;
  }

  public AllocationPlan preview(String requestId) {
    ImportRequest request = requestForAllocation(requestId);
    return new AllocationPlan("PREVIEW", request.requestId(), linesForRequest(request));
  }

  public List<AllocationPlan> list() {
    return store.allocationPlans();
  }

  private ImportRequest requestForAllocation(String requestId) {
    ImportRequest request = store.findImportRequest(requestId)
        .orElseThrow(() -> new BusinessException("import request not found: " + requestId));
    if (request.status() == RequestStatus.CANCELLED) {
      throw new BusinessException("cancelled request cannot be allocated");
    }
    return request;
  }

  private List<AllocationLine> linesForRequest(ImportRequest request) {
    Map<String, ImportSite> sitesByCode = store.sites().stream()
        .collect(Collectors.toMap(ImportSite::siteCode, site -> site));
    try {
      return allocationPolicy.allocate(request, store.inventory(), sitesByCode, LocalDate.now());
    } catch (DomainException exception) {
      throw new BusinessException(exception.getMessage());
    }
  }
}
