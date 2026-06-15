package com.oms.app.orders.domain.vo;

import static com.oms.app.orders.domain.exception.OrderDomainExceptions.invalidOrderNotes;

public record OrderNotes(String value) {

    public static final int MAX_LENGTH = 500;

    public OrderNotes {
        if (value == null || value.isBlank()) {
            value = null;
        } else {
            value = value.trim();
            if (value.length() > MAX_LENGTH) {
                throw invalidOrderNotes(MAX_LENGTH);
            }
        }
    }

    public static OrderNotes of(String value) {
        return new OrderNotes(value);
    }

    public static OrderNotes empty() {
        return new OrderNotes(null);
    }
}
