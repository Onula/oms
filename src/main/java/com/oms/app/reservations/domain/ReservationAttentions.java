package com.oms.app.reservations.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class ReservationAttentions {

    private final Set<ReservationAttentionType> types;

    private ReservationAttentions(Set<ReservationAttentionType> types) {
        this.types = types;
    }

    public static ReservationAttentions empty() {
        return new ReservationAttentions(EnumSet.noneOf(ReservationAttentionType.class));
    }

    public static ReservationAttentions of(Collection<ReservationAttentionType> types) {
        EnumSet<ReservationAttentionType> copy =
                EnumSet.noneOf(ReservationAttentionType.class);

        copy.addAll(types);

        return new ReservationAttentions(copy);
    }

    public boolean addCapacityTooSmall() {
        return types.add(ReservationAttentionType.TABLE_CAPACITY_TOO_SMALL);
    }

    public boolean addTableOutOfService() {
        return types.add(ReservationAttentionType.TABLE_OUT_OF_SERVICE);
    }

    public boolean resolveCapacityTooSmall() {return types.remove(ReservationAttentionType.TABLE_CAPACITY_TOO_SMALL);}

    public boolean resolveTableOutOfService() {
        return types.remove(ReservationAttentionType.TABLE_OUT_OF_SERVICE);
    }

    public boolean hasCapacityTooSmall() {
        return types.contains(ReservationAttentionType.TABLE_CAPACITY_TOO_SMALL);
    }

    public boolean hasTableOutOfService() {
        return types.contains(ReservationAttentionType.TABLE_OUT_OF_SERVICE);
    }

    public boolean hasAny() {
        return !types.isEmpty();
    }

    public Set<ReservationAttentionType> types() {
        return Set.copyOf(types);
    }

    public enum ReservationAttentionType {
        TABLE_CAPACITY_TOO_SMALL,
        TABLE_OUT_OF_SERVICE
    }
}