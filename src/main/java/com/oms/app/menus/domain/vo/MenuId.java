package com.oms.app.menus.domain.vo;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public record MenuId( UUID value ) implements Serializable {

    public MenuId{
        Objects.requireNonNull(value, "MenuId must not be null");
    }

    public static MenuId generate() {
        return new MenuId(UUID.randomUUID());
    }

    public static MenuId of(UUID value) {
        return new MenuId(value);
    }
}
