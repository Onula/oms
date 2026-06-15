package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.OccupyTableUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.vo.TableId;
import com.oms.app.tables.events.TableOccupiedEvent;
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
public class OccupyTableService implements OccupyTableUseCasePort {

    private final TableRepositoryPort tableRepository;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {
        Objects.requireNonNull(cmd, "MarkOccupiedCommand must not be null");
        Instant now = Instant.now(clock);
        TableId tableId = cmd.tableId();
        log.info("[OccupyTable] tableId={}", tableId);



        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> tableNotFound(tableId.value()));

        table.occupy();
        tableRepository.save(table);

        eventPublisherPort.publish(
                TableOccupiedEvent.of(table, now)
        );

        log.info("[OccupyTable] tableId={} marked as occupied", cmd.tableId());
    }
}