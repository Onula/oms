package com.oms.app.orders.infra.adapters.out.catalogs;

import com.oms.app.orders.application.port.out.InventoryPort;
import org.springframework.stereotype.Component;

@Component
public class InventoryOrderAdapter implements InventoryPort{

    @Override
    public void reserveProducts(java.util.List<java.util.UUID> items) {

    }
}
