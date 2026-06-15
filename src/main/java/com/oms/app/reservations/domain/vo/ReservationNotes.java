package com.oms.app.reservations.domain.vo;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.invalidReservationNotes;


public record ReservationNotes(String value) {

    public static final int MAX_LENGTH = 500;

    public ReservationNotes {
        String normalized = value == null || value.isBlank() ? null : value.strip();

        if (normalized != null && normalized.length() > MAX_LENGTH) {
            throw invalidReservationNotes(MAX_LENGTH);
        }

        value = normalized;
    }

    public static ReservationNotes of(String value) {
        return new ReservationNotes(value);
    }

    public static ReservationNotes empty() {
        return new ReservationNotes(null);
    }
}
