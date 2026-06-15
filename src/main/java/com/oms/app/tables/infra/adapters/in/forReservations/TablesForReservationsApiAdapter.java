package com.oms.app.tables.infra.adapters.in.forReservations;

import com.oms.app.tables.application.port.in.query.GetTableByIdWithLockUseCase;
import com.oms.app.tables.application.port.in.query.TableDTO;
import com.oms.app.tables.domain.TableStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
class TablesForReservationsApiAdapter implements TablesForReservationsApi {

    private final GetTableByIdWithLockUseCase getTableWithLockUseCase;

    @Override
    public TableForReservationDTO getTableWithLockForReservation(UUID tableId) {
        GetTableByIdWithLockUseCase.Query query = GetTableByIdWithLockUseCase.Query.of(tableId);

       TableDTO result = getTableWithLockUseCase.handle(query);

        return new TableForReservationDTO(
                result.tableId(),
                result.status().equals(TableStatus.FREE.name()),
                result.status(),
                result.capacity()
        );
    }
}
