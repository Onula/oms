package com.oms.app.common.exception;

import java.util.Optional;

public interface BaseExceptionCode {
    String code();
    default Optional<String> defaultField() { return Optional.empty(); }
}
