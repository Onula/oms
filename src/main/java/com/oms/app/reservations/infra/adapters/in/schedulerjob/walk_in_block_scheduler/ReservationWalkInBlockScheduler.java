package com.oms.app.reservations.infra.adapters.in.schedulerjob.walk_in_block_scheduler;

import com.oms.app.reservations.application.port.in.command.StartReservationWalkInBlockUseCasePort;
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
public class ReservationWalkInBlockScheduler {

    private final TaskScheduler taskScheduler;
    private final StartReservationWalkInBlockUseCasePort startWalkInBlockUseCase;
    private final Clock clock;

    private final Map<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public void scheduleWalkInBlock(UUID reservationId, Instant walkInBlockTime) {
        cancelWalkInBlock(reservationId);

        Instant now = Instant.now(clock);

        if (!walkInBlockTime.isAfter(now)) {
            taskScheduler.schedule(
                    () -> startWalkInBlockUseCase.handle(StartReservationWalkInBlockUseCasePort.Command.of(reservationId)),
                    now
            );
            return;
        }

        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> {
                    try {
                        startWalkInBlockUseCase.handle(StartReservationWalkInBlockUseCasePort.Command.of(reservationId));
                    } finally {
                        scheduledTasks.remove(reservationId);
                    }
                },
                walkInBlockTime
        );

        scheduledTasks.put(reservationId, future);

        log.info("Scheduled reservation walk-in block. reservationId={}, walkInBlockTime={}",
                reservationId,
                walkInBlockTime
        );
    }

    public void cancelWalkInBlock(UUID reservationId) {
        ScheduledFuture<?> existingTask = scheduledTasks.remove(reservationId);

        if (existingTask != null) {
            existingTask.cancel(false);
        }
    }
}
