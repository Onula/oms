package com.oms.app.products.api.forOrders;


import java.math.BigDecimal;
import java.util.UUID;

public record OrderableProductSnapshot(
        UUID productId,
        String name,
        BigDecimal unitPrice,
        String category
) {}
