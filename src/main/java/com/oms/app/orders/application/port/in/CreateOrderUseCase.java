package com.oms.app.orders.application.port.in;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface CreateOrderUseCase {

    Result handle(Command cmd);

    record Command(
            UUID visitId,
            List<UUID> productIds,
            String notes
    ) {
        public Command {
            Objects.requireNonNull(visitId, "visitId must not be null");
            Objects.requireNonNull(productIds, "productIds must not be null");
        }

        public static Command of(UUID visitId, List<UUID> productIds, String notes) {
            return new Command(visitId, productIds, notes);
        }
    }

    record Result(UUID orderId) {
        public static Result of(UUID orderId) {
            return new Result(orderId);
        }
    }
}