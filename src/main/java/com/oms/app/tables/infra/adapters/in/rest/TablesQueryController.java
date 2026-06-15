package com.oms.app.tables.infra.adapters.in.rest;

import com.oms.app.tables.application.port.in.query.GetTablesMapConfigUseCasePort;
import com.oms.app.tables.infra.adapters.in.rest.dto.GetTablesMapConfigResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TablesCommandController.BASE_PATH)
@RequiredArgsConstructor
public class TablesQueryController {

    private final GetTablesMapConfigUseCasePort getTablesMapConfigUseCasePort;

    @GetMapping("/map-config")
    ResponseEntity<GetTablesMapConfigResponse> getMapConfig() {
        return ResponseEntity.ok(
                GetTablesMapConfigResponse.from(getTablesMapConfigUseCasePort.handle())
        );
    }
}
