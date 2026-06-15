package com.oms.app.tables.infra.adapters.in.rest.dto;

import com.oms.app.tables.application.port.in.command.ChangeTableCapacityUseCasePort;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChangeTableCapacityRequest(

        @NotNull(message = "capacity is required")
        @Min(value = 1, message = "capacity must be greater than 0")
        Integer capacity
) {

    public ChangeTableCapacityUseCasePort.Command toCommand(UUID tableId) {
        return ChangeTableCapacityUseCasePort.Command.of(
                tableId,
                capacity
        );
    }
}
