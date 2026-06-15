package com.oms.app.tables.application.service.query;

import com.oms.app.tables.application.port.in.query.GetTablesMapConfigUseCasePort;
import com.oms.app.tables.domain.TablesMap;
import com.oms.app.tables.domain.TablesMapConfig;
import org.springframework.stereotype.Service;

@Service
public class GetTablesMapConfigService implements GetTablesMapConfigUseCasePort {

    @Override
    public TablesMapConfig handle() {
        return TablesMap.config();
    }
}
