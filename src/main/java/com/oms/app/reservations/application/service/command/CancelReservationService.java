package com.oms.app.reservations.application.service.command;

import com.oms.app.reservations.events.ReservationCancelledEvent;
import com.oms.app.reservations.application.port.in.command.CancelReservationUseCasePort;
import com.oms.app.reservations.application.port.out.ReservationEventPublisherPort;
import com.oms.app.reservations.application.port.out.ReservationRepositoryPort;
import com.oms.app.reservations.domain.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import static com.oms.app.reservations.application.exception.ReservationApplicationExceptions.reservationNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CancelReservationService implements CancelReservationUseCasePort {

    private final ReservationEventPublisherPort eventPublisherPort;
    private final ReservationRepositoryPort reservationRepository;

    private final Clock clock;

    @Override
    public void cancel(Command cmd) {
        Objects.requireNonNull(cmd, "CancelReservationCommand must not be null");
        log.info( "Cancelling reservation reservationId={}", cmd.reservationId() );

        Instant now = Instant.now(clock);

        Reservation reservation = reservationRepository.findByIdForUpdate(cmd.reservationId().value())
                .orElseThrow(() -> reservationNotFound(cmd.reservationId().value()));

        reservation.cancel();

        reservationRepository.save(reservation);

        eventPublisherPort.publish(
                ReservationCancelledEvent.of(reservation,now)
        );

        log.info( "Reservation cancelled successfully reservationId={}", cmd.reservationId() );
    }


}
