package com.oms.app.tables.infra.adapters.in.forReservations;

import java.util.UUID;

public interface TablesForReservationsApi {

    TableForReservationDTO getTableWithLockForReservation(UUID tableId);

    record TableForReservationDTO(
            UUID id,
            boolean available,
            String status,
            int capacity
    ) {}

}
