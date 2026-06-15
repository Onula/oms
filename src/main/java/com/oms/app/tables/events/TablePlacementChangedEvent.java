package com.oms.app.tables.events;

import com.oms.app.tables.domain.Table;

import java.time.Instant;
import java.util.UUID;

public record TablePlacementChangedEvent (
        UUID tableId,
        int x,
        int y,
        int width,
        int height,
        Instant occurredAt
) implements TableEvent {

    public static TablePlacementChangedEvent of(Table table, Instant now) {
        return new TablePlacementChangedEvent(
                table.getId().value(),
                table.getPlacement().x(),
                table.getPlacement().y(),
                table.getPlacement().width(),
                table.getPlacement().height(),
                now
        );
    }
}
