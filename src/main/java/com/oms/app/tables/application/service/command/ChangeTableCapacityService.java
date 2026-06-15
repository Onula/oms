package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.ChangeTableCapacityUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.vo.TableCapacity;
import com.oms.app.tables.events.TableCapacityChangedEvent;
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
public class ChangeTableCapacityService implements ChangeTableCapacityUseCasePort {

    private final TableRepositoryPort tableRepository;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {
        Objects.requireNonNull(cmd, "ChangeTableCapacityCommand cannot be null");
        Instant now = Instant.now(clock);
        log.info("[ChangeTableCapacity] tableId={} newCapacity={}", cmd.tableId(), cmd.capacity());

        Table table = tableRepository.findById(cmd.tableId())
                .orElseThrow(() -> tableNotFound(cmd.tableId().value()));

        TableCapacity oldCapacity = table.getCapacity();

        table.changeCapacity(cmd.capacity());

        if (oldCapacity.equals(table.getCapacity())) {
            log.info("[ChangeTableCapacity] no capacity change for tableId={}", table.getId());
            return;
        }

        tableRepository.save(table);

        eventPublisherPort.publish(
                TableCapacityChangedEvent.of(table, oldCapacity, now)
        );

        log.info("[ChangeTableCapacity] tableId={} capacity changed from {} to {}", table.getId(), oldCapacity, table.getCapacity());
    }
}
