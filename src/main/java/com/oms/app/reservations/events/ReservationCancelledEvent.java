package com.oms.app.reservations.events;

import com.oms.app.reservations.domain.Reservation;

import java.time.Instant;
import java.util.UUID;

public record ReservationCancelledEvent(
        UUID reservationId,
        UUID tableId,
        Instant occurredAt
) implements ReservationEvent {


    public static ReservationCancelledEvent of(Reservation reservation,Instant now) {
        return new ReservationCancelledEvent(
                reservation.getId().value(),
                reservation.getTableId().value(),
                now
        );
    }
}
