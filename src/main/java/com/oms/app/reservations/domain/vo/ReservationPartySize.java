package com.oms.app.reservations.domain.vo;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.invalidPartySize;


public record ReservationPartySize(int value) {

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 25;

    public ReservationPartySize {

        if (value < MIN_VALUE || value > MAX_VALUE ) {
            throw invalidPartySize(MIN_VALUE, MAX_VALUE);
        }

    }

    public static ReservationPartySize of(int value) {
        return new ReservationPartySize(value);
    }
}
