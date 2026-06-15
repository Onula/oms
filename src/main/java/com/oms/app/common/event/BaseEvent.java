package com.oms.app.common.event;

import java.time.Instant;

public interface BaseEvent {
    Instant occurredAt();
}
