@ApplicationModule(
        allowedDependencies = {"common", "tables::reservations-api", "tables::events"}
)
package com.oms.app.reservations;

import org.springframework.modulith.ApplicationModule;