package com.oms.app.tables.infra.adapters.in.forVisits;

import com.oms.app.tables.application.port.in.command.OccupyTableUseCasePort;
import com.oms.app.tables.application.port.in.command.ReleaseTableUseCasePort;
import com.oms.app.tables.application.port.in.command.TransferOccupancyUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TableForVisitsApiAdapter implements TableForVisitsApi {

    private final OccupyTableUseCasePort  occupyTableUseCasePort;
    private final ReleaseTableUseCasePort releaseTableUseCasePort;
    private final TransferOccupancyUseCasePort  transferOccupancyUseCasePort;

    @Override
    public void occupyTable(UUID tableId) {

        occupyTableUseCasePort.handle(OccupyTableUseCasePort.Command.of(tableId));
    }

    @Override
    public void releaseTable(UUID tableId) {
        releaseTableUseCasePort.handle(ReleaseTableUseCasePort.Command.of(tableId));
    }

    @Override
    public void transferTableOccupy(UUID fromTableId, UUID toTableId) {
        transferOccupancyUseCasePort.handle(TransferOccupancyUseCasePort.Command.of(fromTableId, toTableId));
    }
}
