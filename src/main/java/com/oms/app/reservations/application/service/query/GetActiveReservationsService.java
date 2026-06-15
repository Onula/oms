package com.oms.app.reservations.application.service.query;

import com.oms.app.reservations.application.port.in.query.GetActiveReservationsUseCasePort;
import com.oms.app.reservations.application.port.out.ReservationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GetActiveReservationsService implements GetActiveReservationsUseCasePort {

    private final ReservationRepositoryPort reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public Result handle() {

        return Result.of(reservationRepository.findActiveReservations());

    }
}