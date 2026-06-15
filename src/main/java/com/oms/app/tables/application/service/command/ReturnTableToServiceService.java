package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.ReturnTableToServiceUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.TableStatus;
import com.oms.app.tables.events.TableMarkedOutOfServiceEvent;
import com.oms.app.tables.events.TableReturnedToServiceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import static com.oms.app.tables.application.exception.TableApplicationExceptions.tableNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReturnTableToServiceService implements ReturnTableToServiceUseCasePort {

    private final TableRepositoryPort tableRepository;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {
        Objects.requireNonNull(cmd, "ReturnTableToServiceCommand cannot be null");
        Instant now = Instant.now(clock);
        log.info("[ReturnTableToService] tableId={}", cmd.tableId());

        Table table = tableRepository.findById(cmd.tableId())
                .orElseThrow(() -> tableNotFound(cmd.tableId().value()));

        TableStatus oldStatus = table.getStatus();

        table.returnToService();

        if (oldStatus == table.getStatus()) {
            log.info("[SetTableOutOfService] no status change for tableId={}", table.getId());
            return;
        }

        tableRepository.save(table);

        eventPublisherPort.publish(
                TableReturnedToServiceEvent.of(table, now)
        );

        log.info("[ReturnTableToService] tableId={} status changed from {} to {}", table.getId(), oldStatus, table.getStatus());
    }
}