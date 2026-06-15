package com.oms.app.ui.common.websocket.application;

import java.util.Objects;

public record WebSocketDestination(String value) {

    public WebSocketDestination {
        Objects.requireNonNull(value, "destination is required");

        if (value.isBlank()) {
            throw new IllegalArgumentException("destination must not be blank");
        }
    }

    public static WebSocketDestination topic(String name) {
        Objects.requireNonNull(name, "topic name is required");

        if (name.isBlank()) {
            throw new IllegalArgumentException("topic name must not be blank");
        }

        String normalizedName = name.startsWith("/")
                ? name.substring(1)
                : name;

        return new WebSocketDestination("/topic/" + normalizedName);
    }
}


