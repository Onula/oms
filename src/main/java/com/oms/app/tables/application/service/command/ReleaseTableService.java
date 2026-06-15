package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.ReleaseTableUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.vo.TableId;
import com.oms.app.tables.events.TableReleasedEvent;
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
public class ReleaseTableService implements ReleaseTableUseCasePort {

    private final TableRepositoryPort tableRepository;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {
        Objects.requireNonNull(cmd, "MarkAvailableCommand must not be null");
        Instant now = Instant.now(clock);
        TableId tableId = cmd.tableId();
        log.info("[ReleaseTable] tableId={}", tableId);

        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> tableNotFound(tableId.value()));

        table.release();
        tableRepository.save(table);

        eventPublisherPort.publish(
                TableReleasedEvent.of(table, now)
        );

        log.info("[ReleaseTable] table released tableId={}", table.getId());
    }
}