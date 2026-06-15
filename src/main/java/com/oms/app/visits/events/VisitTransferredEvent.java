package com.oms.app.visits.events;

import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.domain.vo.VisitTableId;

import java.time.Instant;
import java.util.UUID;

public record VisitTransferredEvent(
        UUID visitId,
        UUID fromTableId,
        UUID toTableId,
        Instant occurredAt
) implements VisitEvent {

    public static VisitTransferredEvent of(
            Visit visit,
            VisitTableId fromTableId,
            VisitTableId toTableId,
            Instant now
    ) {
        return new VisitTransferredEvent(
                visit.getId().value(),
                fromTableId.value(),
                toTableId.value(),
                now
        );
    }
}
