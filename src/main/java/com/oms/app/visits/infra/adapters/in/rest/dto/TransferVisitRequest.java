package com.oms.app.visits.infra.adapters.in.rest.dto;

import java.util.Objects;
import java.util.UUID;

public record TransferVisitRequest(
        UUID targetTableId
) {
    public TransferVisitRequest {
        Objects.requireNonNull(targetTableId, "targetTableId must not be null");
    }
}
