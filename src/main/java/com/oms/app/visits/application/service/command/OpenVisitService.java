package com.oms.app.visits.application.service.command;

import com.oms.app.visits.application.port.in.command.OpenVisitUseCasePort;
import com.oms.app.visits.application.port.out.VisitEventPublisherPort;
import com.oms.app.visits.application.port.out.VisitRepositoryPort;
import com.oms.app.visits.application.port.out.VisitTablesPort;
import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.events.VisitOpenedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OpenVisitService implements OpenVisitUseCasePort {

    private final VisitEventPublisherPort eventPublisherPort;
    private final VisitRepositoryPort visitRepositoryPort;
    private final VisitTablesPort visitTablesPort;

    private final Clock clock;

    @Override
    public Result open(Command cmd) {
        Objects.requireNonNull(cmd, "OpenVisitCommand must not be null");

        log.info("[OpenVisit] tableId={} type={}", cmd.tableId(), cmd.type());

        Instant now = Instant.now(clock);

        Visit visit = Visit.open(
                cmd.tableId(),
                cmd.type(),
                now
        );

        visitTablesPort.occupyTableForVisit(cmd.tableId().value());

        visitRepositoryPort.save(visit);

        eventPublisherPort.publish(
                VisitOpenedEvent.of(visit, now)
        );

        log.info("[OpenVisit] opened visitId={} tableId={}", visit.getId(), visit.getTableId());

        return Result.of(visit.getId());
    }
}