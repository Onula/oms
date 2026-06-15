package com.oms.app.common.exception.rest;

import com.oms.app.common.exception.BaseException;
import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.InvalidValueException;
import com.oms.app.common.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * Handles all module/domain/application exceptions from modules that extend BaseException
 * and translates them to HTTP ProblemDetail responses.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
class BaseExceptionHandler {

    private final ExceptionHandlingHelper helper;

    @ExceptionHandler(BaseException.class)
    ProblemDetail handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ) {
        String code = ex.errorCode().code();
        String message = helper.message(code, ex.args(), code);

        ProblemDetail problem = helper.problem(
                statusOf(ex),
                code,
                message,
                request
        );

        if (ex.hasField()) {
            problem.setProperty("fieldErrors", List.of(
                    helper.violation(ex.field(), code, message)
            ));
        }

        return problem;
    }

    private HttpStatus statusOf(BaseException ex) {
        if (ex instanceof InvalidValueException) {
            return HttpStatus.UNPROCESSABLE_CONTENT;
        }

        if (ex instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        }

        if (ex instanceof BusinessConflictException) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}