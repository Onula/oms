package com.oms.app.orders.application.service.command;

import com.oms.app.orders.application.port.in.CreateOrderUseCase;
import com.oms.app.orders.application.port.out.CatalogsPort;
import com.oms.app.orders.application.port.out.InventoryPort;
import com.oms.app.orders.application.port.out.OrderRepositoryPort;
import com.oms.app.orders.application.port.out.VisitsPort;
import com.oms.app.orders.domain.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateOrderService implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final VisitsPort visitsPort;
    private final CatalogsPort catalogsPort;
    private final InventoryPort inventoryPort;
    private final Clock clock;

    @Override
    public Result handle(Command cmd) {
        Objects.requireNonNull(cmd, "CreateOrderCommand must not be null");

        log.info("[CreateOrder] visitId={} productIds={}", cmd.visitId(), cmd.productIds());

        Instant now = Instant.now(clock);

        // Ensure visit can accept order
        boolean isVisitActive = visitsPort.isVisitActive(cmd.visitId());

        // Get product prices from catalog
        Map<String, BigDecimal> productPrices = catalogsPort.getProductPrices(cmd.productIds());

        // Reserve inventory
        inventoryPort.reserveProducts(cmd.productIds());

        // Create order
        Order order = Order.create(
                cmd.visitId(),
                cmd.productIds(),
                productPrices,
                cmd.notes(),
                now
        );

        orderRepository.save(order);

        log.info("[CreateOrder] order created orderId={}", order.getId().value());

        return Result.of(order.getId().value());
    }
}