package com.oms.app.common.exception;

public class InvalidValueException extends BaseException {

    public InvalidValueException(BaseExceptionCode errorCode) {
        super(errorCode);
    }

    public InvalidValueException(BaseExceptionCode errorCode, Object... args) {
        super(errorCode, args);
    }

}
