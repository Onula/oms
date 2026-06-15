package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.ChangeTablePlacementUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.TablePlacement;
import com.oms.app.tables.domain.TablesMap;
import com.oms.app.tables.domain.vo.TableId;
import com.oms.app.tables.events.TablePlacementChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

import static com.oms.app.tables.application.exception.TableApplicationExceptions.tableNotFound;

@Service
@RequiredArgsConstructor
public class ChangeTablePlacementService implements ChangeTablePlacementUseCasePort {

    private final TableRepositoryPort tableRepositoryPort;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {

        Instant now = Instant.now(clock);
        Table table = tableRepositoryPort.findByIdWithLock(cmd.tableId())
                .orElseThrow(() -> tableNotFound(cmd.tableId().value()));


        List<TablePlacement> existingPlacements =
                tableRepositoryPort.findActivePlacementsExcept(table.getId());

        TablesMap.ensurePlacementAllowed(cmd.newPlacement(), existingPlacements);

        table.changePlacement(cmd.newPlacement());

        tableRepositoryPort.save(table);

        eventPublisherPort.publish(
                TablePlacementChangedEvent.of(table, now)
        );
    }
}
