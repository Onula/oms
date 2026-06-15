package com.oms.app.tables.domain;

import java.util.List;

public record TablesMapConfig(
        int width,
        int height,
        int defaultTableWidth,
        int defaultTableHeight,
        int minTableGap,
        List<TablePlacement> unusedAreas
) {
}
