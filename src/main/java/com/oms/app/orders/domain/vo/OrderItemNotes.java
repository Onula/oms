package com.oms.app.orders.domain.vo;

import static com.oms.app.orders.domain.exception.OrderDomainExceptions.invalidOrderItemNotes;

public record OrderItemNotes(String value) {

    public static final int MAX_LENGTH = 500;

    public OrderItemNotes {
        if (value == null || value.isBlank()) {
            value = null;
        } else {
            value = value.trim();
            if (value.length() > MAX_LENGTH) {
                throw invalidOrderItemNotes(MAX_LENGTH);
            }
        }
    }

    public static OrderItemNotes of(String value) {
        return new OrderItemNotes(value);
    }

    public static OrderItemNotes empty() {
        return new OrderItemNotes(null);
    }
}
