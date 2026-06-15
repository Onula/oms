package com.oms.app.visits.application.port.out;

import com.oms.app.visits.events.VisitEvent;

public interface VisitEventPublisherPort {

    void publish(VisitEvent event);
}