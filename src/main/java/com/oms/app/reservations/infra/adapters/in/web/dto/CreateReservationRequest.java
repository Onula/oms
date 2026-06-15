package com.oms.app.reservations.infra.adapters.in.web.dto;

import com.oms.app.reservations.application.port.in.command.CreateReservationUseCasePort;

import java.time.Instant;
import java.util.UUID;

public record CreateReservationRequest(
        UUID tableId,
        String customerName,
        String customerPhone,
        Integer partySize,
        Instant startTime,
        Instant endTime,
        Instant expirationTime,
        Instant walkInBlockTime,
        String notes
) {

    public CreateReservationUseCasePort.Command toCommand() {
        return CreateReservationUseCasePort.Command.of(
                tableId,
                customerName,
                customerPhone,
                partySize,
                startTime,
                endTime,
                expirationTime,
                walkInBlockTime,
                notes
        );
    }
}
