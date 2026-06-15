package com.oms.app.reservations.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record ReservationTable(
        ReservationTableId id,
        boolean isAvailable,
        String statusString,
        int capacity
) {

    public ReservationTable {
        Objects.requireNonNull(id, "Reservation table id must not be null");
        Objects.requireNonNull(statusString, "Reservation table status string must not be null");

        if (capacity <= 0) {
            throw  new IllegalArgumentException("Reservation table capacity must be positive");
        }
    }

    public static ReservationTable of(UUID tableId, boolean isAvailable, String statusString, int capacity) {
        return new ReservationTable(ReservationTableId.of(tableId), isAvailable, statusString, capacity);
    }

}
