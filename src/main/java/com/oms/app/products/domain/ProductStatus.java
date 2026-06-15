package com.oms.app.products.domain;

public enum ProductStatus {
    AVAILABLE,
    UNAVAILABLE,
    DELETED;

    public boolean canTransitionTo(ProductStatus next) {
        return switch (this) {
            case AVAILABLE -> next == UNAVAILABLE || next == DELETED;
            case UNAVAILABLE ->  next == AVAILABLE || next == DELETED;
            case DELETED -> next == AVAILABLE || next == UNAVAILABLE;
        };
    }
}
