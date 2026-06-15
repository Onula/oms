package com.oms.app.ui.tables_board.application.port.out;

import com.oms.app.ui.tables_board.application.model.TablesBoard;
import com.oms.app.ui.tables_board.application.model.TablesBoardItem;

import java.util.UUID;

public interface TablesBoardWebSocketPort {

    void publishFullSnapshot(TablesBoard board);

    void publishTablePatch(TablesBoardItem item);

    void publishTableRemoved(UUID tableId);
}
