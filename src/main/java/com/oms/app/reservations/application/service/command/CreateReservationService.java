package com.oms.app.reservations.application.service.command;

import com.oms.app.reservations.events.ReservationCreatedEvent;
import com.oms.app.reservations.application.port.in.command.CreateReservationUseCasePort;
import com.oms.app.reservations.application.port.out.ReservationEventPublisherPort;
import com.oms.app.reservations.application.port.out.ReservationRepositoryPort;
import com.oms.app.reservations.application.port.out.ReservationsTablesPort;
import com.oms.app.reservations.domain.Reservation;
import com.oms.app.reservations.domain.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import static com.oms.app.reservations.application.exception.ReservationApplicationExceptions.cannotCreateReservationOverlapsExisting;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CreateReservationService implements CreateReservationUseCasePort {
    private final ReservationEventPublisherPort eventPublisherPort;
    private final ReservationRepositoryPort repoPort;
    private final ReservationsTablesPort tablesPort;

    private final Clock clock;

    @Override
    public Result create(Command cmd) {
        Objects.requireNonNull(cmd, "CreateReservationCommand must not be null");

        log.info("[CreateReservation] tableId={} startTime={} endTime={}", cmd.tableId(), cmd.timing().startTime(), cmd.timing().endTime());

        Instant now = Instant.now(clock);

        ReservationTable table = tablesPort.getTableWithLockForReservation(cmd.tableId().value());

        boolean hasConflict = repoPort.hasActiveReservationForTableDuring(
                cmd.tableId().value(),
                cmd.timing().startTime(),
                cmd.timing().endTime()
        );
        if (hasConflict) {
            throw cannotCreateReservationOverlapsExisting(cmd.tableId().value(), cmd.timing().startTime(), cmd.timing().endTime());
        }

        Reservation newReservation = Reservation.create(
                table,
                cmd.customer(),
                cmd.partySize(),
                cmd.timing(),
                cmd.notes(),
                now
        );

        repoPort.save(newReservation);

        log.info("[CreateReservation] created and saved to DB reservationId={}", newReservation.getId());

        eventPublisherPort.publish(
                ReservationCreatedEvent.of(newReservation,now)
        );

        log.info("[CreateReservation] event published ");

        return Result.of(newReservation.getId());
    }

}
