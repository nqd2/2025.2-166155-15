package vn.edu.hust.itss.group15.domain;

import java.time.LocalDateTime;
import java.util.List;

public record GoodsReceipt(String receiptId, String orderReference, LocalDateTime receivedAt, List<ReceiptLine> lines) {
  public GoodsReceipt {
    if (receiptId == null || receiptId.isBlank()) {
      throw new IllegalArgumentException("receiptId is required");
    }
    if (orderReference == null || orderReference.isBlank()) {
      throw new IllegalArgumentException("orderReference is required");
    }
    if (lines == null || lines.isEmpty()) {
      throw new IllegalArgumentException("receiptLines are required");
    }
    lines = List.copyOf(lines);
  }

  public boolean hasDiscrepancy() {
    return lines.stream().anyMatch(ReceiptLine::hasDiscrepancy);
  }
}
