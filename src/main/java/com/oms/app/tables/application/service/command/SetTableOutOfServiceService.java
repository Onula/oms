package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.SetTableOutOfServiceUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.TableStatus;
import com.oms.app.tables.events.TableMarkedOutOfServiceEvent;
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
public class SetTableOutOfServiceService implements SetTableOutOfServiceUseCasePort {

    private final TableRepositoryPort tableRepository;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {
        Objects.requireNonNull(cmd, "SetTableOutOfServiceCommand cannot be null");
        Instant now = Instant.now(clock);
        log.info("[SetTableOutOfService] tableId={}", cmd.tableId());

        Table table = tableRepository.findById(cmd.tableId())
                .orElseThrow(() -> tableNotFound(cmd.tableId().value()));

        TableStatus oldStatus = table.getStatus();

        table.setOutOfService();

        if (oldStatus == table.getStatus()) {log.info("[SetTableOutOfService] no status change for tableId={}", table.getId());return;}

        tableRepository.save(table);

        eventPublisherPort.publish(
                TableMarkedOutOfServiceEvent.of(table, now)
        );

        log.info("[SetTableOutOfService] tableId={} status changed from {} to {}", table.getId(), oldStatus, table.getStatus());
    }
}
