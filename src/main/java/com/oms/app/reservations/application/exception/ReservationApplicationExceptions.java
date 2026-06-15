package com.oms.app.reservations.application.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.ResourceNotFoundException;

import java.time.Instant;
import java.util.UUID;

public final class ReservationApplicationExceptions {

    private static final ExceptionCode RESERVATION_NOT_FOUND = ExceptionCode.of("reservations.not_found", "reservationId");
    private static final ExceptionCode TABLE_NOT_FOUND_FOR_RESERVATION = ExceptionCode.of("reservations.table.not_found_for_reservation", "tableId");
    private static final ExceptionCode OVERLAPS_EXISTING_RESERVATION = ExceptionCode.of("reservations.overlaps_existing", "tableId");

    private ReservationApplicationExceptions() {
    }

    public static ResourceNotFoundException reservationNotFound(UUID reservationId) {
        return new ResourceNotFoundException(
                RESERVATION_NOT_FOUND,
                reservationId
        );
    }

    public static ResourceNotFoundException tableNotFoundForReservation(UUID tableId) {
        return new ResourceNotFoundException(
                TABLE_NOT_FOUND_FOR_RESERVATION,
                tableId
        );
    }

    public static BusinessConflictException cannotCreateReservationOverlapsExisting(UUID tableId, Instant startTime, Instant endTime) {
        return new BusinessConflictException(
                OVERLAPS_EXISTING_RESERVATION,
                tableId,
                startTime,
                endTime
        );
    }


}