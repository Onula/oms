package com.oms.app.reservations.infra.adapters.out.persistence.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataReservationJpaRepository extends JpaRepository<ReservationJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM ReservationJpaEntity r WHERE r.id = :id")
    Optional<ReservationJpaEntity> findByIdForUpdate(UUID id);

    @Query("""
        SELECT r
        FROM ReservationJpaEntity r
        WHERE r.status = 'ACTIVE'
        """)
    List<ReservationJpaEntity> findActiveReservations();

    @Query(value = """
        SELECT EXISTS (
            SELECT 1
            FROM reservations r
            WHERE r.table_id = :tableId
            AND r.status NOT IN ('CANCELLED', 'SEATED', 'EXPIRED')
            AND r.start_time < :endTime
            AND r.end_time > :startTime
        )
        """, nativeQuery = true)
    boolean hasActiveReservationForTableDuring(UUID tableId, Instant startTime, Instant endTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT r
    FROM ReservationJpaEntity r
    WHERE r.tableId = :tableId
    AND r.status NOT IN ('CANCELLED', 'SEATED', 'EXPIRED')
    """)
    List<ReservationJpaEntity> findActiveOrUpcomingByTableIdForUpdate(UUID tableId);
}