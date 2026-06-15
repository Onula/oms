package com.oms.app.ui.tables_board.application.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record TablesBoardCurrentReservationInfo(
        UUID reservationId,
        Instant reservationStartTime,
        String customerName,
        int partySize,
        Set<String> attentions
) {
    public TablesBoardCurrentReservationInfo {
        Objects.requireNonNull(reservationId, "reservation id must not be null");
        Objects.requireNonNull(reservationStartTime, "reservation start time must not be null");
        Objects.requireNonNull(customerName, "customer name must not be null");

        attentions = attentions == null ? Set.of() : Set.copyOf(attentions);
    }

    public static TablesBoardCurrentReservationInfo of(
            UUID reservationId,
            Instant reservationStartTime,
            String customerName,
            int partySize
    ) {
        return new TablesBoardCurrentReservationInfo(
                reservationId,
                reservationStartTime,
                customerName,
                partySize,
                Set.of()
        );
    }

    public static TablesBoardCurrentReservationInfo of(
            UUID reservationId,
            Instant reservationStartTime,
            String customerName,
            int partySize,
            Set<String> attentions
    ) {
        return new TablesBoardCurrentReservationInfo(
                reservationId,
                reservationStartTime,
                customerName,
                partySize,
                attentions
        );
    }

    public TablesBoardCurrentReservationInfo addAttention(String attention) {
        Objects.requireNonNull(attention, "attention must not be null");

        Set<String> updated = new HashSet<>(attentions);
        updated.add(attention);

        return new TablesBoardCurrentReservationInfo(
                reservationId,
                reservationStartTime,
                customerName,
                partySize,
                updated
        );
    }

    public TablesBoardCurrentReservationInfo removeAttention(String attention) {
        Objects.requireNonNull(attention, "attention must not be null");

        Set<String> updated = new HashSet<>(attentions);
        updated.remove(attention);

        return new TablesBoardCurrentReservationInfo(
                reservationId,
                reservationStartTime,
                customerName,
                partySize,
                updated
        );
    }

    public boolean hasAttentions() {
        return !attentions.isEmpty();
    }
}