package com.oms.app.orders.infra.adapters.out.persistence;

import com.oms.app.orders.domain.Order;
import com.oms.app.orders.domain.OrderItem;
import com.oms.app.orders.domain.vo.*;
import com.oms.app.orders.infra.adapters.out.persistence.jpa.OrderItemJpaEntity;
import com.oms.app.orders.infra.adapters.out.persistence.jpa.OrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityMapper {

    public Order toDomain(OrderJpaEntity j) {
        return Order.reconstitute(
                OrderId.of(j.getId()),
                j.getVisitId(),
                j.getStatus(),
                j.getCreatedAt(),
                j.getCreatedBy(),
                j.getSubmittedAt(),
                j.getSubmittedBy(),
                j.getPreparedAt(),
                j.getPreparedBy(),
                j.getReadyAt(),
                j.getCancelledAt(),
                j.getCancelledBy(),
                OrderNotes.of(j.getNote()),
                OrderItems.of(j.getItems().stream().map(this::toItemDomain).toList()),
                j.getVersion()
        );
    }

    public OrderJpaEntity toJpa(Order d) {
        OrderJpaEntity entity = new OrderJpaEntity(
                d.getId().value(),
                d.getVisitId(),
                d.getStatus(),
                d.getCreatedAt(),
                d.getPreparedAt(),
                d.getPreparedBy(),
                d.getReadyAt(),
                d.getCancelledAt(),
                d.getCancelledBy(),
                d.getNotes() != null ? d.getNotes().value() : null,
                d.getVersion()
        );

        d.getItems().values().stream()  // ← .values() εδώ
                .map(item -> toItemJpa(item, entity))
                .forEach(entity.getItems()::add);

        return entity;
    }

    private OrderItem toItemDomain(OrderItemJpaEntity j) {
        return OrderItem.reconstitute(
                OrderItemId.of(j.getId()),
                j.getProductId(),
                j.getUnitPrice(),
                j.getQuantity(),
                OrderItemNotes.of(j.getNotes())
        );
    }

    private OrderItemJpaEntity toItemJpa(OrderItem item, OrderJpaEntity order) {
        return new OrderItemJpaEntity(
                item.getId().value(),
                order,
                item.getProductId(),
                item.getUnitPrice(),
                item.getQuantity(),
                item.getNotes() != null ? item.getNotes().value() : null
        );
    }
}