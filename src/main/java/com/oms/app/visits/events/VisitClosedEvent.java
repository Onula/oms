package com.oms.app.visits.events;
import com.oms.app.visits.domain.Visit;

import java.time.Instant;
import java.util.UUID;

public record VisitClosedEvent(
        UUID visitId,
        UUID tableId,
        Instant closedAt,
        Instant occurredAt
) implements VisitEvent {

    public static VisitClosedEvent of(Visit visit, Instant occurredAt) {
        return new VisitClosedEvent(
                visit.getId().value(),
                visit.getTableId().value(),
                visit.getTiming().closedAt(),
                occurredAt
        );
    }
}
