package com.oms.app.orders.application.port.in;

import com.oms.app.orders.domain.vo.OrderId;

import java.util.Objects;
import java.util.UUID;

public interface CancelOrderUseCase {

    void handle(Command cmd);

    record Command(OrderId orderId, UUID cancelledBy) {
        public Command {
            Objects.requireNonNull(orderId, "orderId must not be null");
            Objects.requireNonNull(cancelledBy, "cancelledBy must not be null");
        }

        public static Command of(OrderId orderId, UUID cancelledBy) {
            return new Command(orderId, cancelledBy);
        }
    }
}