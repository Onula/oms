package com.oms.app.ui.tables_board.application.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record TablesBoardActiveVisitInfo(
        UUID visitId,
        String visitType,
        Instant openedAt
) {

    public TablesBoardActiveVisitInfo {
        Objects.requireNonNull(visitId, "visit id must not be null");
        Objects.requireNonNull(visitType, "visit type must not be null");
        Objects.requireNonNull(openedAt, "opened at must not be null");
    }
}