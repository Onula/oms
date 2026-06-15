package com.oms.app.visits.domain.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.InvalidValueException;
import com.oms.app.visits.domain.VisitStatus;
import com.oms.app.visits.domain.vo.VisitId;

import java.util.UUID;

public final class VisitsDomainExceptions {

    // Business
    private static final ExceptionCode VISIT_NOT_ACTIVE = ExceptionCode.of("visits.not_active", "visitId");
    private static final ExceptionCode CANNOT_TRANSFER_VISIT_TO_SAME_TABLE = ExceptionCode.of("visits.cannot_transfer_to_same_table", "targetTableId");
    private static final ExceptionCode STATUS_TRANSITION_NOT_ALLOWED = ExceptionCode.of("visits.status_transition_not_allowed");

    // Required fields
    private static final ExceptionCode VISIT_ID_REQUIRED = ExceptionCode.of("visits.id.required", "visitId");
    private static final ExceptionCode VISIT_TABLE_ID_REQUIRED = ExceptionCode.of("visits.table_id.required", "tableId");
    private static final ExceptionCode TARGET_TABLE_ID_REQUIRED = ExceptionCode.of("visits.target_table_id.required", "targetTableId");
    private static final ExceptionCode VISIT_TYPE_REQUIRED = ExceptionCode.of("visits.type.required", "type");
    private static final ExceptionCode VISIT_STATUS_REQUIRED = ExceptionCode.of("visits.status.required", "status");
    private static final ExceptionCode VISIT_TIMING_REQUIRED = ExceptionCode.of("visits.timing.required", "timing");

    // Timing required fields
    private static final ExceptionCode OPENED_AT_REQUIRED = ExceptionCode.of("visits.opened_at.required", "openedAt");
    private static final ExceptionCode CLOSED_AT_REQUIRED = ExceptionCode.of("visits.closed_at.required", "closedAt");
    private static final ExceptionCode CANCELLED_AT_REQUIRED = ExceptionCode.of("visits.cancelled_at.required", "cancelledAt");

    // Timing validation
    private static final ExceptionCode VISIT_CANNOT_BE_CLOSED_AND_CANCELLED = ExceptionCode.of("visits.timing.cannot_be_closed_and_cancelled", "timing");
    private static final ExceptionCode INVALID_CLOSED_AT = ExceptionCode.of("visits.timing.closed_at.invalid", "closedAt");
    private static final ExceptionCode INVALID_CANCELLED_AT = ExceptionCode.of("visits.timing.cancelled_at.invalid", "cancelledAt");
    private static final ExceptionCode VISIT_ALREADY_ENDED = ExceptionCode.of("visits.timing.already_ended", "timing");

    // Status + timing consistency
    private static final ExceptionCode ACTIVE_VISIT_CANNOT_HAVE_END_TIME = ExceptionCode.of("visits.active.cannot_have_end_time", "status");
    private static final ExceptionCode CLOSED_VISIT_MUST_HAVE_CLOSED_AT = ExceptionCode.of("visits.closed.must_have_closed_at", "status");
    private static final ExceptionCode CANCELLED_VISIT_MUST_HAVE_CANCELLED_AT = ExceptionCode.of("visits.cancelled.must_have_cancelled_at", "status");

    private VisitsDomainExceptions() {}

    public static BusinessConflictException visitNotActive(VisitId visitId) { return new BusinessConflictException(VISIT_NOT_ACTIVE, visitId.value()); }
    public static BusinessConflictException cannotTransferVisitToSameTable(UUID targetTableId) { return new BusinessConflictException(CANNOT_TRANSFER_VISIT_TO_SAME_TABLE, targetTableId); }
    public static BusinessConflictException statusTransitionNotAllowed(VisitId visitId, VisitStatus currentStatus, VisitStatus nextStatus) { return new BusinessConflictException(STATUS_TRANSITION_NOT_ALLOWED, visitId.value(), currentStatus, nextStatus); }

    public static InvalidValueException visitIdRequired() { return new InvalidValueException(VISIT_ID_REQUIRED); }
    public static InvalidValueException visitTableIdRequired() { return new InvalidValueException(VISIT_TABLE_ID_REQUIRED); }
    public static InvalidValueException targetTableIdRequired() { return new InvalidValueException(TARGET_TABLE_ID_REQUIRED); }
    public static InvalidValueException visitTypeRequired() { return new InvalidValueException(VISIT_TYPE_REQUIRED); }
    public static InvalidValueException visitStatusRequired() { return new InvalidValueException(VISIT_STATUS_REQUIRED); }
    public static InvalidValueException visitTimingRequired() { return new InvalidValueException(VISIT_TIMING_REQUIRED); }

    public static InvalidValueException openedAtRequired() { return new InvalidValueException(OPENED_AT_REQUIRED); }
    public static InvalidValueException closedAtRequired() { return new InvalidValueException(CLOSED_AT_REQUIRED); }
    public static InvalidValueException cancelledAtRequired() { return new InvalidValueException(CANCELLED_AT_REQUIRED); }

    public static InvalidValueException visitCannotBeClosedAndCancelled() { return new InvalidValueException(VISIT_CANNOT_BE_CLOSED_AND_CANCELLED); }
    public static InvalidValueException invalidClosedAt() { return new InvalidValueException(INVALID_CLOSED_AT); }
    public static InvalidValueException invalidCancelledAt() { return new InvalidValueException(INVALID_CANCELLED_AT); }
    public static InvalidValueException visitAlreadyEnded() { return new InvalidValueException(VISIT_ALREADY_ENDED); }

    public static InvalidValueException activeVisitCannotHaveEndTime() { return new InvalidValueException(ACTIVE_VISIT_CANNOT_HAVE_END_TIME); }
    public static InvalidValueException closedVisitMustHaveClosedAt() { return new InvalidValueException(CLOSED_VISIT_MUST_HAVE_CLOSED_AT); }
    public static InvalidValueException cancelledVisitMustHaveCancelledAt() { return new InvalidValueException(CANCELLED_VISIT_MUST_HAVE_CANCELLED_AT); }
}