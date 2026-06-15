package com.oms.app.visits.infra.adapters.in.rest;

import com.oms.app.visits.application.port.in.query.GetVisitByIdUseCasePort;
import com.oms.app.visits.application.port.in.query.VisitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visits")
public class VisitsQueryController {

    private final GetVisitByIdUseCasePort getVisitByIdUseCasePort;

    @GetMapping("/{visitId}")
    public ResponseEntity<VisitDTO> getById(@PathVariable UUID visitId) {
        GetVisitByIdUseCasePort.Result result = getVisitByIdUseCasePort.get(
                GetVisitByIdUseCasePort.Query.of(visitId)
        );

        return ResponseEntity.ok(result.visit());
    }
}
