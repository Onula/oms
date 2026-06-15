package com.oms.app.reservations.infra.adapters.out.persistence;

import com.oms.app.reservations.application.port.out.ReservationRepositoryPort;
import com.oms.app.reservations.domain.Reservation;
import com.oms.app.reservations.infra.adapters.out.persistence.jpa.SpringDataReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReservationPersistenceAdapter implements ReservationRepositoryPort {

    private final SpringDataReservationJpaRepository jpaRepository;
    private final ReservationPersistenceMapper mapper;

    @Override
    public Optional<Reservation> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Reservation> findByIdForUpdate(UUID id) {
        return jpaRepository.findByIdForUpdate(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Reservation> findActiveReservations() {
        return jpaRepository.findActiveReservations()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void save(Reservation reservation) {
        jpaRepository.save(mapper.toJpa(reservation));
    }

    @Override
    public boolean hasActiveReservationForTableDuring(UUID tableId, Instant startTime, Instant endTime) {
        return jpaRepository.hasActiveReservationForTableDuring(tableId, startTime, endTime);
    }

    @Override
    public List<Reservation> findActiveOrUpcomingByTableIdForUpdate(UUID tableId) {
        return jpaRepository.findActiveOrUpcomingByTableIdForUpdate(tableId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}