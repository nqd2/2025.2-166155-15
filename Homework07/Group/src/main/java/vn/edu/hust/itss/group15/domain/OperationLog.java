package vn.edu.hust.itss.group15.domain;

import java.time.LocalDateTime;

public record OperationLog(String logId, String operatorId, String actionType, LocalDateTime timestamp, String details) {}
