package com.oms.app.reservations.application.service.command;

import com.oms.app.reservations.application.port.in.command.SeatReservationUseCasePort;
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
@Slf4j
@RequiredArgsConstructor
public class SeatReservationService implements SeatReservationUseCasePort {

    private final ReservationRepositoryPort reservationRepository;
    private final Clock clock;
    @Override
    @Transactional
    public void seat(Command cmd) {
        Objects.requireNonNull(cmd.reservationId(), "ReservationId must not be null");
        Instant now = Instant.now(clock);
        log.info("[SeatReservation] seating reservationId={}", cmd.reservationId());

        Reservation reservation = reservationRepository.findById(cmd.reservationId().value())
                .orElseThrow(() -> reservationNotFound(cmd.reservationId().value()));

        reservation.markSeated(cmd.visitId(), now);

        reservationRepository.save(reservation);

        log.info("[SeatReservation] seated successfully reservation={}", reservation);
    }
}
