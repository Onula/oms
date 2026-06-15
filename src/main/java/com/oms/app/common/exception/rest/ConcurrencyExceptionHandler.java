package com.oms.app.common.exception.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Handles concurrency conflicts caused by optimistic locking failures,
 * usually when multiple transactions modify the same versioned entity.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
class ConcurrencyExceptionHandler {

    private static final String CONCURRENT_MODIFICATION = "request.concurrent_modification";

    private final ExceptionHandlingHelper helper;

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    ProblemDetail handleOptimisticLockingFailure(
            ObjectOptimisticLockingFailureException ex,
            HttpServletRequest request
    ) {
        String message = helper.message(
                CONCURRENT_MODIFICATION,
                null,
                "The resource was modified by another transaction. Please reload and try again."
        );

        ProblemDetail problem = helper.problem(
                HttpStatus.CONFLICT,
                CONCURRENT_MODIFICATION,
                message,
                request
        );

        if (ex.getPersistentClassName() != null) {
            problem.setProperty("resource", ex.getPersistentClassName());
        }

        if (ex.getIdentifier() != null) {
            problem.setProperty("resourceId", ex.getIdentifier());
        }

        return problem;
    }
}