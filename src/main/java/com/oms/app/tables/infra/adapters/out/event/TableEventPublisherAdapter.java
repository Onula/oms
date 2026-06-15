package com.oms.app.tables.infra.adapters.out.event;

import com.oms.app.common.event.BaseEventPublisher;
import com.oms.app.tables.application.port.out.TableEventPublisherPort;
import com.oms.app.tables.events.TableEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TableEventPublisherAdapter implements TableEventPublisherPort {

    private final BaseEventPublisher eventPublisher;

    @Override
    public void publish(TableEvent event) {
        eventPublisher.publish(event);
    }


}
