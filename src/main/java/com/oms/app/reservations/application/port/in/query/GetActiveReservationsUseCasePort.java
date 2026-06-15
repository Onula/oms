package com.oms.app.reservations.application.port.in.query;

import com.oms.app.reservations.domain.Reservation;

import java.util.List;

public interface GetActiveReservationsUseCasePort {

    Result handle();

    record Result(List<ReservationDTO> reservations) {

        public static Result of(List<Reservation> reservations) {
            return new Result(
                    reservations.stream()
                            .map(ReservationDTO::of)
                            .toList()
            );
        }
    }
}
