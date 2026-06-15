package com.oms.app.visits.infra.adapters.in.rest;

import com.oms.app.visits.application.port.in.command.CloseVisitUseCasePort;
import com.oms.app.visits.application.port.in.command.OpenVisitUseCasePort;
import com.oms.app.visits.application.port.in.command.TransferVisitUseCasePort;
import com.oms.app.visits.infra.adapters.in.rest.dto.OpenVisitRequest;
import com.oms.app.visits.infra.adapters.in.rest.dto.OpenVisitResponse;
import com.oms.app.visits.infra.adapters.in.rest.dto.TransferVisitRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(VisitsCommandController.BASE_PATH)
public class VisitsCommandController {

    static final String BASE_PATH = "/api/v1/visits";

    private final OpenVisitUseCasePort openVisitUseCasePort;
    private final CloseVisitUseCasePort closeVisitUseCasePort;
    private final TransferVisitUseCasePort transferVisitUseCasePort;

    @PostMapping
    public ResponseEntity<OpenVisitResponse> open(@RequestBody OpenVisitRequest request) {
        Objects.requireNonNull(request, "OpenVisitRequest must not be null");

        OpenVisitUseCasePort.Result result = openVisitUseCasePort.open(
                OpenVisitUseCasePort.Command.of(
                        request.tableId(),
                        request.type()
                )
        );

        return ResponseEntity
                .created(URI.create("/api/visits/" + result.visitId()))
                .body(OpenVisitResponse.from(result));
    }

    @PostMapping("/{visitId}/close")
    public ResponseEntity<Void> close(@PathVariable UUID visitId) {
        closeVisitUseCasePort.close(
                CloseVisitUseCasePort.Command.of(visitId)
        );

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{visitId}/transfer")
    public ResponseEntity<Void> transfer(
            @PathVariable UUID visitId,
            @RequestBody TransferVisitRequest request
    ) {
        Objects.requireNonNull(request, "TransferVisitRequest must not be null");

        transferVisitUseCasePort.transfer(
                TransferVisitUseCasePort.Command.of(
                        visitId,
                        request.targetTableId()
                )
        );

        return ResponseEntity.noContent().build();
    }
}