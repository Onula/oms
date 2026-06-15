package com.oms.app.visits.application.port.out;

import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.domain.VisitStatus;

import java.util.Optional;
import java.util.UUID;

public interface VisitRepositoryPort {

    Optional<Visit> findById(UUID visitId);

    Optional<Visit> findByIdWithLock(UUID visitId);

    void save(Visit visit);

    boolean existsByIdAndStatus(UUID value, VisitStatus visitStatus);
}