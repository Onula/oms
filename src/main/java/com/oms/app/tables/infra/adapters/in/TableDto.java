package com.oms.app.tables.infra.adapters.in;

public record TableDto(
    String id,
    boolean isAvailable,
    String statusString,
    int capacity
) {
}