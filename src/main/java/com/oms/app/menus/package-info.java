@ApplicationModule(
        allowedDependencies = {
                "shared",
                "products::menus-api"
        }
)
package com.oms.app.menus;

import org.springframework.modulith.ApplicationModule;