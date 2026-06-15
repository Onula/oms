package com.oms.app.tables.domain.vo;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;


@Embeddable
public record TableId(UUID value) implements Serializable {

    public TableId {
        Objects.requireNonNull(value, "tableId must not be null");
    }

    public static TableId generate() {
        return new TableId(UUID.randomUUID());
    }

    public static TableId of(UUID value) {
        return new TableId(value);
    }
}