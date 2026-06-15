package com.oms.app.orders.application.port.out;

import java.util.List;
import java.util.UUID;

public interface InventoryPort {
    void reserveProducts(List<UUID> items);
}
