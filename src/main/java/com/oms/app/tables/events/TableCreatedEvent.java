package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;

import java.time.Instant;
import java.util.UUID;

public record TableCreatedEvent(
        UUID tableId,
        String tableCode,
        String tableStatus,
        int tableCapacity,
        int x,
        int y,
        int width,
        int height,
        Instant occurredAt
) implements TableEvent {

    public static TableCreatedEvent of(
            Table table,
            Instant now
    ) {
        return new TableCreatedEvent(
                table.getId().value(),
                table.getCode().value(),
                table.getStatus().name(),
                table.getCapacity().value(),
                table.getPlacement().x(),
                table.getPlacement().y(),
                table.getPlacement().width(),
                table.getPlacement().height(),
                now
        );
    }
}
