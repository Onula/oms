package com.oms.app.reservations.application.service.command;

import com.oms.app.reservations.application.port.in.command.ReactToTableEventsUseCasePort;
import com.oms.app.reservations.application.port.out.ReservationEventPublisherPort;
import com.oms.app.reservations.application.port.out.ReservationRepositoryPort;
import com.oms.app.reservations.domain.Reservation;
import com.oms.app.reservations.events.ReservationAttentionAddedEvent;
import com.oms.app.reservations.events.ReservationAttentionRemovedEvent;
import com.oms.app.reservations.events.ReservationCancelledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactToTableEventsService implements ReactToTableEventsUseCasePort {

    private final ReservationRepositoryPort reservationRepository;
    private final ReservationEventPublisherPort eventPublisher;

    @Override
    @Transactional
    public void handle(TableCapacityChangedCommand cmd) {
        List<Reservation> reservations =
                reservationRepository.findActiveOrUpcomingByTableIdForUpdate(cmd.tableId());

        for (Reservation reservation : reservations) {
            boolean changed;

            if (reservation.getPartySize().value() > cmd.newCapacity()) {
                changed = reservation.addCapacityTooSmallAttention();

                if (changed) {
                    reservationRepository.save(reservation);
                    eventPublisher.publish(new ReservationAttentionAddedEvent(
                            reservation.getId().value(),
                            reservation.getTableId().value(),
                            "TABLE_CAPACITY_TOO_SMALL",
                            Instant.now()
                    ));
                }
            } else {
                changed = reservation.resolveCapacityTooSmallAttention();

                if (changed) {
                    reservationRepository.save(reservation);
                    eventPublisher.publish(new ReservationAttentionRemovedEvent(
                            reservation.getId().value(),
                            reservation.getTableId().value(),
                            "TABLE_CAPACITY_TOO_SMALL",
                            Instant.now()
                    ));
                }
            }
        }
    }

    @Override
    @Transactional
    public void handle(TableMarkedOutOfServiceCommand cmd) {
        List<Reservation> reservations =
                reservationRepository.findActiveOrUpcomingByTableIdForUpdate(cmd.tableId());

        for (Reservation reservation : reservations) {
            boolean changed = reservation.addTableOutOfServiceAttention();

            if (changed) {
                reservationRepository.save(reservation);
                eventPublisher.publish(new ReservationAttentionAddedEvent(
                        reservation.getId().value(),
                        reservation.getTableId().value(),
                        "TABLE_OUT_OF_SERVICE",
                        Instant.now()
                ));
            }
        }
    }

    @Override
    @Transactional
    public void handle(TableReturnedToServiceCommand cmd) {
        List<Reservation> reservations =
                reservationRepository.findActiveOrUpcomingByTableIdForUpdate(cmd.tableId());

        for (Reservation reservation : reservations) {
            boolean changed = reservation.resolveTableOutOfServiceAttention();

            if (changed) {
                reservationRepository.save(reservation);
                eventPublisher.publish(new ReservationAttentionRemovedEvent(
                        reservation.getId().value(),
                        reservation.getTableId().value(),
                        "TABLE_OUT_OF_SERVICE",
                        Instant.now()
                ));
            }
        }
    }

    @Override
    @Transactional
    public void handle(TableRemovedFromLayoutCommand cmd) {
        List<Reservation> reservations =
                reservationRepository.findActiveOrUpcomingByTableIdForUpdate(cmd.tableId());

        Instant now = Instant.now();

        for (Reservation reservation : reservations) {
            reservation.cancel();

            reservationRepository.save(reservation);

            eventPublisher.publish(
                    ReservationCancelledEvent.of(reservation, now)
            );
        }
    }


}