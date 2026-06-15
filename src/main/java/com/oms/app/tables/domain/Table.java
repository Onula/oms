package com.oms.app.tables.domain;

import com.oms.app.tables.domain.vo.TableCapacity;
import com.oms.app.tables.domain.vo.TableCode;
import com.oms.app.tables.domain.vo.TableId;
import lombok.Getter;

import java.util.Objects;

import static com.oms.app.tables.domain.exception.TableDomainExceptions.*;

@Getter
public class Table {

    private final TableId id;
    private final TableCode code;
    private TableStatus status;
    private TableCapacity capacity;
    private TablePlacement placement;

    private Table(
            TableId id,
            TableCode code,
            TableCapacity capacity,
            TableStatus status,
            TablePlacement placement
    ) {
        this.id = Objects.requireNonNull(id, "table id must not be null");
        this.code = Objects.requireNonNull(code, "table code must not be null");
        this.capacity = Objects.requireNonNull(capacity, "table capacity must not be null");
        this.status = Objects.requireNonNull(status, "table status must not be null");
        this.placement = Objects.requireNonNull(placement, "table placement must not be null");
    }

    public static Table create(TableCode code, TableCapacity capacity,TablePlacement placement ) {
        return new Table(
                TableId.generate(),
                code,
                capacity,
                TableStatus.FREE,
                placement
        );
    }

    public static Table reconstitute(
            TableId id,
            TableCode code,
            TableCapacity capacity,
            TableStatus status,
            TablePlacement placement
    ) {
        return new Table(
                id,
                code,
                capacity,
                status,
                placement
        );
    }

    public void removeFromLayout() {
        TableStatus nextStatus = TableStatus.REMOVED_FROM_LAYOUT;

        if (this.status == nextStatus) {
            return;
        }
        changeStatusTo(nextStatus);
    }

    public void occupy() {
        TableStatus nextStatus = TableStatus.OCCUPIED;

        if (this.status == nextStatus) {
            return;
        }
        changeStatusTo(nextStatus);
    }

    public void release() {
        TableStatus nextStatus = TableStatus.FREE;

        if (this.status == nextStatus) {
            return;
        }
        changeStatusTo(nextStatus);
    }

    public void setOutOfService() {
        TableStatus nextStatus = TableStatus.OUT_OF_SERVICE;

        if (this.status == nextStatus) {
            return;
        }

        changeStatusTo(nextStatus);
    }

    public void returnToService() {
        TableStatus nextStatus = TableStatus.FREE;

        if (this.status == nextStatus) {
            return;
        }

        changeStatusTo(nextStatus);
    }

    public void changeCapacity(TableCapacity newCapacity) {
        Objects.requireNonNull(newCapacity, "table capacity cannot be null");

        if (this.capacity.equals(newCapacity)) {
            return;
        }

        if (!this.status.allowsCapacityChange()) {
            throw tableCapacityChangeNotAllowedInStatus(
                    this.status,
                    TableStatus.capacityChangeAllowedStatusesText()
            );
        }

        this.capacity = newCapacity;
    }

    private void changeStatusTo(TableStatus next) {
        Objects.requireNonNull(next, "next table status must not be null");

        if (!status.canTransitionTo(next)) {
            throw statusChangeNotAllowed(this.status, next);
        }

        this.status = next;
    }

    public void changePlacement(TablePlacement newPlacement) {
        Objects.requireNonNull(newPlacement, "table placement must not be null");

        if (this.placement.equals(newPlacement)) {
            return;
        }

        if (this.status == TableStatus.REMOVED_FROM_LAYOUT) {
            throw statusChangeNotAllowed(this.status, this.status);
        }

        this.placement = newPlacement;
    }
}