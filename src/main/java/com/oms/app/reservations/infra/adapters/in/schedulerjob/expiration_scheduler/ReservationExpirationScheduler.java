package com.oms.app.reservations.infra.adapters.in.schedulerjob.expiration_scheduler;

import com.oms.app.reservations.application.port.in.command.ExpireReservationUseCasePort;
import com.oms.app.reservations.domain.vo.ReservationId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpirationScheduler {

    private final TaskScheduler taskScheduler;
    private final ExpireReservationUseCasePort expireReservationUseCase;
    private final Clock clock;

    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleExpiration(UUID reservationId, Instant expirationTime) {
        cancelExpiration(reservationId);

        Instant now = Instant.now(clock);

        if (!expirationTime.isAfter(now)) {
            taskScheduler.schedule(
                    () -> expireReservationUseCase.expireReservation(ReservationId.of(reservationId)),
                    now
            );
            return;
        }

        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> {
                    try {
                        expireReservationUseCase.expireReservation(ReservationId.of(reservationId));
                    } finally {
                        scheduledTasks.remove(reservationId);
                    }
                },
                expirationTime
        );

        scheduledTasks.put(reservationId, future);

        log.info("Scheduled reservation expiration. reservationId={}, expirationTime={}",
                reservationId,
                expirationTime
        );
    }

    public void cancelExpiration(UUID reservationId) {
        ScheduledFuture<?> existingTask = scheduledTasks.remove(reservationId);

        if (existingTask != null) {
            existingTask.cancel(false);
        }
    }
}
