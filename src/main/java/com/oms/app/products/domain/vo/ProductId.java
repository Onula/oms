package com.oms.app.products.domain.vo;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public record ProductId(UUID value) implements Serializable {
    public ProductId {
        Objects.requireNonNull(value, "productId must not be null");
    }
    public static ProductId generate() { return new ProductId(UUID.randomUUID()); }

    public static ProductId of(UUID value) { return new ProductId(value); }
}