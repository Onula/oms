package com.oms.app.orders.infra.adapters.out.persistence;

import com.oms.app.orders.application.port.out.OrderRepositoryPort;
import com.oms.app.orders.domain.Order;
import com.oms.app.orders.infra.adapters.out.persistence.jpa.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository jpaRepository;
    private final OrderEntityMapper mapper;

    @Override
    public Optional<Order> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findByIdForUpdate(UUID id) {
        return jpaRepository.findByIdForUpdate(id)
                .map(mapper::toDomain);
    }

    @Override
    public void save(Order order) {
        jpaRepository.save(mapper.toJpa(order));
    }
}