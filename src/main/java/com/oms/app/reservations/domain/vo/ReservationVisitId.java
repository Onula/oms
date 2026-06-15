package com.oms.app.reservations.domain.vo;

import java.util.UUID;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.reservationVisitIdRequired;

public record ReservationVisitId(UUID value) {

    public ReservationVisitId {
    }

    public static ReservationVisitId of(UUID value) {
        return new ReservationVisitId(value);
    }

    public static void ensureNoNull(ReservationVisitId visitId) {
        if (visitId == null || visitId.value() == null) {
            throw reservationVisitIdRequired();
        }
    }
}
