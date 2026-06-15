package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.CreateTableUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.TablePlacement;
import com.oms.app.tables.domain.TablesMap;
import com.oms.app.tables.events.TableCreatedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateTableService implements CreateTableUseCasePort {

    private final TableRepositoryPort tableRepository;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public Result handle(Command cmd) {
        Objects.requireNonNull(cmd, "CreateTableCommand cannot be null");
        Instant now = Instant.now(clock);
        log.info("[CreateTable] code={} capacity={}", cmd.code(), cmd.capacity());

        List<TablePlacement> existingPlacements =
                tableRepository.findActivePlacements();

        TablesMap.ensurePlacementAllowed(cmd.placement(), existingPlacements);

        Table newTable = Table.create(
                cmd.code(),
                cmd.capacity(),
                cmd.placement()
        );
        tableRepository.save(newTable);

        eventPublisherPort.publish(
                TableCreatedEvent.of(newTable, now)
        );

        log.info("[CreateTable] table created with id={}, code={}, capacity={}", newTable.getId(), newTable.getCode(), newTable.getCapacity());
        return Result.of(newTable.getId().value());
    }
}
