package com.oms.app.reservations.domain.vo;


import java.util.Objects;

public record ReservationCustomer(
        ReservationCustomerName name,
        ReservationCustomerPhone phone
) {

    public ReservationCustomer {
        Objects.requireNonNull(name, "Reservation customer name must not be null");
        Objects.requireNonNull(phone, "Reservation customer phone must not be null");
    }

    public static ReservationCustomer of(ReservationCustomerName name, ReservationCustomerPhone phone) {
        return new ReservationCustomer(name, phone);
    }
}
