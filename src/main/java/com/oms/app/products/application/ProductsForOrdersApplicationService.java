package com.oms.app.products.application;

import com.oms.app.products.api.forOrders.OrderableProductSnapshot;
import com.oms.app.products.api.forOrders.ProductsForOrdersApi;
import com.oms.app.products.application.get_orderable_products.GetOrderableProductsHandler;
import com.oms.app.products.application.get_orderable_products.GetOrderableProductsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductsForOrdersApplicationService implements ProductsForOrdersApi {

    private final GetOrderableProductsHandler getOrderableProductsHandler;

    @Override
    public Map<UUID, OrderableProductSnapshot> getOrderableProducts(Set<UUID> productIds) {
        return getOrderableProductsHandler.handle(new GetOrderableProductsQuery(productIds));
    }
}

