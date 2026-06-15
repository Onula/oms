package com.oms.app.visits.events;

import com.oms.app.common.event.BaseEvent;

import java.util.UUID;

public interface VisitEvent extends BaseEvent {
    UUID visitId();
}