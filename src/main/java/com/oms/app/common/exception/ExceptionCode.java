package com.oms.app.common.exception;

import java.util.Optional;

public record ExceptionCode(
        String code,
        String field
) implements BaseExceptionCode {

    public ExceptionCode(String code) {
        this(code, null);
    }

    public ExceptionCode {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Error code must not be blank");
        }

        code = code.strip();
        field = field == null || field.isBlank() ? null : field.strip();
    }

    public static ExceptionCode of(String code) {
        return new ExceptionCode(code);
    }

    public static ExceptionCode of(String code, String field) {
        return new ExceptionCode(code, field);
    }

    @Override
    public Optional<String> defaultField() {
        return Optional.ofNullable(field);
    }
}
