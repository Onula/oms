package com.oms.app.visits.infra.adapters.in.oders;

import java.util.UUID;

public interface VisitsForOrdersApi {

    boolean isVisitActive(UUID visitId);
}
