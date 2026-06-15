package com.oms.app.reservations.domain.vo;

import java.io.Serializable;
import java.util.UUID;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.reservationIdRequired;


public record ReservationId(UUID value) implements Serializable {

    public ReservationId {
        if (value == null) {
            throw reservationIdRequired();
        }
    }

    public static ReservationId generate() {
        return new ReservationId(UUID.randomUUID());
    }

    public static ReservationId of(UUID id) {
        return new ReservationId(id);
    }
}
