package com.oms.app.tables.infra.adapters.in.rest;

import com.oms.app.tables.application.port.in.command.*;
import com.oms.app.tables.infra.adapters.in.rest.dto.ChangeTableCapacityRequest;
import com.oms.app.tables.infra.adapters.in.rest.dto.ChangeTablePlacementRequest;
import com.oms.app.tables.infra.adapters.in.rest.dto.CreateTableRequest;
import com.oms.app.tables.infra.adapters.in.rest.dto.CreateTableResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(TablesCommandController.BASE_PATH)
@RequiredArgsConstructor
class TablesCommandController {

    static final String BASE_PATH = "/api/v1/tables";

    private final CreateTableUseCasePort createTableUseCasePort;
    private final ChangeTableCapacityUseCasePort changeTableCapacityUseCasePort;
    private final SetTableOutOfServiceUseCasePort setTableOutOfServiceUseCasePort;
    private final ReturnTableToServiceUseCasePort returnTableToServiceUseCasePort;
    private final RemoveTableFromLayoutUseCasePort removeTableFromLayoutUseCasePort;
    private final ChangeTablePlacementUseCasePort  changeTablePlacementUseCasePort;

    @PostMapping
    ResponseEntity<CreateTableResponse> create(@Valid @RequestBody CreateTableRequest request) {

        CreateTableUseCasePort.Result result = createTableUseCasePort.handle(request.toCommand());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CreateTableResponse.from(result));
    }

    @PatchMapping("/{tableId}/capacity")
    ResponseEntity<Void> changeCapacity(
            @PathVariable UUID tableId,
            @Valid @RequestBody ChangeTableCapacityRequest request
    ) {
        changeTableCapacityUseCasePort.handle(request.toCommand(tableId));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tableId}/out-of-service")
    ResponseEntity<Void> setOutOfService(@PathVariable UUID tableId) {
        setTableOutOfServiceUseCasePort.handle(
                SetTableOutOfServiceUseCasePort.Command.of(tableId)
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tableId}/return-to-service")
    ResponseEntity<Void> returnToService(@PathVariable UUID tableId) {
        returnTableToServiceUseCasePort.handle(
                ReturnTableToServiceUseCasePort.Command.of(tableId)
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tableId}/remove-from-layout")
    ResponseEntity<Void> removeFromLayout(@PathVariable UUID tableId) {
        removeTableFromLayoutUseCasePort.handle(
                RemoveTableFromLayoutUseCasePort.Command.of(tableId)
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tableId}/placement")
    public ResponseEntity<Void> changePlacement(
            @PathVariable UUID tableId,
            @RequestBody ChangeTablePlacementRequest request
    ) {

        changeTablePlacementUseCasePort.handle(request.toCommand(tableId));

        return ResponseEntity.noContent().build();
    }



}