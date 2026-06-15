package com.oms.app.visits.application.exception;

import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.ResourceNotFoundException;

import java.util.UUID;

public final class VisitsApplicationExceptions {

    private static final ExceptionCode VISIT_NOT_FOUND = ExceptionCode.of("visits.not_found", "visitId");

    private VisitsApplicationExceptions() {}

    public static ResourceNotFoundException visitNotFound(UUID visitId) {
        return new ResourceNotFoundException(
                VISIT_NOT_FOUND,
                visitId
        );
    }
}