package com.oms.app.tables.application.port.out;

import com.oms.app.tables.events.TableEvent;

public interface TableEventPublisherPort {
    void publish(TableEvent event);
}
