package com.oms.app.products.application.get_orderable_products;

import com.oms.app.products.api.forOrders.OrderableProductSnapshot;
import com.oms.app.products.domain.Product;
import com.oms.app.products.domain.vo.ProductId;
import com.oms.app.products.infrastructure.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetOrderableProductsHandler {

    private final ProductRepository productRepository;

    public Map<UUID, OrderableProductSnapshot> handle(GetOrderableProductsQuery query) {
        Objects.requireNonNull(query, "query must not be null");

        if (query.productIds().isEmpty()) { return Map.of(); }

        Set<ProductId> productIds = query.productIds().stream()
                .map(ProductId::of)
                .collect(Collectors.toSet());

        return productRepository.findAllById(productIds).stream()
                .filter(Product::isAvailable)
                .collect(Collectors.toMap(
                        p -> p.getId().value(),
                        this::toSnapshot
                ));
    }

    private OrderableProductSnapshot toSnapshot(Product product) {
        return new OrderableProductSnapshot(
                product.getId().value(),
                product.getName().value(),
                product.getPrice().value(),
                product.getCategory().name()
        );
    }
}
