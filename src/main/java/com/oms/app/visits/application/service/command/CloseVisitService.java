package com.oms.app.visits.application.service.command;

import com.oms.app.visits.application.port.in.command.CloseVisitUseCasePort;
import com.oms.app.visits.application.port.out.VisitEventPublisherPort;
import com.oms.app.visits.application.port.out.VisitRepositoryPort;
import com.oms.app.visits.application.port.out.VisitTablesPort;
import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.events.VisitClosedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import static com.oms.app.visits.application.exception.VisitsApplicationExceptions.visitNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CloseVisitService implements CloseVisitUseCasePort {

    private final VisitEventPublisherPort eventPublisherPort;
    private final VisitRepositoryPort visitRepositoryPort;
    private final VisitTablesPort visitTablesPort;

    private final Clock clock;

    @Override
    public void close(Command cmd) {
        Objects.requireNonNull(cmd, "CloseVisitCommand must not be null");

        log.info("[CloseVisit] visitId={}", cmd.visitId());

        Instant now = Instant.now(clock);

        Visit visit = visitRepositoryPort.findByIdWithLock(cmd.visitId().value())
                .orElseThrow(() -> visitNotFound(cmd.visitId().value()));

        visit.close(now);

        visitTablesPort.releaseTableFromVisit(visit.getTableId().value());

        visitRepositoryPort.save(visit);

        eventPublisherPort.publish(
                VisitClosedEvent.of(visit, now)
        );

        log.info("[CloseVisit] closed visitId={} tableId={}", visit.getId(), visit.getTableId());
    }
}