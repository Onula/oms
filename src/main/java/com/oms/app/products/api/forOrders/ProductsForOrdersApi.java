package com.oms.app.products.api.forOrders;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ProductsForOrdersApi {
    Map<UUID, OrderableProductSnapshot> getOrderableProducts(Set<UUID> productIds);
}
