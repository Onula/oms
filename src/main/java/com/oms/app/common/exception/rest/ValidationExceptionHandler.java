package com.oms.app.common.exception.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

/**
 * Handles request validation errors produced by Spring MVC,
 * including @Valid request bodies and validated controller method parameters.
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
class ValidationExceptionHandler {

    private static final String REQUEST_VALIDATION_FAILED = "request.validation.failed";

    private final ExceptionHandlingHelper helper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ExceptionHandlingHelper.FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(helper::violationOf)
                .toList();

        return validationProblem(request, violations);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    ProblemDetail handleHandlerMethodValidation(
            HandlerMethodValidationException ex,
            HttpServletRequest request
    ) {
        List<ExceptionHandlingHelper.FieldViolation> violations = ex.getParameterValidationResults()
                .stream()
                .flatMap(result -> helper.violationsOf(result).stream())
                .toList();

        return validationProblem(request, violations);
    }

    private ProblemDetail validationProblem(
            HttpServletRequest request,
            List<ExceptionHandlingHelper.FieldViolation> violations
    ) {
        String message = helper.message(
                REQUEST_VALIDATION_FAILED,
                null,
                "Request validation failed"
        );

        ProblemDetail problem = helper.problem(
                HttpStatus.BAD_REQUEST,
                REQUEST_VALIDATION_FAILED,
                message,
                request
        );

        problem.setProperty("fieldErrors", violations);

        return problem;
    }
}
