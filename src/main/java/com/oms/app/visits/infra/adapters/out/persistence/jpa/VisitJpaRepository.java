package com.oms.app.visits.infra.adapters.out.persistence.jpa;

import com.oms.app.visits.domain.VisitStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface VisitJpaRepository extends JpaRepository<VisitJpaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM VisitJpaEntity v WHERE v.id = :id")
    Optional<VisitJpaEntity> findByIdWithLock(@Param("id") UUID id);

    boolean existsByIdAndStatus(UUID id, VisitStatus status);

}
