package com.oms.app.tables.events;

import com.oms.app.common.event.BaseEvent;

import java.util.UUID;

public interface TableEvent extends BaseEvent {
    UUID tableId();
}
