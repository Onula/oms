package com.oms.app.common.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(BaseExceptionCode errorCode) {
        super(errorCode);
    }
    public ResourceNotFoundException(BaseExceptionCode errorCode, Object... args) {
        super(errorCode, args);
    }
}