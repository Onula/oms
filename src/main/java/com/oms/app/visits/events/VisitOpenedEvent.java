package com.oms.app.visits.events;

import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.domain.VisitType;

import java.time.Instant;
import java.util.UUID;

public record VisitOpenedEvent(
        UUID visitId,
        UUID tableId,
        String type,
        Instant openedAt,
        Instant occurredAt
) implements VisitEvent {

    public static VisitOpenedEvent of(Visit visit, Instant occurredAt) {
        return new VisitOpenedEvent(
                visit.getId().value(),
                visit.getTableId().value(),
                visit.getType().name(),
                visit.getTiming().openedAt(),
                occurredAt
        );
    }
}
