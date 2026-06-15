package com.oms.app.tables.infra.adapters.in.rest.dto;

import com.oms.app.tables.application.port.in.command.CreateTableUseCasePort;

import java.util.UUID;

public record CreateTableResponse(
        UUID tableId
) {
    public static CreateTableResponse from(CreateTableUseCasePort.Result result) {
        return new CreateTableResponse(result.tableId());
    }
}
