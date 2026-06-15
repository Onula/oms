package com.oms.app.products.application.create_product;


import com.oms.app.products.domain.ProductCategory;
import com.oms.app.products.domain.vo.ProductName;
import com.oms.app.products.domain.vo.ProductPrice;

import java.util.Objects;

public record CreateProductCommand(
        ProductName name,
        ProductPrice price,
        ProductCategory category
) {
    public CreateProductCommand {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(price, "price must not be null");
        Objects.requireNonNull(category, "category must not be null");
    }
}
