package com.oms.app.ui.tables_board.application.port.out;

import com.oms.app.ui.tables_board.application.model.TablesBoard;
import com.oms.app.ui.tables_board.application.model.TablesBoardItem;

import java.util.Optional;
import java.util.UUID;


public interface TablesBoardReadModelPort {

    TablesBoard findBoard();

    Optional<TablesBoardItem> findItemByTableId(UUID tableId);

    void saveItem(TablesBoardItem item);

    void deleteItemByTableId(UUID tableId);
}
