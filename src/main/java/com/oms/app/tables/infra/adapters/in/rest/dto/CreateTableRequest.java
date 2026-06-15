package com.oms.app.tables.infra.adapters.in.rest.dto;

import com.oms.app.tables.application.port.in.command.CreateTableUseCasePort;

public record CreateTableRequest(
        String code,
        int capacity,
        int x,
        int y,
        int width,
        int height

) {

    public CreateTableUseCasePort.Command toCommand() {
        return CreateTableUseCasePort.Command.of(
                code,
                capacity,
                x,y,width,height);
    }
}
