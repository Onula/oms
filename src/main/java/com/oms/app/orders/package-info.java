@ApplicationModule(
        allowedDependencies = {
                "common",
                "products::orders-api",
                "visits::orders-api"
        }
)
package com.oms.app.orders;

import org.springframework.modulith.ApplicationModule;