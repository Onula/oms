package com.oms.app.reservations.application.port.in.command;

import com.oms.app.reservations.domain.vo.ReservationId;
import com.oms.app.reservations.domain.vo.ReservationVisitId;

import java.util.UUID;

public interface SeatReservationUseCasePort {

    void seat(Command cmd);


    record Command(
            ReservationId reservationId,
            ReservationVisitId visitId
    ) {
        public static Command of(
                UUID reservationId,
                UUID visitId
        ) {
            return new Command(
                    ReservationId.of(reservationId),
                    ReservationVisitId.of(visitId)
            );
        }
    }
}
