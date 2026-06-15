package com.oms.app.reservations.infra.adapters.out.tables;

import com.oms.app.reservations.application.port.out.ReservationsTablesPort;
import com.oms.app.reservations.domain.vo.ReservationTable;
import com.oms.app.tables.infra.adapters.in.forReservations.TablesForReservationsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
class ReservationsTablesAdapter implements ReservationsTablesPort {

    private final TablesForReservationsApi tablesApi;

    @Override
    public ReservationTable getTableWithLockForReservation(UUID tableId) {
        TablesForReservationsApi.TableForReservationDTO table =
                tablesApi.getTableWithLockForReservation(tableId);

        return ReservationTable.of(
                table.id(),
                table.available(),
                table.status(),
                table.capacity()
        );
    }
}


