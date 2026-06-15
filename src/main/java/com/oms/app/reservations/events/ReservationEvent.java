package com.oms.app.reservations.events;

import com.oms.app.common.event.BaseEvent;

import java.util.UUID;


public interface ReservationEvent extends BaseEvent {
    UUID reservationId();
    UUID tableId();
}
