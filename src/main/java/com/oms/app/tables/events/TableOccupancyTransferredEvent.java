package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;

import java.time.Instant;
import java.util.UUID;

public record TableOccupancyTransferredEvent(
        UUID tableId,
        UUID toTableId,
        Instant occurredAt
) implements TableEvent {

    public static TableOccupancyTransferredEvent of(Table fromTable, Table toTable, Instant now) {
        return new TableOccupancyTransferredEvent(
                fromTable.getId().value(),
                toTable.getId().value(),
                now
        );
    }

}
