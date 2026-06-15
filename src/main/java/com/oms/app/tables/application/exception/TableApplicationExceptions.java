package com.oms.app.tables.application.exception;

import com.oms.app.common.exception.BusinessConflictException;
import com.oms.app.common.exception.ExceptionCode;
import com.oms.app.common.exception.ResourceNotFoundException;

import java.util.UUID;

public final class TableApplicationExceptions {

    private static final ExceptionCode TABLE_NOT_FOUND                 = ExceptionCode.of("tables.not_found",              "tableId");
    private static final ExceptionCode CANNOT_TRANSFER_TO_SAME_TABLE   = ExceptionCode.of("tables.transfer.same_table",    "tableId");

    private TableApplicationExceptions() {
    }

    public static ResourceNotFoundException tableNotFound(UUID tableId) {
        return new ResourceNotFoundException(TABLE_NOT_FOUND, tableId);
    }

    public static BusinessConflictException cannotTransferToSameTable(UUID tableId) {
        return new BusinessConflictException(CANNOT_TRANSFER_TO_SAME_TABLE, tableId);
    }
}