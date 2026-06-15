package com.oms.app.orders.domain.vo;

import com.oms.app.orders.domain.OrderItem;
import com.oms.app.orders.domain.exception.OrderDomainExceptions;

import java.util.List;

public final class OrderItems {

    private final List<OrderItem> items;

    private OrderItems(List<OrderItem> items) {
        this.items = List.copyOf(items);
    }

    public static OrderItems of(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw OrderDomainExceptions.mustContainAtLeastOneItem();
        }
        return new OrderItems(items);
    }

    public List<OrderItem> values() {
        return items;
    }
}
