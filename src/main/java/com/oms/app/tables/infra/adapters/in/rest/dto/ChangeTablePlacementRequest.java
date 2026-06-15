package com.oms.app.tables.infra.adapters.in.rest.dto;

import com.oms.app.tables.application.port.in.command.ChangeTablePlacementUseCasePort;

import java.util.UUID;

public record ChangeTablePlacementRequest(
        int x,
        int y,
        int width,
        int height
) {

    public ChangeTablePlacementUseCasePort.Command toCommand(UUID tableId) {
        return ChangeTablePlacementUseCasePort.Command.of(
                tableId,x,y,width,height
        );
    }

}
