package com.oms.app.reservations.application.port.out;

import com.oms.app.reservations.domain.Reservation;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepositoryPort {

    Optional<Reservation> findById(UUID id);

    Optional<Reservation> findByIdForUpdate(UUID id);

    List<Reservation> findActiveReservations();

    void save(Reservation reservation);

    boolean hasActiveReservationForTableDuring(UUID tableId, Instant startTime, Instant endTime);

    List<Reservation> findActiveOrUpcomingByTableIdForUpdate(UUID tableId);
}
