package vn.edu.hust.itss.group15.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AllocationPolicy {
  public List<AllocationLine> allocate(
      ImportRequest request,
      List<InventoryRecord> inventory,
      Map<String, ImportSite> sites,
      LocalDate today) {
    List<AllocationLine> lines = new ArrayList<>();
    for (ImportRequestItem item : request.items()) {
      lines.addAll(allocateItem(request.requestId(), item, inventory, sites, today));
    }
    return lines;
  }

  private List<AllocationLine> allocateItem(
      String requestId,
      ImportRequestItem item,
      List<InventoryRecord> inventory,
      Map<String, ImportSite> sites,
      LocalDate today) {
    List<Candidate> candidates = inventory.stream()
        .filter(record -> record.merchandiseCode().equals(item.merchandiseCode()))
        .filter(record -> record.inStockQuantity() > 0)
        .map(record -> candidate(record, item.desiredDeliveryDate(), sites, today))
        .flatMap(List::stream)
        .sorted(Comparator
            .comparingInt((Candidate candidate) -> candidate.deliveryMeans() == DeliveryMeans.SHIP ? 0 : 1)
            .thenComparing(Comparator.comparingInt(Candidate::inStockQuantity).reversed()))
        .toList();

    if (candidates.isEmpty()) {
      throw new DomainException("no Site can meet desired delivery date for " + item.merchandiseCode());
    }
    int total = candidates.stream().mapToInt(Candidate::inStockQuantity).sum();
    if (total < item.quantityOrdered()) {
      throw new DomainException("insufficient supply for " + item.merchandiseCode());
    }

    int remaining = item.quantityOrdered();
    List<AllocationLine> result = new ArrayList<>();
    for (Candidate candidate : candidates) {
      int quantity = Math.min(remaining, candidate.inStockQuantity());
      result.add(new AllocationLine(
          requestId,
          item.merchandiseCode(),
          candidate.site().siteCode(),
          quantity,
          item.unit(),
          candidate.deliveryMeans()));
      remaining -= quantity;
      if (remaining == 0) {
        break;
      }
    }
    return result;
  }

  private List<Candidate> candidate(
      InventoryRecord record,
      LocalDate desiredDate,
      Map<String, ImportSite> sites,
      LocalDate today) {
    ImportSite site = sites.get(record.siteCode());
    if (site == null) {
      throw new DomainException("site not found: " + record.siteCode());
    }
    long daysAvailable = ChronoUnit.DAYS.between(today, desiredDate);
    List<Candidate> candidates = new ArrayList<>();
    if (site.deliveryDaysByShip() <= daysAvailable) {
      candidates.add(new Candidate(site, record.inStockQuantity(), DeliveryMeans.SHIP));
    } else if (site.deliveryDaysByAir() <= daysAvailable) {
      candidates.add(new Candidate(site, record.inStockQuantity(), DeliveryMeans.AIR));
    }
    return candidates;
  }

  private record Candidate(ImportSite site, int inStockQuantity, DeliveryMeans deliveryMeans) {}
}
