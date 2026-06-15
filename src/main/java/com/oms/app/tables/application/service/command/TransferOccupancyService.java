package com.oms.app.tables.application.service.command;

import com.oms.app.tables.application.port.in.command.TransferOccupancyUseCasePort;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.vo.TableId;
import com.oms.app.tables.events.TableOccupancyTransferredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import static com.oms.app.tables.application.exception.TableApplicationExceptions.cannotTransferToSameTable;
import static com.oms.app.tables.application.exception.TableApplicationExceptions.tableNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferOccupancyService implements TransferOccupancyUseCasePort {

    private final TableRepositoryPort tableRepository;
    private final TableEventPublisherPort eventPublisherPort;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {
        Objects.requireNonNull(cmd, "TransferOccupancyCommand must not be null");
        Instant now = Instant.now(clock);

        TableId toTableId = cmd.toTableId();
        TableId fromTableId = cmd.fromTableId();

        log.info("[TransferOccupancy] fromTableId={} toTableId={}", fromTableId, toTableId);



        if (cmd.fromTableId().equals(toTableId)) {
            throw cannotTransferToSameTable(toTableId.value());
        }

        Table fromTable = tableRepository.findByIdWithLock(fromTableId)
                .orElseThrow(() -> tableNotFound(fromTableId.value()));

        Table toTable = tableRepository.findByIdWithLock(toTableId )
                .orElseThrow(() -> tableNotFound(toTableId .value()));

        fromTable.release();
        toTable.occupy();

        tableRepository.save(fromTable);
        tableRepository.save(toTable);

        eventPublisherPort.publish(
                TableOccupancyTransferredEvent.of(fromTable, toTable, now)
        );
        log.info("[TransferOccupancy] transfer occupancy completed fromTableId={} toTableId={}",fromTable.getId(), toTable.getId());
    }
}