package com.oms.app.reservations.domain;

import com.oms.app.reservations.domain.vo.ReservationId;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.reservationStatusChangeNotAllowed;

public enum ReservationStatus {

    ACTIVE {
        @Override
        public ReservationStatus cancel(ReservationId id) {
            return CANCELLED;
        }

        @Override
        public ReservationStatus seat(ReservationId id) {
            return SEATED;
        }

        @Override
        public ReservationStatus expire(ReservationId id) {
            return EXPIRED;
        }

    },

    SEATED,
    CANCELLED,
    EXPIRED,
    ;

    public ReservationStatus cancel(ReservationId id) {
        throw reservationStatusChangeNotAllowed(id, this, CANCELLED);
    }

    public ReservationStatus seat(ReservationId id) {
        throw reservationStatusChangeNotAllowed(id, this, SEATED);
    }

    public ReservationStatus expire(ReservationId id) {
        throw reservationStatusChangeNotAllowed(id, this, EXPIRED);
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canStartWalkInBlock(){ return this == ACTIVE; }
}