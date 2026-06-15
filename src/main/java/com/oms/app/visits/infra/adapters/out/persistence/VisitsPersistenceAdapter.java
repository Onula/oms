package com.oms.app.visits.infra.adapters.out.persistence;

import com.oms.app.visits.application.port.out.VisitRepositoryPort;
import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.domain.VisitStatus;
import com.oms.app.visits.infra.adapters.out.persistence.jpa.VisitJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VisitsPersistenceAdapter implements VisitRepositoryPort {
    VisitJpaRepository visitJpaRepository;
    VisitPersistenceMapper mapper;

    @Override
    public Optional<Visit> findById(UUID visitId) {
        return visitJpaRepository.findById(visitId).map(mapper::toDomain);
    }

    @Override
    public Optional<Visit> findByIdWithLock(UUID visitId) {
        return visitJpaRepository.findByIdWithLock(visitId).map(mapper::toDomain);
    }

    @Override
    public void save(Visit visit) {
        visitJpaRepository.save(mapper.toJpaEntity(visit));
    }

    @Override
    public boolean existsByIdAndStatus(UUID visitId, VisitStatus visitStatus) {
        return visitJpaRepository.existsByIdAndStatus(visitId,visitStatus);
    }
}
