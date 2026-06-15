package com.oms.app.ui.common.websocket.infra;

import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import lombok.RequiredArgsConstructor;

import com.oms.app.ui.common.websocket.application.WebSocketDestination;
import com.oms.app.ui.common.websocket.application.WebSocketMessage;
import com.oms.app.ui.common.websocket.application.port.out.WebSocketPublisherPort;

@Component
@RequiredArgsConstructor
public class SpringWebSocketAdapter implements WebSocketPublisherPort {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publish(WebSocketDestination destination, WebSocketMessage message) {
        messagingTemplate.convertAndSend(destination.value(), message);
    }
}
