package com.oms.app.orders.domain;

public enum OrderStatus {
    SUBMITTED,
    IN_PREPARATION,
    READY,
    SERVED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus next) {
        return switch (this) {
            case SUBMITTED       -> next == IN_PREPARATION || next == CANCELLED;
            case IN_PREPARATION  -> next == READY || next == CANCELLED;
            case READY           -> next == SERVED || next == CANCELLED;
            case SERVED          -> false;  // final state
            case CANCELLED       -> false;  // final state
        };
    }
}