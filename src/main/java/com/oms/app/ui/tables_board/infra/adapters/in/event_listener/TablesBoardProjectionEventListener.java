package com.oms.app.ui.tables_board.infra.adapters.in.event_listener;

import com.oms.app.reservations.events.*;
import com.oms.app.tables.events.*;
import com.oms.app.ui.tables_board.application.projection.TablesBoardProjectionUpdater;
import com.oms.app.visits.events.VisitClosedEvent;
import com.oms.app.visits.events.VisitOpenedEvent;
import com.oms.app.visits.events.VisitTransferredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TablesBoardProjectionEventListener {

    private final TablesBoardProjectionUpdater updater;

    /**
     * Table Events
     */
    @ApplicationModuleListener
    void on(TableCreatedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(TableRemovedFromLayoutEvent event) {updater.on(event);}

    /**
     * Reservations Events
     */
    @ApplicationModuleListener
    void on(ReservationCreatedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(ReservationWalkInBlockStartedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(ReservationCancelledEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(ReservationExpiredEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(ReservationSeatedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(ReservationAttentionAddedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(ReservationAttentionRemovedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(TablePlacementChangedEvent event) {updater.on(event);}

    @ApplicationModuleListener
    void on(TableMarkedOutOfServiceEvent event) {updater.on(event);}

    @ApplicationModuleListener
    void on(TableReturnedToServiceEvent event) {updater.on(event);}

    @ApplicationModuleListener
    void on(TableCapacityChangedEvent event) {updater.on(event);}

    /**
     * Visits Events
     */
    @ApplicationModuleListener
    void on(VisitOpenedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(VisitClosedEvent event) {
        updater.on(event);
    }

    @ApplicationModuleListener
    void on(VisitTransferredEvent event) {
        updater.on(event);
    }
}