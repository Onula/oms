package com.oms.app.reservations.infra.adapters.out.event;

import com.oms.app.common.event.BaseEventPublisher;
import com.oms.app.reservations.events.ReservationEvent;
import com.oms.app.reservations.application.port.out.ReservationEventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReservationEventPublisherAdapter implements ReservationEventPublisherPort {

    private final BaseEventPublisher eventPublisher;

    @Override
    public void publish(ReservationEvent event) {

        eventPublisher.publish(event);
    }
}
