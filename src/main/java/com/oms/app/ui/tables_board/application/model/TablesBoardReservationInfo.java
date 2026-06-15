package com.oms.app.ui.tables_board.application.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record TablesBoardReservationInfo(
        UUID reservationId,
        Instant reservationStartTime,
        Instant reservationEndTime,
        String customerName,
        int partySize,
        boolean walkInBlocked,
        Set<String> attentions
) {

    public TablesBoardReservationInfo {
        Objects.requireNonNull(reservationId, "reservation id must not be null");
        Objects.requireNonNull(reservationStartTime, "reservation start time must not be null");
        Objects.requireNonNull(reservationEndTime, "reservation end time must not be null");
        Objects.requireNonNull(customerName, "customer name must not be null");

        attentions = attentions == null ? Set.of() : Set.copyOf(attentions);
    }

    public static TablesBoardReservationInfo of(
            UUID reservationId,
            Instant reservationStartTime,
            Instant reservationEndTime,
            String customerName,
            int partySize
    ) {
        return new TablesBoardReservationInfo(
                reservationId,
                reservationStartTime,
                reservationEndTime,
                customerName,
                partySize,
                false,
                Set.of()
        );
    }

    public static TablesBoardReservationInfo of(
            UUID reservationId,
            Instant reservationStartTime,
            Instant reservationEndTime,
            String customerName,
            int partySize,
            Set<String> attentions
    ) {
        return new TablesBoardReservationInfo(
                reservationId,
                reservationStartTime,
                reservationEndTime,
                customerName,
                partySize,
                false,
                attentions
        );
    }

    public TablesBoardReservationInfo markWalkInBlocked() {
        return new TablesBoardReservationInfo(
                reservationId,
                reservationStartTime,
                reservationEndTime,
                customerName,
                partySize,
                true,
                attentions
        );
    }

    public TablesBoardReservationInfo addAttention(String attention) {
        Objects.requireNonNull(attention, "attention must not be null");

        Set<String> updated = new HashSet<>(attentions);
        updated.add(attention);

        return new TablesBoardReservationInfo(
                reservationId,
                reservationStartTime,
                reservationEndTime,
                customerName,
                partySize,
                walkInBlocked,
                updated
        );
    }

    public TablesBoardReservationInfo removeAttention(String attention) {
        Objects.requireNonNull(attention, "attention must not be null");

        Set<String> updated = new HashSet<>(attentions);
        updated.remove(attention);

        return new TablesBoardReservationInfo(
                reservationId,
                reservationStartTime,
                reservationEndTime,
                customerName,
                partySize,
                walkInBlocked,
                updated
        );
    }

    public boolean hasAttentions() {
        return !attentions.isEmpty();
    }
}