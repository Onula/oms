package com.oms.app.tables.domain;

import static com.oms.app.tables.domain.exception.TableDomainExceptions.*;

public record TablePlacement(
        int x,
        int y,
        int width,
        int height
) {

    public TablePlacement {
        if (x < 0) {
            throw invalidPlacementX(0);
        }
        if (y < 0) {
            throw invalidPlacementY(0);
        }
        if (width <= 0) {
            throw invalidPlacementWidth(1);
        }
        if (height <= 0) {
            throw invalidPlacementHeight(1);
        }
    }

    public boolean overlaps(TablePlacement other) {
        return x < other.x + other.width
                && x + width > other.x
                && y < other.y + other.height
                && y + height > other.y;
    }

    public static TablePlacement of(int x, int y, int width, int height) {
        return new TablePlacement(x, y, width, height);
    }

    public TablePlacement expandedBy(int gap) {
        if (gap < 0) {
            throw invalidPlacementGap(0);
        }

        int expandedX = Math.max(0, x - gap);
        int expandedY = Math.max(0, y - gap);

        int expandedRight = x + width + gap;
        int expandedBottom = y + height + gap;

        return new TablePlacement(
                expandedX,
                expandedY,
                expandedRight - expandedX,
                expandedBottom - expandedY
        );
    }
}