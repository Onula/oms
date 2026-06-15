package com.oms.app.reservations.events;

import java.time.Instant;
import java.util.UUID;

public record ReservationAttentionAddedEvent(
        UUID reservationId,
        UUID tableId,
        String attention,
        Instant occurredAt
) implements ReservationEvent {
}
