package com.oms.app.reservations.infra.adapters.in.tables;

import com.oms.app.reservations.application.port.in.command.ReactToTableEventsUseCasePort;
import com.oms.app.tables.events.TableCapacityChangedEvent;
import com.oms.app.tables.events.TableRemovedFromLayoutEvent;
import com.oms.app.tables.events.TableMarkedOutOfServiceEvent;
import com.oms.app.tables.events.TableReturnedToServiceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TableEventsListener {

    private final ReactToTableEventsUseCasePort useCase;

    @ApplicationModuleListener
    public void on(TableCapacityChangedEvent event) {
        useCase.handle(new ReactToTableEventsUseCasePort.TableCapacityChangedCommand(
                event.tableId(),
                event.newCapacity()
        ));
    }

    @ApplicationModuleListener
    public void on(TableMarkedOutOfServiceEvent event) {
        useCase.handle(new ReactToTableEventsUseCasePort.TableMarkedOutOfServiceCommand(
                event.tableId()
        ));
    }

    @ApplicationModuleListener
    public void on(TableReturnedToServiceEvent event) {
        useCase.handle(new ReactToTableEventsUseCasePort.TableReturnedToServiceCommand(
                event.tableId()
        ));
    }

    @ApplicationModuleListener
    public void on(TableRemovedFromLayoutEvent event) {
        useCase.handle(new ReactToTableEventsUseCasePort.TableRemovedFromLayoutCommand(
                event.tableId()
        ));
    }
}