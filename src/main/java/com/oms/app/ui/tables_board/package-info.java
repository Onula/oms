@ApplicationModule(
        allowedDependencies = {"ui.common", "reservations::events", "tables::events", "visits::events"}
)
package com.oms.app.ui.tables_board;

import org.springframework.modulith.ApplicationModule;