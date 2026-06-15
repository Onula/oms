package com.oms.app.visits.infra.adapters.in.rest.dto;

import com.oms.app.visits.application.port.in.command.OpenVisitUseCasePort;

import java.util.UUID;

public record OpenVisitResponse(
        UUID visitId
) {
    public static OpenVisitResponse from(OpenVisitUseCasePort.Result result) {
        return new OpenVisitResponse(result.visitId());
    }
}
