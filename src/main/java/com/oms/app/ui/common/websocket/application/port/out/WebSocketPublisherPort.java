package com.oms.app.ui.common.websocket.application.port.out;

import com.oms.app.ui.common.websocket.application.WebSocketDestination;
import com.oms.app.ui.common.websocket.application.WebSocketMessage;

public interface WebSocketPublisherPort {

    void publish(WebSocketDestination destination, WebSocketMessage message);
}
