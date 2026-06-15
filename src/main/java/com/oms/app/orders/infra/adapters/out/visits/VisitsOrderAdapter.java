package com.oms.app.orders.infra.adapters.out.visits;

import com.oms.app.orders.application.port.out.VisitsPort;
import com.oms.app.visits.infra.adapters.in.oders.VisitsForOrdersApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VisitsOrderAdapter implements VisitsPort {
    private final VisitsForOrdersApi visitsForOrdersApi;

    @Override
    public boolean isVisitActive(UUID visitId){
        return visitsForOrdersApi.isVisitActive(visitId);
    };
}
