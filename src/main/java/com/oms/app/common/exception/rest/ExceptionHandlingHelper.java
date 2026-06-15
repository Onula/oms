package com.oms.app.common.exception.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;

import java.net.URI;
import java.util.List;

/**
 * Shared helper for building ProblemDetail responses, resolving localized messages,
 * and mapping validation errors to API field violation objects.
 */
@Component
@RequiredArgsConstructor
class ExceptionHandlingHelper {

    private final MessageSource messageSource;

    record FieldViolation(
            String field,
            String code,
            String message
    ) {
    }


    ProblemDetail problem(
            HttpStatus status,
            String code,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(status.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("code", code);
        return problem;
    }

    FieldViolation violationOf(FieldError error) {
        return new FieldViolation(
                error.getField(),
                codeOf(error),
                messageOf(error)
        );
    }

    List<FieldViolation> violationsOf(ParameterValidationResult result) {
        String parameterName = parameterNameOf(result);

        return result.getResolvableErrors()
                .stream()
                .map(error -> new FieldViolation(
                        fieldNameOf(error, parameterName),
                        codeOf(error),
                        messageOf(error)
                ))
                .toList();
    }

    FieldViolation violation(
            String field,
            String code,
            String message
    ) {
        return new FieldViolation(field, code, message);
    }

    String message(String code, Object[] args, String fallback) {
        return messageSource.getMessage(
                code,
                args,
                fallback,
                LocaleContextHolder.getLocale()
        );
    }

    private String fieldNameOf(MessageSourceResolvable error, String fallback) {
        return error instanceof FieldError fieldError
                ? fieldError.getField()
                : fallback;
    }

    private String parameterNameOf(ParameterValidationResult result) {
        String parameterName = result.getMethodParameter().getParameterName();

        return parameterName == null || parameterName.isBlank()
                ? "arg" + result.getMethodParameter().getParameterIndex()
                : parameterName;
    }

    private String codeOf(MessageSourceResolvable resolvable) {
        String[] codes = resolvable.getCodes();

        return codes == null || codes.length == 0
                ? "Invalid"
                : codes[codes.length - 1];
    }

    private String messageOf(MessageSourceResolvable resolvable) {
        return messageSource.getMessage(
                resolvable,
                LocaleContextHolder.getLocale()
        );
    }
}
