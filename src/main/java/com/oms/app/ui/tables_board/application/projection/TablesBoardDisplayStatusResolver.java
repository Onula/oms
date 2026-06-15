package com.oms.app.ui.tables_board.application.projection;

import com.oms.app.ui.tables_board.application.model.TableDisplayStatus;
import com.oms.app.ui.tables_board.application.model.TablesBoardItem;
import org.springframework.stereotype.Component;

@Component
public class TablesBoardDisplayStatusResolver {

    public TablesBoardItem recalculate(TablesBoardItem item) {
        return item.withDisplayStatus(resolve(item));
    }

    private TableDisplayStatus resolve(TablesBoardItem item) {
        if (item.hasActiveVisit()) {
            return TableDisplayStatus.VISITED;
        }

        if (item.displayStatus() == TableDisplayStatus.OUT_OF_SERVICE) {
            return TableDisplayStatus.OUT_OF_SERVICE;
        }

        if (item.hasWalkInBlockedReservation()) {
            return TableDisplayStatus.WALK_IN_BLOCKED;
        }

        if (item.hasCurrentReservation() || item.hasUpcomingReservations()) {
            return TableDisplayStatus.RESERVED;
        }

        return TableDisplayStatus.FREE;
    }
}