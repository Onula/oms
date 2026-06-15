package com.oms.app.reservations.application.service.command;

import com.oms.app.reservations.application.port.in.command.ExpireReservationUseCasePort;
import com.oms.app.reservations.application.port.out.ReservationEventPublisherPort;
import com.oms.app.reservations.application.port.out.ReservationRepositoryPort;
import com.oms.app.reservations.domain.Reservation;
import com.oms.app.reservations.events.ReservationExpiredEvent;
import com.oms.app.reservations.domain.vo.ReservationId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

import static com.oms.app.reservations.application.exception.ReservationApplicationExceptions.reservationNotFound;

@Service
@Slf4j
@RequiredArgsConstructor
class ExpireReservationService implements ExpireReservationUseCasePort {

    private final ReservationRepositoryPort reservationRepository;
    private final ReservationEventPublisherPort eventPublisher;
    private final Clock clock;

    @Override
    @Transactional
    public void expireReservation(ReservationId reservationId) {
        Instant now = Instant.now(clock);

        Reservation reservation = reservationRepository.findById(reservationId.value())
                .orElseThrow(() -> reservationNotFound(reservationId.value()));

        if (!reservation.getStatus().isActive()) {
            return;
        }

        if (reservation.getTiming().isExpiredAt(now)) {
            return;
        }

        reservation.markExpired(now);
        reservationRepository.save(reservation);

        eventPublisher.publish(
                ReservationExpiredEvent.of(reservation, now)
        );

        log.info("Reservation expired: id{}", reservation.getId());
    }
}
