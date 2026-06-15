package com.oms.app.reservations.infra.adapters.in.schedulerjob.expiration_scheduler;

import com.oms.app.reservations.events.ReservationCancelledEvent;
import com.oms.app.reservations.events.ReservationSeatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ReservationNoLongerExpirableListener {

    private final ReservationExpirationScheduler expirationScheduler;

    @ApplicationModuleListener
    public void on(ReservationSeatedEvent event) {
        expirationScheduler.cancelExpiration(event.reservationId());
    }

    @ApplicationModuleListener
    public void on(ReservationCancelledEvent event) {
        expirationScheduler.cancelExpiration(event.reservationId());
    }
}
