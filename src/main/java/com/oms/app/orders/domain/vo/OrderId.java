package com.oms.app.orders.domain.vo;

import java.io.Serializable;
import java.util.UUID;

import static com.oms.app.orders.domain.exception.OrderDomainExceptions.orderIdRequired;


public record OrderId(UUID value) implements Serializable {

    public OrderId {
        if(value == null){
            throw orderIdRequired();
        }
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId of(UUID value) {
        return new OrderId(value);
    }
}