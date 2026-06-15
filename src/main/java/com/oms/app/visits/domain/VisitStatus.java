package com.oms.app.visits.domain;

public enum VisitStatus {

    ACTIVE,
    CLOSED,
    CANCELLED;

    public boolean canTransitionTo(VisitStatus next) {
        return switch (this) {
            case ACTIVE -> next == CLOSED || next == CANCELLED;
            case CLOSED, CANCELLED -> false;
        };
    }
}