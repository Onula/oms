package com.oms.app.ui.tables_board.infra.adapters.out.websocket;

import com.oms.app.ui.common.websocket.application.WebSocketDestination;
import com.oms.app.ui.common.websocket.application.port.out.WebSocketPublisherPort;
import com.oms.app.ui.tables_board.application.model.TablesBoard;
import com.oms.app.ui.tables_board.application.model.TablesBoardItem;
import com.oms.app.ui.tables_board.application.port.out.TablesBoardWebSocketPort;
import com.oms.app.ui.tables_board.application.websocket.TablesBoardWebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
class TablesBoardWebSocketAdapter implements TablesBoardWebSocketPort {

    private static final WebSocketDestination DESTINATION =
            WebSocketDestination.topic("tables-board");

    private final WebSocketPublisherPort webSocketPublisher;

    @Override
    public void publishFullSnapshot(TablesBoard board) {
        webSocketPublisher.publish(
                DESTINATION,
                TablesBoardWebSocketMessage.fullSnapshot(board)
        );
    }

    @Override
    public void publishTablePatch(TablesBoardItem item) {
        webSocketPublisher.publish(
                DESTINATION,
                TablesBoardWebSocketMessage.tablePatch(item)
        );
    }

    @Override
    public void publishTableRemoved(UUID tableId) {
        webSocketPublisher.publish(
                DESTINATION,
                TablesBoardWebSocketMessage.tableRemoved(tableId)
        );
    }
}
