package com.oms.app.ui.tables_board.application.websocket;


import com.oms.app.ui.common.websocket.application.WebSocketMessage;
import com.oms.app.ui.tables_board.application.model.TablesBoard;
import com.oms.app.ui.tables_board.application.model.TablesBoardItem;

import java.util.Objects;
import java.util.UUID;

public record TablesBoardWebSocketMessage<T>(
        TablesBoardWebSocketMessageType type,
        T payload
) implements WebSocketMessage {

    public TablesBoardWebSocketMessage {
        Objects.requireNonNull(type, "tables board message type must not be null");
        Objects.requireNonNull(payload, "tables board message payload must not be null");
    }

    public static TablesBoardWebSocketMessage<TablesBoard> fullSnapshot(TablesBoard board) {
        return new TablesBoardWebSocketMessage<>(
                TablesBoardWebSocketMessageType.FULL_SNAPSHOT,
                board
        );
    }

    public static TablesBoardWebSocketMessage<TablesBoardItem> tablePatch(TablesBoardItem item) {
        return new TablesBoardWebSocketMessage<>(
                TablesBoardWebSocketMessageType.TABLE_PATCH,
                item
        );
    }

    public static TablesBoardWebSocketMessage<UUID> tableRemoved(UUID tableId) {
        return new TablesBoardWebSocketMessage<>(
                TablesBoardWebSocketMessageType.TABLE_REMOVED,
                tableId
        );
    }
}
