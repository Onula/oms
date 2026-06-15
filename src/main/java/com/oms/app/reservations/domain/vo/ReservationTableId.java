package com.oms.app.reservations.domain.vo;

import java.util.UUID;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.reservationTableIdRequired;

public record ReservationTableId(UUID value) {

    public ReservationTableId {
        if (value == null) {
            throw reservationTableIdRequired();
        }
    }

    public static ReservationTableId of(UUID value) {
        return new ReservationTableId(value);
    }
}
