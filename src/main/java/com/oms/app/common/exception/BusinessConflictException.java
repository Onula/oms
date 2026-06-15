package com.oms.app.common.exception;

public class BusinessConflictException extends BaseException {
    public BusinessConflictException(BaseExceptionCode errorCode) {
        super(errorCode);
    }
    public BusinessConflictException(BaseExceptionCode errorCode, Object... args) {
        super(errorCode, args);
    }
}