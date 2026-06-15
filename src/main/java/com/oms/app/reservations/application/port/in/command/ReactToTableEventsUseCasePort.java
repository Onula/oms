package com.oms.app.reservations.application.port.in.command;

import java.util.UUID;

public interface ReactToTableEventsUseCasePort {

    void handle(TableCapacityChangedCommand cmd);

    void handle(TableMarkedOutOfServiceCommand cmd);

    void handle(TableReturnedToServiceCommand cmd);

    void handle(TableRemovedFromLayoutCommand cmd);

    record TableCapacityChangedCommand(
            UUID tableId,
            int newCapacity
    ) {
    }

    record TableMarkedOutOfServiceCommand(
            UUID tableId
    ) {
    }

    record TableReturnedToServiceCommand(
            UUID tableId
    ) {
    }



    record TableRemovedFromLayoutCommand(
            UUID tableId
    ) {
    }
}
