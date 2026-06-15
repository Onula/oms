package com.oms.app.common.event.infra;

import com.oms.app.common.event.BaseEvent;
import com.oms.app.common.event.BaseEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
class SpringModulithEventPublisher implements BaseEventPublisher {

    private final ApplicationEventPublisher publisher;

    SpringModulithEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(BaseEvent event) {
        publisher.publishEvent(event);
    }
}
