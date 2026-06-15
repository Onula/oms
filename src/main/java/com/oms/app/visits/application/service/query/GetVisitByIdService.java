package com.oms.app.visits.application.service.query;

import com.oms.app.visits.application.port.in.query.GetVisitByIdUseCasePort;
import com.oms.app.visits.application.port.out.VisitRepositoryPort;
import com.oms.app.visits.domain.Visit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.oms.app.visits.application.exception.VisitsApplicationExceptions.visitNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class GetVisitByIdService implements GetVisitByIdUseCasePort {

    private final VisitRepositoryPort visitRepositoryPort;

    @Override
    public Result get(Query query) {
        Objects.requireNonNull(query, "GetVisitByIdQuery must not be null");

        log.info("[GetVisitById] visitId={}", query.visitId());

        Visit visit = visitRepositoryPort.findById(query.visitId().value())
                .orElseThrow(() -> visitNotFound(query.visitId().value()));

        log.info("[GetVisitById] visit found visitId={}", query.visitId());
        return Result.from(visit);
    }
}