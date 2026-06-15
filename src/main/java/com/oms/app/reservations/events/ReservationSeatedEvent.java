package com.oms.app.reservations.events;

import com.oms.app.reservations.domain.Reservation;

import java.time.Instant;
import java.util.UUID;

public record ReservationSeatedEvent(
        UUID reservationId,
        UUID tableId,
        UUID visitId,
        Instant occurredAt
) implements ReservationEvent {

    public static ReservationSeatedEvent of(Reservation reservation, Instant now) {
        return new ReservationSeatedEvent(
                reservation.getId().value(),
                reservation.getTableId().value(),
                reservation.getVisitId().value(),
                now
        );
    }
}
