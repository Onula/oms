package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TableMarkedOutOfServiceEvent(
        UUID tableId,
        Instant occurredAt
) implements TableEvent {

    public TableMarkedOutOfServiceEvent {
        Objects.requireNonNull(tableId, "tableId cannot be null");
        Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
    }

    public static TableMarkedOutOfServiceEvent of(Table table, Instant now) {
        return new TableMarkedOutOfServiceEvent(
                table.getId().value(),
                now
        );
    }
}
