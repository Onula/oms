package com.oms.app.visits.application.service.command;

import com.oms.app.visits.application.port.in.command.TransferVisitUseCasePort;
import com.oms.app.visits.application.port.out.VisitEventPublisherPort;
import com.oms.app.visits.application.port.out.VisitRepositoryPort;
import com.oms.app.visits.application.port.out.VisitTablesPort;
import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.domain.vo.VisitTableId;
import com.oms.app.visits.events.VisitTransferredEvent;
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
public class TransferVisitService implements TransferVisitUseCasePort {

    private final VisitEventPublisherPort eventPublisherPort;
    private final VisitRepositoryPort visitRepositoryPort;
    private final VisitTablesPort visitTablesPort;

    private final Clock clock;

    @Override
    public void transfer(Command cmd) {
        Objects.requireNonNull(cmd, "TransferVisitCommand must not be null");
        Instant now = Instant.now(clock);
        log.info("[TransferVisit] visitId={} targetTableId={}", cmd.visitId(), cmd.toTableId());

        Visit visit = visitRepositoryPort.findByIdWithLock(cmd.visitId().value())
                .orElseThrow(() -> visitNotFound(cmd.visitId().value()));

        VisitTableId fromTableId = visit.getTableId();

        visit.transferTo(cmd.toTableId());

        visitTablesPort.transferTableOccupancy(
                fromTableId.value(),
                cmd.toTableId().value()
        );

        visitRepositoryPort.save(visit);

        eventPublisherPort.publish(
                VisitTransferredEvent.of(
                        visit,
                        fromTableId,
                        cmd.toTableId(),
                        now
                )
        );

        log.info("[TransferVisit] transferred visitId={} fromTableId={} toTableId={}", visit.getId(), cmd.toTableId(), cmd.toTableId());
    }
}