package com.oms.app.common.exception;

import java.util.Objects;

public class BaseException extends RuntimeException {

    private final BaseExceptionCode errorCode;
    private final String field;
    private final Object[] args;

    protected BaseException(BaseExceptionCode errorCode, Object... args) {
        super(errorCode.code());  // fallback message =  key
        this.errorCode = Objects.requireNonNull(errorCode);
        this.field = errorCode.defaultField().orElse(null);
        this.args = args == null ? new Object[0] : args;
    }

    protected BaseException(BaseExceptionCode errorCode, String field, Object... args) {
        super(errorCode.code());
        this.errorCode = Objects.requireNonNull(errorCode);
        this.field = field;
        this.args = args == null ? new Object[0] : args;
    }

    public BaseExceptionCode errorCode() { return errorCode; }
    public String field() { return field; }
    public Object[] args() { return args; }
    public boolean hasField() { return field != null && !field.isBlank(); }
}
