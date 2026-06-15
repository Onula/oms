package com.oms.app.orders.application.port.out;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CatalogsPort {
    Map<UUID, BigDecimal> getOrderableProductsPrice(List<UUID> items);

    Map<String, BigDecimal> getProductPrices(List<UUID> uuids);
}

