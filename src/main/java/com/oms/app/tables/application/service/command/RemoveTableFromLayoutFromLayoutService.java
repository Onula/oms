package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.RemoveTableFromLayoutUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.events.TableRemovedFromLayoutEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import static com.oms.app.tables.application.exception.TableApplicationExceptions.tableNotFound;

@Service
@Slf4j
@AllArgsConstructor
public class RemoveTableFromLayoutFromLayoutService implements RemoveTableFromLayoutUseCasePort {

    private final TableRepositoryPort tableRepositoryPort;
    private final TableEventPublisherPort eventPublisherPort;
    private Clock clock;

    @Transactional
    public void handle(Command cmd){
        Objects.requireNonNull(cmd, "DeleteTableCommand cannot be null");
        Instant now = Instant.now(clock);
        log.info("[DeleteTableService] Executing delete table command for tableId={}", cmd.tableId());

        Table table =  tableRepositoryPort.findById(cmd.tableId())
                .orElseThrow(() -> tableNotFound(cmd.tableId().value()));

        table.removeFromLayout();

        log.info("[DeleteTableService] Deleted tableId{}", cmd.tableId());

        eventPublisherPort.publish(
                TableRemovedFromLayoutEvent.of(table, now)
        );

        tableRepositoryPort.save(table);

        log.info("[DeleteTableService] TableDeletedEvent published ");

    }
}
