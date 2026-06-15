package com.oms.app.visits.infra.adapters.in.oders;

import com.oms.app.visits.application.port.in.query.CheckVisitActiveUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VisitsForOrdersApiAdapter implements VisitsForOrdersApi {

    private final CheckVisitActiveUseCasePort checkVisitActiveUseCasePort;

    @Override
    public boolean isVisitActive(UUID visitId) {
        return checkVisitActiveUseCasePort
                .check(CheckVisitActiveUseCasePort.Query.of(visitId))
                .active();
    }
}
