package com.oms.app.reservations.application.port.out;

import com.oms.app.reservations.events.ReservationEvent;

public interface ReservationEventPublisherPort {
    void publish(ReservationEvent event);
}
