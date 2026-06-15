package com.oms.app.orders.infra.adapters.in.rest.dto.create_reservation;

import com.oms.app.orders.application.port.in.CreateOrderUseCase;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(

        UUID visitId,

        List< UUID> items,

        String note
) {

        public CreateOrderUseCase.Command toCommand() {
                return CreateOrderUseCase.Command.of(
                        visitId,
                        items,
                        note
                );
        }
}
