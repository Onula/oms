package com.oms.app.reservations.events;

import com.oms.app.reservations.domain.Reservation;

import java.time.Instant;
import java.util.UUID;

public record ReservationWalkInBlockStartedEvent(
        UUID reservationId,
        UUID tableId,
        Instant occurredAt
) implements ReservationEvent {

    public static ReservationWalkInBlockStartedEvent of(Reservation reservation, Instant now) {
        return new ReservationWalkInBlockStartedEvent(
                reservation.getId().value(),
                reservation.getTableId().value(),
                now
        );
    }
}
