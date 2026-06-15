package com.oms.app.visits.infra.adapters.out.tables;

import com.oms.app.tables.infra.adapters.in.forVisits.TableForVisitsApi;
import com.oms.app.visits.application.port.out.VisitTablesPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VisitTablesAdapter implements VisitTablesPort {

    private final TableForVisitsApi tableForVisitsApi;

    @Override
    public void occupyTableForVisit(UUID tableId) {
        tableForVisitsApi.occupyTable(tableId);
    }

    @Override
    public void releaseTableFromVisit(UUID tableId) {
        tableForVisitsApi.releaseTable(tableId);
    }

    @Override
    public void transferTableOccupancy(UUID fromTableId, UUID toTableId) {
        tableForVisitsApi.transferTableOccupy(fromTableId, toTableId);

    }
}
