package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.vo.TableCapacity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TableCapacityChangedEvent(
        UUID tableId,
        int newCapacity,
        int oldCapacity,
        Instant occurredAt
) implements TableEvent {

    public TableCapacityChangedEvent {
        Objects.requireNonNull(tableId, "tableId cannot be null");
        Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
    }

    public static TableCapacityChangedEvent of(Table table, TableCapacity oldCapacity, Instant now) {
        return new TableCapacityChangedEvent(
                table.getId().value(),
                table.getCapacity().value(),
                oldCapacity.value(),
                now
        );
    }
}
