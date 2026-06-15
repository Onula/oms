package com.oms.app.orders.infra.adapters.out.inventory;

import com.oms.app.orders.application.port.out.CatalogsPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CatalogsOrderAdapter implements CatalogsPort {

    @Override
    public Map<UUID, BigDecimal> getOrderableProductsPrice(List<UUID> items) {
        return Map.of();
    }

    @Override
    public Map<String, BigDecimal> getProductPrices(List<UUID> uuids) {
        return Map.of();
    }
}
