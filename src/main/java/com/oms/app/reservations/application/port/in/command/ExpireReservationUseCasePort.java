package com.oms.app.reservations.application.port.in.command;

import com.oms.app.reservations.domain.vo.ReservationId;

public interface ExpireReservationUseCasePort {
    void expireReservation(ReservationId reservationId);
}
