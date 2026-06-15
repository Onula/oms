package com.oms.app.tables.domain;

import java.util.List;

import static com.oms.app.tables.domain.exception.TableDomainExceptions.*;

public final class TablesMap {

    public static final int WIDTH = 2200;
    public static final int HEIGHT = 1600;

    public static final int DEFAULT_TABLE_WIDTH = 80;
    public static final int DEFAULT_TABLE_HEIGHT = 80;

    public static final int MIN_TABLE_GAP = 32;

    private static final List<TablePlacement> UNUSED_AREAS = List.of(
            new TablePlacement(1850, 1300, 350, 300)
    );

    private TablesMap() {
    }

    public static TablesMapConfig config() {
        return new TablesMapConfig(
                WIDTH,
                HEIGHT,
                DEFAULT_TABLE_WIDTH,
                DEFAULT_TABLE_HEIGHT,
                MIN_TABLE_GAP,
                UNUSED_AREAS
        );
    }

    public static void ensurePlacementAllowed(
            TablePlacement placement,
            List<TablePlacement> existingPlacements
    ) {
        ensureInsideMap(placement);
        ensureDoesNotOverlapUnusedArea(placement);
        ensureDoesNotOverlapOtherTable(placement, existingPlacements);
    }

    private static void ensureInsideMap(TablePlacement placement) {
        boolean outsideMap = placement.x() + placement.width() > WIDTH
                || placement.y() + placement.height() > HEIGHT;

        if (outsideMap) {
            throw tablePlacementOutsideMap(WIDTH, HEIGHT);
        }
    }

    private static void ensureDoesNotOverlapUnusedArea(TablePlacement placement) {
        boolean overlapsUnusedArea = UNUSED_AREAS.stream()
                .anyMatch(unusedArea -> unusedArea.overlaps(placement));

        if (overlapsUnusedArea) {
            throw tablePlacementOverlapsUnusedArea();
        }
    }

    private static void ensureDoesNotOverlapOtherTable(
            TablePlacement placement,
            List<TablePlacement> existingPlacements
    ) {
        boolean tooCloseToOtherTable = existingPlacements.stream()
                .map(existing -> existing.expandedBy(MIN_TABLE_GAP))
                .anyMatch(expandedExisting -> expandedExisting.overlaps(placement));

        if (tooCloseToOtherTable) {
            throw tablePlacementTooCloseToAnotherTable(MIN_TABLE_GAP);
        }
    }


}