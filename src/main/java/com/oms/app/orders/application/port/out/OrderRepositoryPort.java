package com.oms.app.orders.application.port.out;

import com.oms.app.orders.domain.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {
    Optional<Order> findById(UUID orderId);

    Optional<Order> findByIdForUpdate(UUID orderId);

    void save(Order order);
}
