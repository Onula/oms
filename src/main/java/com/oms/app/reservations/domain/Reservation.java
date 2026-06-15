package com.oms.app.reservations.domain;

import com.oms.app.reservations.domain.vo.*;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.*;

@Getter
public class Reservation {

    private final ReservationId id;
    private final ReservationTableId tableId ;
    private ReservationVisitId visitId;

    private final ReservationCustomer customer;
    private final ReservationPartySize partySize;
    private final ReservationTiming timing;
    private final ReservationNotes notes;
    private ReservationStatus status;

    private final Instant createdAt;
    private ReservationAttentions attentions;

    private static final Duration TABLES_AVAILABILITY_RISK_WINDOW = Duration.ofHours(3);

    Reservation(
            ReservationId id,
            ReservationTableId tableId,
            UUID visitId,
            ReservationCustomer customer,
            ReservationPartySize partySize,
            ReservationTiming timing,
            ReservationNotes notes,
            ReservationStatus status,
            Instant createdAt,
            ReservationAttentions attentions
    ) {
        this.id = Objects.requireNonNull(id, "ReservationId must not be null");
        this.tableId = Objects.requireNonNull(tableId, "Reservation table must not be null");
        this.visitId = visitId == null ? null : ReservationVisitId.of(visitId);
        this.customer = Objects.requireNonNull(customer, "Reservation customer must not be null");
        this.partySize = Objects.requireNonNull(partySize, "Reservation partySize must not be null");
        this.timing = Objects.requireNonNull(timing, "Reservation timing must not be null");
        this.notes = notes == null ? ReservationNotes.empty() : notes;
        this.status = Objects.requireNonNull(status, "Reservation status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Reservation createdAt must not be null");
        this.attentions = attentions == null
                ? ReservationAttentions.empty()
                : attentions;
    }

    public static Reservation create(
            ReservationTable table,
            ReservationCustomer customer,
            ReservationPartySize partySize,
            ReservationTiming timing,
            ReservationNotes notes,
            Instant now
    ){
        if( timing.startTime().isBefore( now.plus(TABLES_AVAILABILITY_RISK_WINDOW))
                && !table.isAvailable()
        ){
            throw tableNotAvailableForReservation(table.id().value(), table.statusString());
        }

        if(partySize.value() > table.capacity()) throw partySizeExceedsTableCapacity(partySize.value(),table.capacity());

        return new Reservation(
                ReservationId.generate(),
                table.id(),
                null,
                customer,
                partySize,
                timing,
                notes,
                ReservationStatus.ACTIVE,
                now,
                ReservationAttentions.empty()
        );
    }

    public void cancel() {

        this.status = this.status.cancel(this.id);
    }

    public void markExpired(Instant now) {
        Objects.requireNonNull(now, "now must not be null");

        if (!this.timing.isExpiredAt(now)) {
            throw cannotExpireReservationBeforeExpirationTime(this.id, this.timing.expirationTime());
        }

        this.status = this.status.expire(this.id);
    }

    public void markSeated(ReservationVisitId visitId, Instant now) {
        ReservationVisitId.ensureNoNull(visitId);

        this.status = this.status.seat(this.id);
        this.visitId = visitId;
    }

    public void ensureCanStartWalkInBlock(Instant now) {
        Objects.requireNonNull(now, "now must not be null");

        if (!status.canStartWalkInBlock()) {
            throw walkInBlockStatusNotAllowed(this.id.value(), this.status.name());
        }

        if (now.isBefore(timing.walkInBlockTime())) {
            throw walkInBlockTooEarly(id, timing.walkInBlockTime(), now);
        }
    }

    public boolean addCapacityTooSmallAttention() {
        return attentions.addCapacityTooSmall();
    }

    public boolean addTableOutOfServiceAttention() {
        return attentions.addTableOutOfService();
    }

    public boolean resolveCapacityTooSmallAttention() {
        return attentions.resolveCapacityTooSmall();
    }

    public boolean resolveTableOutOfServiceAttention() {
        return attentions.resolveTableOutOfService();
    }


    public void deleteAllAttentions(){
        if(attentions.types().isEmpty()){
            return;
        }
        attentions = ReservationAttentions.empty();
    }

    public static Reservation reconstitute(
            ReservationId reservationId,
            ReservationTableId tableId,
            UUID visitId,
            ReservationCustomer customer,
            ReservationPartySize partySize,
            ReservationTiming timing,
            ReservationNotes notes,
            ReservationStatus status,
            Instant createdAt,
            ReservationAttentions attentions
    ){
        return new Reservation(
                reservationId,
                tableId,
                visitId,
                customer,
                partySize,
                timing,
                notes,
                status,
                createdAt,
                attentions
        );
    }

}