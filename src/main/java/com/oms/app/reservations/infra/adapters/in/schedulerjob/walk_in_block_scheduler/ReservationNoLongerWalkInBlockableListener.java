package com.oms.app.reservations.infra.adapters.in.schedulerjob.walk_in_block_scheduler;

import com.oms.app.reservations.events.ReservationCancelledEvent;
import com.oms.app.reservations.events.ReservationExpiredEvent;
import com.oms.app.reservations.events.ReservationSeatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReservationNoLongerWalkInBlockableListener {

    private final ReservationWalkInBlockScheduler walkInBlockScheduler;

    @ApplicationModuleListener
    public void on(ReservationSeatedEvent event) {
        walkInBlockScheduler.cancelWalkInBlock(event.reservationId());
    }

    @ApplicationModuleListener
    public void on(ReservationCancelledEvent event) {
        walkInBlockScheduler.cancelWalkInBlock(event.reservationId());
    }

    @ApplicationModuleListener
    public void on(ReservationExpiredEvent event) {
        walkInBlockScheduler.cancelWalkInBlock(event.reservationId());
    }
}
