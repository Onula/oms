package com.oms.app.orders.domain.vo;

import java.io.Serializable;
import java.util.UUID;

import static com.oms.app.orders.domain.exception.OrderDomainExceptions.orderItemIdRequired;

public record OrderItemId(UUID value) implements Serializable {

    public OrderItemId {
        if(value==null) {
            throw orderItemIdRequired();
        }
    }

    public static OrderItemId generate() {
        return new OrderItemId(UUID.randomUUID());
    }

    public static OrderItemId of(UUID value) {
        return new OrderItemId(value);
    }
}