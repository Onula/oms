package com.oms.app.reservations.infra.adapters.in.web.dto;

import com.oms.app.reservations.application.port.in.command.CreateReservationUseCasePort;

import java.util.UUID;

public record CreateReservationResponse(
        UUID reservationId
) {
    public CreateReservationResponse {
    }

    public static CreateReservationResponse of(CreateReservationUseCasePort.Result result) {
        return new CreateReservationResponse(result.reservationId());
    }

}
