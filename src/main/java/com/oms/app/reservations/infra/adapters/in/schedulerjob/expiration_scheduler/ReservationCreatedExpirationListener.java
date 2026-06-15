package com.oms.app.reservations.infra.adapters.in.schedulerjob.expiration_scheduler;

import com.oms.app.reservations.events.ReservationCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReservationCreatedExpirationListener {

    private final ReservationExpirationScheduler expirationScheduler;

    @ApplicationModuleListener
    public void on(ReservationCreatedEvent event) {
        expirationScheduler.scheduleExpiration(
                event.reservationId(),
                event.expirationTime()
        );
    }
}
