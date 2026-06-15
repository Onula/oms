package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;

import java.time.Instant;
import java.util.UUID;

public record TableRemovedFromLayoutEvent(
        UUID tableId,
        Instant occurredAt
) implements TableEvent{

    public static TableRemovedFromLayoutEvent of(Table table, Instant now) {
        return new TableRemovedFromLayoutEvent(
                table.getId().value(),
                now
        );
    }

}
