package com.oms.app.reservations.domain.vo;


import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.invalidCustomerName;

public record ReservationCustomerName(String value) {

    private static final int MAX_LENGTH = 100;
    private static final int MIN_LENGTH = 3;

    public ReservationCustomerName {

        String normalized = value == null ? null : value.strip();

        if (normalized == null
                || normalized.length() < MIN_LENGTH
                || normalized.length() > MAX_LENGTH) {
            throw invalidCustomerName(MIN_LENGTH, MAX_LENGTH);
        }
        value = normalized;
    }
    public static ReservationCustomerName of(String value) {
        return new ReservationCustomerName(value);
    }
}
