package com.oms.app.orders.infra.adapters.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItemJpaEntity {

    @Id
    @Column(name = "order_item_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "notes", length = 500)
    private String notes;

    public OrderItemJpaEntity(
            UUID id,
            OrderJpaEntity order,
            UUID productId,
            BigDecimal unitPrice,
            int quantity,
            String notes
    ) {
        this.id = id;
        this.order = order;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.notes = notes;
    }
}