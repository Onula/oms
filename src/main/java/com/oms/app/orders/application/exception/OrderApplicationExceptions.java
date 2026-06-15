package com.oms.app.orders.application.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.ResourceNotFoundException;

import java.util.UUID;

public final class OrderApplicationExceptions {
    private OrderApplicationExceptions() {}


    private static final ExceptionCode ORDER_NOT_FOUND = ExceptionCode.of("orders.not_found");
    private static final ExceptionCode CANNOT_CANCEL_FOR_INACTIVE_VISIT = ExceptionCode.of("orders.cannot_cancel_for_inactive_visit");

    public static ResourceNotFoundException orderNotFound(UUID id) {
        return new ResourceNotFoundException(
                ORDER_NOT_FOUND,
                id
        );
    }

    public static BusinessConflictException cannotCancelOrderForInactiveVisit(UUID visitId) {
        return new BusinessConflictException(
                CANNOT_CANCEL_FOR_INACTIVE_VISIT,
                visitId
        );
    }
}
