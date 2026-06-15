package com.oms.app.reservations.infra.adapters.in.web;

import com.oms.app.reservations.application.port.in.command.CancelReservationUseCasePort;
import com.oms.app.reservations.application.port.in.command.CreateReservationUseCasePort;
import com.oms.app.reservations.infra.adapters.in.web.dto.CreateReservationRequest;
import com.oms.app.reservations.infra.adapters.in.web.dto.CreateReservationResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = ReservationsCommandController.BASE_PATH)
@RequiredArgsConstructor
class ReservationsCommandController {

    static final String BASE_PATH = "/api/v1/reservations";

    private final CreateReservationUseCasePort createReservationUseCase;
    private final CancelReservationUseCasePort cancelReservationUseCase;

    @PostMapping
    ResponseEntity<CreateReservationResponse> create(@RequestBody CreateReservationRequest request) {

        CreateReservationUseCasePort.Result result;

        result = createReservationUseCase.create(request.toCommand());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateReservationResponse.of(result));
    }

    @PatchMapping("/{id}/cancel")
    ResponseEntity<Void> cancel(@NotNull @PathVariable UUID id) {

        var command = CancelReservationUseCasePort.Command.of(id);
        cancelReservationUseCase.cancel(command);

        return ResponseEntity.noContent().build();
    }

}
