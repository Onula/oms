package com.oms.app.products.application.get_orderable_products;


import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record GetOrderableProductsQuery(
        Set<UUID> productIds
) {
    public GetOrderableProductsQuery {
        Objects.requireNonNull(productIds, "productIds must not be null");
        productIds = Set.copyOf(productIds);
    }
}
