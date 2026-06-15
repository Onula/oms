package com.oms.app.visits.application.port.in.query;

import com.oms.app.visits.domain.Visit;

import java.time.Instant;
import java.util.UUID;

public record VisitDTO(
        UUID visitId,
        UUID tableId,
        String type,
        String status,
        Instant openedAt,
        Instant closedAt,
        Instant cancelledAt
) {
    public static VisitDTO from(Visit visit) {
        return new VisitDTO(
                visit.getId().value(),
                visit.getTableId().value(),
                visit.getType().name(),
                visit.getStatus().name(),
                visit.getTiming().openedAt(),
                visit.getTiming().closedAt(),
                visit.getTiming().cancelledAt()
        );
    }
}
