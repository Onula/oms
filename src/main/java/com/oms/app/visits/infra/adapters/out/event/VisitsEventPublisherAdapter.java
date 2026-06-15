package com.oms.app.visits.infra.adapters.out.event;

import com.oms.app.common.event.BaseEventPublisher;
import com.oms.app.visits.application.port.out.VisitEventPublisherPort;
import com.oms.app.visits.events.VisitEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class VisitsEventPublisherAdapter implements VisitEventPublisherPort {

    private final BaseEventPublisher eventPublisher;

    @Override
    public void publish(VisitEvent event) {

        eventPublisher.publish(event);
    }
}