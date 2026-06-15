package com.oms.app.orders.infra.adapters.in.rest.dto.cancel_order;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CancelOrderRequest(
        @NotNull
        UUID orderId
) {
}
