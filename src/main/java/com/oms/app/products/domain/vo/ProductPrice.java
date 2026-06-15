package com.oms.app.products.domain.vo;

import jakarta.persistence.Embeddable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
public record ProductPrice(BigDecimal value) {

    public ProductPrice {
        Objects.requireNonNull(value, "productPrice value must not be null");

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Product price cannot be negative.");
        }

        // normalize scale (2 decimal places)
        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static ProductPrice of(BigDecimal value) {
        return new ProductPrice(value);
    }

    public static ProductPrice of(double value) {
        return new ProductPrice(BigDecimal.valueOf(value));
    }

}
