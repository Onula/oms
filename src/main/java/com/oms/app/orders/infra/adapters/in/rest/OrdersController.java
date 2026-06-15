package com.oms.app.orders.infra.adapters.in.rest;

import com.oms.app.orders.application.port.in.CreateOrderUseCase;
import com.oms.app.orders.infra.adapters.in.rest.dto.create_reservation.CreateOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(OrdersController.BASE_PATH)
@RequiredArgsConstructor
public class OrdersController {
    static final String BASE_PATH = "/api/v1/orders";

    CreateOrderUseCase createOrderUseCase;

    @PostMapping
    ResponseEntity<Void> create(@Valid @RequestBody CreateOrderRequest request) {

        CreateOrderUseCase.Result result;

        result = createOrderUseCase.handle(request.toCommand());

        return ResponseEntity.created(locationOf(result.orderId())).build();
    }


    private URI locationOf(UUID orderId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{orderId}")
                .buildAndExpand(orderId)
                .toUri();
    }
}
