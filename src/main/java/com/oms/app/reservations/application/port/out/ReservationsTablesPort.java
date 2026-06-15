package com.oms.app.reservations.application.port.out;

import com.oms.app.reservations.domain.vo.ReservationTable;

import java.util.UUID;

public interface ReservationsTablesPort {
    ReservationTable getTableWithLockForReservation(UUID reservationTableId);
}

