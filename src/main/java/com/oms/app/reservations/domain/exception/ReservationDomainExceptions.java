package com.oms.app.reservations.domain.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.InvalidValueException;
import com.oms.app.reservations.domain.ReservationStatus;
import com.oms.app.reservations.domain.vo.ReservationId;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public final class ReservationDomainExceptions {
    //Business
    private static final ExceptionCode STATUS_CHANGE_NOT_ALLOWED = ExceptionCode.of("reservations.status_transition_not_allowed");
    private static final ExceptionCode EXPIRATION_TIME_NOT_REACHED = ExceptionCode.of("reservations.expiration_time_not_reached");
    private static final ExceptionCode TABLE_NOT_AVAILABLE = ExceptionCode.of("reservations.table.not_available_for_reservation");
    private static final ExceptionCode PARTY_SIZE_EXCEEDS_TABLE_CAPACITY = ExceptionCode.of("reservations.party_size.exceeds_table_capacity");
    //VO's
    private static final ExceptionCode RESERVATION_ID_REQUIRED = ExceptionCode.of("reservations.reservation_id.required", "reservationId");
    private static final ExceptionCode RESERVATION_TABLE_ID_REQUIRED = ExceptionCode.of("reservations.table_id.required", "tableId");
    private static final ExceptionCode RESERVATION_VISIT_ID_REQUIRED = ExceptionCode.of("reservations.visit_id.required", "visitId");
    private static final ExceptionCode CUSTOMER_NAME_INVALID = ExceptionCode.of("reservations.customer.name.invalid", "customerName");
    private static final ExceptionCode CUSTOMER_PHONE_INVALID = ExceptionCode.of("reservations.customer.phone.invalid", "customerPhone");
    private static final ExceptionCode PARTY_SIZE_INVALID = ExceptionCode.of("reservations.party_size.invalid", "partySize");
    private static final ExceptionCode START_TIME_REQUIRED = ExceptionCode.of("reservations.timing.start_time.required", "startTime");
    private static final ExceptionCode END_TIME_INVALID = ExceptionCode.of("reservations.timing.end_time.invalid", "endTime");
    private static final ExceptionCode EXPIRATION_TIME_INVALID = ExceptionCode.of("reservations.timing.expiration_time.invalid", "expirationTime");
    private static final ExceptionCode WALK_IN_BLOCK_TIME_INVALID = ExceptionCode.of("reservations.timing.walk_in_block_time.invalid", "walkInBlockTime");
    private static final ExceptionCode NOTES_INVALID = ExceptionCode.of("reservations.notes.invalid", "notes");
    private static final ExceptionCode TIMING_DURATION_TOO_LONG = ExceptionCode.of("reservations.timing.duration.too_long", "endTime");
    private static final ExceptionCode TIMING_EXPIRATION_TOO_LONG = ExceptionCode.of("reservations.timing.expiration.too_long", "expirationTime");
    private static final ExceptionCode TIMING_WALK_IN_BLOCK_TOO_EARLY = ExceptionCode.of("reservations.timing.walk_in_block.too_early", "walkInBlockTime");
    private static final ExceptionCode WALK_IN_BLOCK_STATUS_NOT_ALLOWED = ExceptionCode.of("reservations.walk_in_block.status_not_allowed", "status");

    private ReservationDomainExceptions() {
    }

    /** Change Reservation Status **/
    public static BusinessConflictException reservationStatusChangeNotAllowed(ReservationId id, ReservationStatus from, ReservationStatus to) {
        return new BusinessConflictException(STATUS_CHANGE_NOT_ALLOWED, id.value(), from, to);
    }

    /** Mark Expired **/
    public static BusinessConflictException cannotExpireReservationBeforeExpirationTime(ReservationId id, Instant expirationTime) {
        return new BusinessConflictException(EXPIRATION_TIME_NOT_REACHED, id.value(), expirationTime);
    }

    /** Create Reservation **/
    public static BusinessConflictException tableNotAvailableForReservation(UUID tableId, String currentStatus) {
        return new BusinessConflictException(TABLE_NOT_AVAILABLE, tableId, currentStatus);
    }
    public static BusinessConflictException partySizeExceedsTableCapacity(int partySize, int tableCapacity) {
        return new BusinessConflictException(PARTY_SIZE_EXCEEDS_TABLE_CAPACITY, partySize, tableCapacity);
    }


    /**
     * VO validations
     **/
    // ReservationId //
    public static InvalidValueException reservationIdRequired() {return new InvalidValueException(RESERVATION_ID_REQUIRED);}
    // TableId //
    public static InvalidValueException reservationTableIdRequired() {return new InvalidValueException(RESERVATION_TABLE_ID_REQUIRED);}
    public static InvalidValueException reservationVisitIdRequired() {return new InvalidValueException(RESERVATION_VISIT_ID_REQUIRED);}
    // ReservationPartySize //
    public static InvalidValueException invalidPartySize(int min, int max) {return new InvalidValueException(PARTY_SIZE_INVALID, min, max);}
    // ReservationCustomer //
    public static InvalidValueException invalidCustomerPhone(int min, int max, String example) {return new InvalidValueException(CUSTOMER_PHONE_INVALID, min, max, example);}
    public static InvalidValueException invalidCustomerName(int min, int max) {return new InvalidValueException(CUSTOMER_NAME_INVALID, min, max);}
    // ReservationNotes //
    public static InvalidValueException invalidReservationNotes(int max) {return new InvalidValueException(NOTES_INVALID, max);}
    // ReservationTiming //
    public static InvalidValueException startTimeRequired() {return new InvalidValueException(START_TIME_REQUIRED);}
    public static InvalidValueException invalidEndTime() {return new InvalidValueException(END_TIME_INVALID);}
    public static InvalidValueException invalidExpirationTime() {return new InvalidValueException(EXPIRATION_TIME_INVALID);}
    public static InvalidValueException reservationTooLong(Duration max) {return new InvalidValueException(TIMING_DURATION_TOO_LONG, max.toHours());}
    public static InvalidValueException expirationWindowTooLong(Duration max) {return new InvalidValueException(TIMING_EXPIRATION_TOO_LONG, max.toHours());}
    // Walk-in Block //
    public static InvalidValueException invalidWalkInBlockTime() {return new InvalidValueException(WALK_IN_BLOCK_TIME_INVALID);}
    public static InvalidValueException walkInBlockTooEarly(Duration max) {return new InvalidValueException(TIMING_WALK_IN_BLOCK_TOO_EARLY, max.toMinutes());}
    public static BusinessConflictException walkInBlockStatusNotAllowed(UUID id, String currentStatus) {return new BusinessConflictException(WALK_IN_BLOCK_STATUS_NOT_ALLOWED, id, currentStatus);}
    public static BusinessConflictException walkInBlockTooEarly(ReservationId id, Instant walkInBlockTime, Instant now) {return new BusinessConflictException(TIMING_WALK_IN_BLOCK_TOO_EARLY, id.value(), walkInBlockTime, now);}
}