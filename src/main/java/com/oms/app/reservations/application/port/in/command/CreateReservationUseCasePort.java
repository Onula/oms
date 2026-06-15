package com.oms.app.reservations.application.port.in.command;
import com.oms.app.reservations.domain.vo.*;

import java.time.Instant;
import java.util.UUID;

public interface CreateReservationUseCasePort {

    Result create(Command cmd);

    record Command(
            ReservationTableId tableId,
            ReservationCustomer customer,
            ReservationPartySize partySize,
            ReservationTiming timing,
            ReservationNotes notes
    ) {
        public static Command of(
                UUID tableId,
                String customerName,
                String customerPhone,
                Integer partySize,
                Instant startTime,
                Instant endTime,
                Instant expirationTime,
                Instant walkInBlockTime,
                String notes
        ) {
            return new Command(
                    ReservationTableId.of(tableId),
                    ReservationCustomer.of(
                            ReservationCustomerName.of(customerName),
                            ReservationCustomerPhone.of(customerPhone)
                    ),
                    ReservationPartySize.of(partySize),
                    ReservationTiming.of(startTime, endTime, expirationTime,walkInBlockTime),
                    ReservationNotes.of(notes)
            );
        }
    }

    record Result(
            UUID reservationId
    ){
        public static Result of(ReservationId reservationId) {

            return new Result(reservationId.value());
        }
    }
}
