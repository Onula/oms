package com.oms.app.tables.infra.adapters.in.rest.dto;

import com.oms.app.tables.domain.TablesMapConfig;

import java.util.List;

public record GetTablesMapConfigResponse(
        int width,
        int height,
        int defaultTableWidth,
        int defaultTableHeight,
        int minTableGap,
        List<TablesMapAreaResponse> unusedAreas
) {

    public static GetTablesMapConfigResponse from(TablesMapConfig config) {
        return new GetTablesMapConfigResponse(
                config.width(),
                config.height(),
                config.defaultTableWidth(),
                config.defaultTableHeight(),
                config.minTableGap(),
                config.unusedAreas()
                        .stream()
                        .map(area -> new TablesMapAreaResponse(
                                area.x(),
                                area.y(),
                                area.width(),
                                area.height()
                        ))
                        .toList()
        );
    }

    public record TablesMapAreaResponse(
            int x,
            int y,
            int width,
            int height
    ) {
    }
}
