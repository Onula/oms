package com.oms.app.products.domain;

import com.oms.app.products.domain.vo.ProductId;
import com.oms.app.products.domain.vo.ProductName;
import com.oms.app.products.domain.vo.ProductPrice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

    @EmbeddedId
    @AttributeOverride( name = "value", column = @Column(name = "product_id", nullable = false, updatable = false) )
    private ProductId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false, unique = true, length = 120, updatable = false))
    private ProductName name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false, precision = 10, scale = 2))
    private ProductPrice price;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ProductStatus status;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    // ===== CONSTRUCTOR =====
    private Product(ProductId id, ProductName name, ProductPrice price, ProductCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = Objects.requireNonNull(category, "product category cannot be null");
        this.status = ProductStatus.AVAILABLE;
    }

    // ===== FACTORY =====
    public static Product create(ProductName name, ProductPrice price, ProductCategory category) {
        return new Product(
                ProductId.generate(),
                name,
                price,
                category
        );
    }

    // ===== BUSINESS METHODS =====

    public boolean isAvailable() {
        return this.status == ProductStatus.AVAILABLE;
    }

}
