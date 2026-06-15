package com.oms.app.tables.domain;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public enum TableStatus {
    FREE,
    OCCUPIED,
    OUT_OF_SERVICE,
    REMOVED_FROM_LAYOUT;

    private static final Set<TableStatus> CAPACITY_CHANGE_ALLOWED_STATUSES =
            EnumSet.of(FREE, OUT_OF_SERVICE);

    boolean canTransitionTo(TableStatus next) {
        return switch (this) {
            case FREE -> next == OCCUPIED
                    || next == OUT_OF_SERVICE
                    || next == REMOVED_FROM_LAYOUT;

            case OCCUPIED -> next == FREE;

            case OUT_OF_SERVICE -> next == FREE
                    || next == REMOVED_FROM_LAYOUT;

            case REMOVED_FROM_LAYOUT -> false;
        };
    }

    public boolean isFree() {
        return this == FREE;
    }

    public boolean isOccupied() {
        return this == OCCUPIED;
    }

    public boolean isOutOfService() {
        return this == OUT_OF_SERVICE;
    }

    public boolean isRemovedFromLayout() {
        return this == REMOVED_FROM_LAYOUT;
    }

    public boolean allowsCapacityChange() {
        return CAPACITY_CHANGE_ALLOWED_STATUSES.contains(this);
    }

    public static Set<TableStatus> capacityChangeAllowedStatuses() {
        return Set.copyOf(CAPACITY_CHANGE_ALLOWED_STATUSES);
    }

    public static String capacityChangeAllowedStatusesText() {
        return CAPACITY_CHANGE_ALLOWED_STATUSES.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}