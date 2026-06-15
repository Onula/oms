package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;

import java.time.Instant;
import java.util.UUID;

public record TableOccupiedEvent(
        UUID tableId,
        Instant occurredAt
) implements TableEvent {

    public static TableOccupiedEvent of(Table table, Instant now) {
        return new TableOccupiedEvent(
                table.getId().value(),
                now
        );
    }

}
