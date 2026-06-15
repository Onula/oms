package com.oms.app.orders.domain;

import com.oms.app.orders.domain.vo.OrderId;
import com.oms.app.orders.domain.vo.OrderItems;
import com.oms.app.orders.domain.vo.OrderNotes;
import jakarta.persistence.Version;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static com.oms.app.orders.domain.exception.OrderDomainExceptions.*;

@Getter
public class Order {

    private final OrderId id;
    private final UUID visitId;
    private OrderStatus status;

    private final Instant createdAt;

    private Instant preparedAt;
    private UUID preparedBy;

    private Instant readyAt;
    private Instant cancelledAt;
    private UUID cancelledBy;

    private final OrderNotes notes;
    private final OrderItems items;

    @Version
    private Long version;

    private Order(OrderId id, UUID visitId, Instant createdAt, OrderNotes notes, OrderItems items) {
        this.id = id;
        this.visitId = Objects.requireNonNull(visitId, "visitId cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
        this.status = OrderStatus.SUBMITTED;
        this.notes = notes;
        this.items = items;
    }

    public static Order create(
            UUID visitId,
            List<UUID> productIds,
            Map<String, BigDecimal> productPrices,
            String notesStr,
            Instant now
    ) {
        List<OrderItem> orderItems = productIds.stream()
                .map(productId -> OrderItem.create(
                        productId,
                        productPrices.get(productId.toString()),
                        1,  // default quantity
                        null
                ))
                .toList();

        OrderItems items = OrderItems.of(orderItems);
        OrderNotes notes = OrderNotes.of(notesStr);

        return new Order(
                OrderId.generate(),
                visitId,
                now,
                notes,
                items
        );
    }

    // ===== TRANSITIONS =====

    public void cancel(UUID cancelledBy) {
        Objects.requireNonNull(cancelledBy, "cancelledBy cannot be null");
        transitionTo(OrderStatus.CANCELLED);
        this.cancelledAt = Instant.now();
        this.cancelledBy = cancelledBy;
    }

    public void startPreparation(UUID preparedBy) {
        Objects.requireNonNull(preparedBy, "preparedBy cannot be null");
        transitionTo(OrderStatus.IN_PREPARATION);
        this.preparedAt = Instant.now();
        this.preparedBy = preparedBy;
    }

    public void markReady() {
        transitionTo(OrderStatus.READY);
        this.readyAt = Instant.now();
    }

    private void transitionTo(OrderStatus next) {
        Objects.requireNonNull(next, "next status must not be null");
        if (!status.canTransitionTo(next)) {
            throw statusTransitionNotAllowed(id, status, next);
        }
        this.status = next;
    }

    public static Order reconstitute(
            OrderId id,
            UUID visitId,
            OrderStatus status,
            Instant createdAt,
            UUID createdBy,
            Instant submittedAt,
            UUID submittedBy,
            Instant preparedAt,
            UUID preparedBy,
            Instant readyAt,
            Instant cancelledAt,
            UUID cancelledBy,
            OrderNotes notes,
            OrderItems items,
            Long version
    ) {
        Order order = new Order(id, visitId, createdAt, notes, items);
        order.status = status;
        order.preparedAt = preparedAt;
        order.preparedBy = preparedBy;
        order.readyAt = readyAt;
        order.cancelledAt = cancelledAt;
        order.cancelledBy = cancelledBy;
        order.version = version;
        return order;
    }
}