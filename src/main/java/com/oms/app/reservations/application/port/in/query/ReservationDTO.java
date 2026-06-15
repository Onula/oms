package com.oms.app.reservations.application.port.in.query;

import com.oms.app.reservations.domain.Reservation;
import com.oms.app.reservations.domain.vo.*;

import java.time.Instant;
import java.util.UUID;

public record ReservationDTO(
        UUID id,
        UUID tableId,
        UUID visitId,
        String customer,
        int partySize,
        Instant startTime,
        Instant endTime,
        Instant expirationTime,
        Instant walkInBlockTime,
        String notes,
        String status,
        Instant createdAt
) {

    public static ReservationDTO of(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId().value(),
                reservation.getTableId().value(),
                reservation.getVisitId() == null ? null : reservation.getVisitId().value(),
                reservation.getCustomer().name().value(),
                reservation.getPartySize().value(),
                reservation.getTiming().startTime(),
                reservation.getTiming().endTime(),
                reservation.getTiming().expirationTime(),
                reservation.getTiming().walkInBlockTime(),
                reservation.getNotes().value(),
                reservation.getStatus().name(),
                reservation.getCreatedAt()
        );
    }
}
