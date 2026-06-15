package com.oms.app.visits.infra.adapters.in.rest.dto;

import com.oms.app.visits.domain.VisitType;

import java.util.Objects;
import java.util.UUID;

public record OpenVisitRequest(
        UUID tableId,
        VisitType type
) {
    public OpenVisitRequest {
        Objects.requireNonNull(tableId, "tableId must not be null");
        Objects.requireNonNull(type, "type must not be null");
    }
}
