package com.oms.app.tables.application.port.in.query;

import com.oms.app.tables.domain.Table;

import java.util.Objects;
import java.util.UUID;

public record TableDTO (
        UUID tableId,
        String code,
        String status,
        int capacity

){

    public static TableDTO of(Table table) {
        return new TableDTO(
                table.getId().value(),
                table.getCode().value(),
                table.getStatus().name(),
                table.getCapacity().value()
        );
    }

    public TableDTO{
        Objects.requireNonNull(tableId, "tableId must not be null");
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(status, "status must not be null");
    }
}
