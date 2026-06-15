package com.oms.app.reservations.events;

import com.oms.app.reservations.domain.Reservation;

import java.time.Instant;
import java.util.UUID;

public record ReservationCreatedEvent(
        UUID reservationId,
        UUID tableId,
        String customerName,
        int partySize,
        Instant startTime,
        Instant endTime,
        Instant expirationTime,
        Instant walkInBlockTime,
        Instant occurredAt
) implements ReservationEvent {

    public static ReservationCreatedEvent of(Reservation reservation, Instant now) {
        return new ReservationCreatedEvent(
                reservation.getId().value(),
                reservation.getTableId().value(),
                reservation.getCustomer().name().value(),
                reservation.getPartySize().value(),
                reservation.getTiming().startTime(),
                reservation.getTiming().endTime(),
                reservation.getTiming().expirationTime(),
                reservation.getTiming().walkInBlockTime(),
                now
        );
    }
}


