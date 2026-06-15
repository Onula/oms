package com.oms.app.reservations.infra.adapters.out.persistence;

import com.oms.app.reservations.domain.Reservation;
import com.oms.app.reservations.domain.ReservationAttentions;
import com.oms.app.reservations.domain.ReservationStatus;
import com.oms.app.reservations.domain.vo.*;
import com.oms.app.reservations.infra.adapters.out.persistence.jpa.ReservationJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReservationPersistenceMapper {

    public Reservation toDomain(ReservationJpaEntity e) {
        return Reservation.reconstitute(
                ReservationId.of(e.getId()),
                ReservationTableId.of(e.getTableId()),
                e.getVisitId(),
                ReservationCustomer.of(
                        ReservationCustomerName.of(e.getCustomerName()),
                        ReservationCustomerPhone.of(e.getCustomerPhone())
                ),
                ReservationPartySize.of(e.getPartySize()),
                ReservationTiming.of(
                        e.getStartTime(),
                        e.getEndTime(),
                        e.getExpirationTime(),
                        e.getWalkInBlockTime()
                ),
                ReservationNotes.of(e.getNotes()),
                ReservationStatus.valueOf(e.getStatus()),
                e.getCreatedAt(),
                toDomainAttentions(e.getAttentions())
        );
    }

    public ReservationJpaEntity toJpa(Reservation r) {
        return new ReservationJpaEntity(
                r.getId().value(),
                r.getTableId().value(),
                r.getCustomer().name().value(),
                r.getCustomer().phone().value(),
                r.getPartySize().value(),
                r.getTiming().startTime(),
                r.getTiming().endTime(),
                r.getTiming().expirationTime(),
                r.getTiming().walkInBlockTime(),
                r.getNotes().value(),
                r.getStatus().name(),
                r.getCreatedAt(),
                r.getVisitId() == null ? null : r.getVisitId().value(),
                toJpaAttentions(r.getAttentions())
        );
    }


    private ReservationAttentions toDomainAttentions(Set<String> attentions) {
        if (attentions == null || attentions.isEmpty()) {
            return ReservationAttentions.empty();
        }

        return ReservationAttentions.of(
                attentions.stream()
                        .map(ReservationAttentions.ReservationAttentionType::valueOf)
                        .toList()
        );
    }

    private Set<String> toJpaAttentions(ReservationAttentions attentions) {
        if (attentions == null) {
            return Set.of();
        }

        return attentions.types()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

}