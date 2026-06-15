package com.oms.app.orders.application.command;

import com.oms.app.orders.application.port.in.CancelOrderUseCase;
import com.oms.app.orders.application.port.out.OrderRepositoryPort;
import com.oms.app.orders.domain.Order;
import com.oms.app.visits.infra.adapters.in.oders.VisitsForOrdersApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.oms.app.orders.application.exception.OrderApplicationExceptions.cannotCancelOrderForInactiveVisit;
import static com.oms.app.orders.application.exception.OrderApplicationExceptions.orderNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepository;
    private final VisitsForOrdersApi visitsApi;

    @Override
    public void handle(Command cmd) {
        Objects.requireNonNull(cmd, "CancelOrderCommand must not be null");

        log.info("[CancelOrder] orderId={} cancelledBy={}", cmd.orderId().value(), cmd.cancelledBy());

        Order order = orderRepository.findById(cmd.orderId().value())
                .orElseThrow(() -> orderNotFound(cmd.orderId().value()));

        if (!visitsApi.isVisitActive(order.getVisitId())) {
            throw cannotCancelOrderForInactiveVisit(order.getVisitId());
        }

        order.cancel(cmd.cancelledBy());
        orderRepository.save(order);

        log.info("[CancelOrder] order cancelled orderId={}", cmd.orderId().value());
    }
}