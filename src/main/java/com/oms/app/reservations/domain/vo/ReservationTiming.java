package com.oms.app.reservations.domain.vo;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static com.oms.app.reservations.domain.exception.ReservationDomainExceptions.*;

public record ReservationTiming(
        Instant startTime,
        Instant endTime,
        Instant expirationTime,
        Instant walkInBlockTime
) {

    private static final Duration MAX_DURATION          = Duration.ofHours(10);
    private static final Duration MAX_WALK_IN_BLOCK     = Duration.ofMinutes(100);
    private static final Duration MAX_EXPIRATION_WINDOW = Duration.ofHours(24);

    public ReservationTiming {
        if (startTime == null) throw startTimeRequired();

        if (endTime == null || !endTime.isAfter(startTime)) throw invalidEndTime();

        if (Duration.between(startTime, endTime).compareTo(MAX_DURATION) > 0)
            throw reservationTooLong(MAX_DURATION);

        if (expirationTime == null
                || !expirationTime.isAfter(startTime)
                || expirationTime.isAfter(endTime))
            throw invalidExpirationTime();

        if (Duration.between(startTime, expirationTime).compareTo(MAX_EXPIRATION_WINDOW) > 0)
            throw expirationWindowTooLong(MAX_EXPIRATION_WINDOW);

        if (walkInBlockTime == null || !walkInBlockTime.isBefore(startTime))
            throw invalidWalkInBlockTime();

        if (Duration.between(walkInBlockTime, startTime).compareTo(MAX_WALK_IN_BLOCK) > 0)
            throw walkInBlockTooEarly(MAX_WALK_IN_BLOCK);
    }

    public static ReservationTiming of(
            Instant startTime,
            Instant endTime,
            Instant expirationTime,
            Instant walkInBlockTime
    ) {
        return new ReservationTiming(
                startTime,
                endTime,
                expirationTime,
                walkInBlockTime
        );
    }

    public boolean isExpiredAt(Instant time) {
        Objects.requireNonNull(time, "time must not be null");

        return !time.isBefore(this.expirationTime);
    }

    public boolean blocksWalkInsAt(Instant time) {
        Objects.requireNonNull(time, "time must not be null");

        return !time.isBefore(this.walkInBlockTime)
                && time.isBefore(this.startTime);
    }
}