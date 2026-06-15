package com.oms.app.orders.application.port.out;


import java.util.UUID;

public interface VisitsPort {
    boolean isVisitActive(UUID uuid);
}
