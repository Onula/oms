package com.oms.app.orders.domain;

import com.oms.app.orders.domain.exception.OrderDomainExceptions;
import com.oms.app.orders.domain.vo.OrderItemId;
import com.oms.app.orders.domain.vo.OrderItemNotes;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
public class OrderItem {

    private final OrderItemId id;
    private final UUID productId;
    private final BigDecimal unitPrice;
    private int quantity;
    private final OrderItemNotes notes;

    private OrderItem(OrderItemId id, UUID productId, BigDecimal unitPrice, int quantity, OrderItemNotes notes) {
        this.id = id;
        this.productId = Objects.requireNonNull(productId, "productId must not be null");
        this.unitPrice = Objects.requireNonNull(unitPrice, "unitPrice must not be null");
        this.quantity = quantity;
        this.notes = notes;
    }

    public static OrderItem create(UUID productId, BigDecimal unitPrice, int quantity, String notesStr) {
        return new OrderItem(
                OrderItemId.generate(),
                productId,
                unitPrice,
                quantity,
                OrderItemNotes.of(notesStr)
        );
    }

    public static OrderItem reconstitute(
            OrderItemId id,
            UUID productId,
            BigDecimal unitPrice,
            int quantity,
            OrderItemNotes notes
    ) {
        return new OrderItem(id, productId, unitPrice, quantity, notes);
    }

    public boolean hasSameProduct(UUID productId) {
        return this.productId.equals(productId);
    }

    public boolean hasSameNotes(OrderItemNotes notes) {
        return Objects.equals(this.notes, notes);
    }

    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw OrderDomainExceptions.quantityMustBePositive(amount);
        }
        this.quantity += amount;
    }
}