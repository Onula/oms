package com.oms.app.visits.domain.vo;

import java.time.Instant;

import static com.oms.app.visits.domain.exception.VisitsDomainExceptions.*;

public record VisitTiming(
        Instant openedAt,
        Instant closedAt,
        Instant cancelledAt
) {

    public VisitTiming {
        if (openedAt == null) throw openedAtRequired();
        if (closedAt != null && cancelledAt != null) throw visitCannotBeClosedAndCancelled();
        if (closedAt != null && !closedAt.isAfter(openedAt)) throw invalidClosedAt();
        if (cancelledAt != null && !cancelledAt.isAfter(openedAt)) throw invalidCancelledAt();
    }

    public static VisitTiming open(Instant openedAt) {
        return new VisitTiming(openedAt, null, null);
    }

    public static VisitTiming of(Instant openedAt, Instant closedAt, Instant cancelledAt) {
        return new VisitTiming(openedAt, closedAt, cancelledAt);
    }

    public VisitTiming closeAt(Instant closedAt) {
        if (closedAt == null) throw closedAtRequired();
        if (!isOpen()) throw visitAlreadyEnded();
        return new VisitTiming(openedAt, closedAt, null);
    }

    public VisitTiming cancelAt(Instant cancelledAt) {
        if (cancelledAt == null) throw cancelledAtRequired();
        if (!isOpen()) throw visitAlreadyEnded();
        return new VisitTiming(openedAt, null, cancelledAt);
    }

    public boolean isOpen() {
        return closedAt == null && cancelledAt == null;
    }

    public boolean isClosed() {
        return closedAt != null;
    }

    public boolean isCancelled() {
        return cancelledAt != null;
    }
}