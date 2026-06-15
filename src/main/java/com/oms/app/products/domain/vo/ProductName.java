package com.oms.app.products.domain.vo;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public record ProductName(String value) {

    public ProductName {
        Objects.requireNonNull(value, "productName must not be null");

        String normalized = value.trim();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be blank.");
        }

        if (normalized.length() > 120) {
            throw new IllegalArgumentException("Product name cannot exceed 120 characters.");
        }

        value = normalized;
    }

    public static ProductName of(String value) { return new ProductName(value); }
}
