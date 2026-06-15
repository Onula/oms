package com.oms.app.visits.domain;

import com.oms.app.visits.domain.vo.VisitId;
import com.oms.app.visits.domain.vo.VisitTableId;
import com.oms.app.visits.domain.vo.VisitTiming;
import lombok.Getter;

import java.time.Instant;

import static com.oms.app.visits.domain.exception.VisitsDomainExceptions.*;

@Getter
public class Visit {

    private final VisitId id;
    private VisitTableId tableId;
    private final VisitType type;
    private VisitStatus status;
    private VisitTiming timing;

    private Visit(
            VisitId id,
            VisitTableId tableId,
            VisitType type,
            VisitStatus status,
            VisitTiming timing
    ) {
        if (id == null) throw visitIdRequired();
        if (tableId == null) throw visitTableIdRequired();
        if (type == null) throw visitTypeRequired();
        if (status == null) throw visitStatusRequired();
        if (timing == null) throw visitTimingRequired();

        validateStatusMatchesTiming(status, timing);

        this.id = id;
        this.tableId = tableId;
        this.type = type;
        this.status = status;
        this.timing = timing;
    }

    public static Visit open(
            VisitTableId tableId,
            VisitType type,
            Instant now
    ) {
        return new Visit(
                VisitId.generate(),
                tableId,
                type,
                VisitStatus.ACTIVE,
                VisitTiming.open(now)
        );
    }

    public static Visit reconstitute(
            VisitId id,
            VisitTableId tableId,
            VisitType type,
            VisitStatus status,
            Instant openedAt,
            Instant closedAt,
            Instant cancelledAt
    ) {
        return new Visit(
                id,
                tableId,
                type,
                status,
                VisitTiming.of(openedAt, closedAt, cancelledAt)
        );
    }

    public void close(Instant now) {
        transitionTo(VisitStatus.CLOSED);
        this.timing = this.timing.closeAt(now);
    }

    public void cancel(Instant now) {
        transitionTo(VisitStatus.CANCELLED);
        this.timing = this.timing.cancelAt(now);
    }

    public void transferTo(VisitTableId targetTableId) {
        if (targetTableId == null) throw targetTableIdRequired();
        if (!isActive()) throw visitNotActive(id);
        if (this.tableId.equals(targetTableId)) throw cannotTransferVisitToSameTable(targetTableId.value());

        this.tableId = targetTableId;
    }

    public boolean isActive() {
        return status == VisitStatus.ACTIVE;
    }

    private void transitionTo(VisitStatus next) {
        if (next == null) throw visitStatusRequired();
        if (!status.canTransitionTo(next)) throw statusTransitionNotAllowed(id, status, next);

        this.status = next;
    }

    private static void validateStatusMatchesTiming(VisitStatus status, VisitTiming timing) {
        switch (status) {
            case ACTIVE -> {
                if (!timing.isOpen()) throw activeVisitCannotHaveEndTime();
            }
            case CLOSED -> {
                if (!timing.isClosed()) throw closedVisitMustHaveClosedAt();
            }
            case CANCELLED -> {
                if (!timing.isCancelled()) throw cancelledVisitMustHaveCancelledAt();
            }
        }
    }
}