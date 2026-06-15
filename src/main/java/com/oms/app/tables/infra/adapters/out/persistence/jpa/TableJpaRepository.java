package com.oms.app.tables.infra.adapters.out.persistence.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TableJpaRepository extends JpaRepository<TableJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM TableJpaEntity t WHERE t.id = :tableId")
    Optional<TableJpaEntity> findByIdWithLock(@Param("tableId") UUID tableId);

    @Query("""
    SELECT t
    FROM TableJpaEntity t
    WHERE t.status <> 'REMOVED_FROM_LAYOUT'
    """)
    List<TableJpaEntity> findActiveLayoutTables();

    @Query("""
    SELECT t
    FROM TableJpaEntity t
    WHERE t.id <> :tableId
    AND t.status <> 'REMOVED_FROM_LAYOUT'
    """)
    List<TableJpaEntity> findActiveLayoutTablesExcept(UUID tableId);
}