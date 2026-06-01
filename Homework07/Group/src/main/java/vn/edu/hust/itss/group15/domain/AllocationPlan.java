package vn.edu.hust.itss.group15.domain;

import java.util.List;

public record AllocationPlan(String planId, String requestId, List<AllocationLine> lines) {
  public AllocationPlan {
    if (planId == null || planId.isBlank()) {
      throw new IllegalArgumentException("planId is required");
    }
    if (requestId == null || requestId.isBlank()) {
      throw new IllegalArgumentException("requestId is required");
    }
    if (lines == null || lines.isEmpty()) {
      throw new IllegalArgumentException("allocation lines are required");
    }
    lines = List.copyOf(lines);
  }
}
