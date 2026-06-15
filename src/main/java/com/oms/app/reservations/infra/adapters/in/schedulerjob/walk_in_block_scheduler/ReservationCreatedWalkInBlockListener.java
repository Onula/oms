package com.oms.app.reservations.infra.adapters.in.schedulerjob.walk_in_block_scheduler;

import com.oms.app.reservations.events.ReservationCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReservationCreatedWalkInBlockListener {

    private final ReservationWalkInBlockScheduler walkInBlockScheduler;

    @ApplicationModuleListener
    public void on(ReservationCreatedEvent event) {
        walkInBlockScheduler.scheduleWalkInBlock(
                event.reservationId(),
                event.walkInBlockTime()
        );
    }
}
