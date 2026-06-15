package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;

import java.time.Instant;
import java.util.UUID;

public record TableReleasedEvent(
        UUID tableId,
        Instant occurredAt
) implements TableEvent {

    public static TableReleasedEvent of(Table table, Instant occurredAt) {
        return new TableReleasedEvent(
                table.getId().value(),
                occurredAt
        );
    }
}
