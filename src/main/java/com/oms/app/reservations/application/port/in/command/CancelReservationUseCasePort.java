package com.oms.app.reservations.application.port.in.command;

import com.oms.app.reservations.domain.vo.*;

import java.util.UUID;

public interface CancelReservationUseCasePort {

    void cancel(Command cmd);

    record Command(
            ReservationId reservationId
    ) {

        public static Command of(UUID reservationId) {

            return new Command(ReservationId.of(reservationId));
        }

    }


}
