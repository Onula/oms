package com.oms.app.reservations.events;

import com.oms.app.reservations.domain.Reservation;

import java.time.Instant;
import java.util.UUID;

public record ReservationExpiredEvent(
        UUID reservationId,
        UUID tableId,
        Instant occurredAt
) implements ReservationEvent {

    public static ReservationExpiredEvent of(Reservation reservation, Instant now) {
        return new ReservationExpiredEvent(
                reservation.getId().value(),
                reservation.getTableId().value(),
                now
        );
    }
}