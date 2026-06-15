package com.oms.app.reservations.application.service.command;

import com.oms.app.reservations.application.port.in.command.StartReservationWalkInBlockUseCasePort;
import com.oms.app.reservations.application.port.out.ReservationEventPublisherPort;
import com.oms.app.reservations.application.port.out.ReservationRepositoryPort;
import com.oms.app.reservations.domain.Reservation;
import com.oms.app.reservations.domain.ReservationStatus;
import com.oms.app.reservations.domain.vo.ReservationId;
import com.oms.app.reservations.events.ReservationWalkInBlockStartedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StartReservationWalkInBlockService implements StartReservationWalkInBlockUseCasePort {

    private final ReservationRepositoryPort reservationRepository;
    private final ReservationEventPublisherPort eventPublisher;
    private final Clock clock;

    @Override
    @Transactional
    public void handle(Command cmd) {
        Instant now = Instant.now(clock);

        ReservationId reservationId = cmd.reservationId();

        Reservation reservation = reservationRepository.findById(reservationId.value())
                .orElseThrow(() -> new IllegalStateException("Reservation not found: " + reservationId.value()));

        reservation.ensureCanStartWalkInBlock(now);

        eventPublisher.publish(ReservationWalkInBlockStartedEvent.of(reservation, now));
    }
}
