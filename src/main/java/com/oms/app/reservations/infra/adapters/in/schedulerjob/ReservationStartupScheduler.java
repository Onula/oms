package com.oms.app.reservations.infra.adapters.in.schedulerjob;

import com.oms.app.reservations.application.port.in.query.GetActiveReservationsUseCasePort;
import com.oms.app.reservations.infra.adapters.in.schedulerjob.expiration_scheduler.ReservationExpirationScheduler;
import com.oms.app.reservations.infra.adapters.in.schedulerjob.walk_in_block_scheduler.ReservationWalkInBlockScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class ReservationStartupScheduler {

    private final GetActiveReservationsUseCasePort getActiveReservationsUseCasePort;
    private final ReservationExpirationScheduler expirationScheduler;
    private final ReservationWalkInBlockScheduler walkInBlockScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleActiveReservationsOnStartup() {
        var result = getActiveReservationsUseCasePort.handle();

        result.reservations().forEach(r -> {
            expirationScheduler.scheduleExpiration(r.id(), r.expirationTime());
            walkInBlockScheduler.scheduleWalkInBlock(r.id(), r.walkInBlockTime());
        });

        log.info("Scheduled expiration and walk-in block for {} active reservations on startup",
                result.reservations().size());
    }
}
