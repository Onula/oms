package com.oms.app.ui.tables_board.application.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public record TablesBoardItem(
        UUID tableId,
        String tableCode,
        int capacity,
        TableDisplayStatus displayStatus,
        TablesBoardPlacement placement,
        TablesBoardActiveVisitInfo activeVisit,
        TablesBoardCurrentReservationInfo currentReservation,
        List<TablesBoardReservationInfo> upcomingReservations
) {

    private static final int MAX_UPCOMING_RESERVATIONS = 2;

    public TablesBoardItem {
        Objects.requireNonNull(tableId, "table id must not be null");
        Objects.requireNonNull(tableCode, "table code must not be null");
        Objects.requireNonNull(displayStatus, "display status must not be null");
        Objects.requireNonNull(placement, "table placement must not be null");

        upcomingReservations = upcomingReservations == null
                ? List.of()
                : upcomingReservations.stream()
                .sorted(Comparator.comparing(TablesBoardReservationInfo::reservationStartTime))
                .limit(MAX_UPCOMING_RESERVATIONS)
                .toList();
    }

    public static TablesBoardItem createFree(
            UUID tableId,
            String tableCode,
            int capacity,
            TablesBoardPlacement placement
    ) {
        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                TableDisplayStatus.FREE,
                placement,
                null,
                null,
                List.of()
        );
    }

    public TablesBoardItem markOutOfService() {
        return withDisplayStatus(TableDisplayStatus.OUT_OF_SERVICE);
    }

    public TablesBoardItem returnToService() {
        return withDisplayStatus(TableDisplayStatus.FREE);
    }

    public TablesBoardItem withCapacity(int capacity) {
        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }


    public TablesBoardItem withPlacement(TablesBoardPlacement placement) {
        Objects.requireNonNull(placement, "table placement must not be null");

        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }

    public TablesBoardItem withDisplayStatus(TableDisplayStatus displayStatus) {
        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }

    public TablesBoardItem withActiveVisit(TablesBoardActiveVisitInfo activeVisit) {
        Objects.requireNonNull(activeVisit, "active visit must not be null");

        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }

    public TablesBoardItem withoutActiveVisit() {
        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }

    public TablesBoardItem withCurrentReservation(TablesBoardCurrentReservationInfo currentReservation) {
        Objects.requireNonNull(currentReservation, "current reservation must not be null");

        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }

    public TablesBoardItem withoutCurrentReservation() {
        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }

    public TablesBoardItem withUpcomingReservation(TablesBoardReservationInfo reservation) {
        Objects.requireNonNull(reservation, "reservation must not be null");

        List<TablesBoardReservationInfo> updatedReservations = upcomingReservations.stream()
                .filter(existing -> !existing.reservationId().equals(reservation.reservationId()))
                .collect(Collectors.toList());

        updatedReservations.add(reservation);

        return withUpcomingReservations(updatedReservations);
    }

    public TablesBoardItem withUpcomingReservations(List<TablesBoardReservationInfo> upcomingReservations) {
        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                currentReservation,
                upcomingReservations
        );
    }

    public TablesBoardItem withoutUpcomingReservation(UUID reservationId) {
        Objects.requireNonNull(reservationId, "reservation id must not be null");

        List<TablesBoardReservationInfo> updatedReservations = upcomingReservations.stream()
                .filter(reservation -> !reservation.reservationId().equals(reservationId))
                .toList();

        return withUpcomingReservations(updatedReservations);
    }

    public TablesBoardItem withReservationAttentionAdded(
            UUID reservationId,
            String attention
    ) {
        Objects.requireNonNull(reservationId, "reservation id must not be null");
        Objects.requireNonNull(attention, "attention must not be null");

        TablesBoardCurrentReservationInfo updatedCurrentReservation = currentReservation;

        if (currentReservation != null
                && currentReservation.reservationId().equals(reservationId)) {
            updatedCurrentReservation = currentReservation.addAttention(attention);
        }

        List<TablesBoardReservationInfo> updatedUpcomingReservations = upcomingReservations.stream()
                .map(reservation -> reservation.reservationId().equals(reservationId)
                        ? reservation.addAttention(attention)
                        : reservation)
                .toList();

        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                updatedCurrentReservation,
                updatedUpcomingReservations
        );
    }

    public TablesBoardItem withReservationAttentionRemoved(
            UUID reservationId,
            String attention
    ) {
        Objects.requireNonNull(reservationId, "reservation id must not be null");
        Objects.requireNonNull(attention, "attention must not be null");

        TablesBoardCurrentReservationInfo updatedCurrentReservation = currentReservation;

        if (currentReservation != null
                && currentReservation.reservationId().equals(reservationId)) {
            updatedCurrentReservation = currentReservation.removeAttention(attention);
        }

        List<TablesBoardReservationInfo> updatedUpcomingReservations = upcomingReservations.stream()
                .map(reservation -> reservation.reservationId().equals(reservationId)
                        ? reservation.removeAttention(attention)
                        : reservation)
                .toList();

        return new TablesBoardItem(
                tableId,
                tableCode,
                capacity,
                displayStatus,
                placement,
                activeVisit,
                updatedCurrentReservation,
                updatedUpcomingReservations
        );
    }

    public TablesBoardItem markReservationWalkInBlocked(UUID reservationId) {
        Objects.requireNonNull(reservationId, "reservation id must not be null");

        List<TablesBoardReservationInfo> updatedReservations = upcomingReservations.stream()
                .map(reservation -> reservation.reservationId().equals(reservationId)
                        ? reservation.markWalkInBlocked()
                        : reservation)
                .toList();

        return withUpcomingReservations(updatedReservations);
    }

    public boolean hasWalkInBlockedReservation() {
        return upcomingReservations.stream()
                .anyMatch(TablesBoardReservationInfo::walkInBlocked);
    }

    public boolean hasReservationAttentions() {
        return currentReservation != null && currentReservation.hasAttentions()
                || upcomingReservations.stream()
                .anyMatch(TablesBoardReservationInfo::hasAttentions);
    }

    public boolean hasActiveVisit() {
        return activeVisit != null;
    }

    public boolean hasCurrentReservation() {
        return currentReservation != null;
    }

    public boolean hasUpcomingReservations() {
        return !upcomingReservations.isEmpty();
    }
}