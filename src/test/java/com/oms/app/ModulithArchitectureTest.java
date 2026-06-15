package com.oms.app.architecture;

import com.oms.app.OmsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithArchitectureTest {

    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(OmsApplication.class);

        modules.verify();
    }
}