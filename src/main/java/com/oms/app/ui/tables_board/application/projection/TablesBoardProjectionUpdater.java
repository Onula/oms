package com.oms.app.ui.tables_board.application.projection;

import com.oms.app.reservations.events.*;
import com.oms.app.tables.events.*;
import com.oms.app.ui.tables_board.application.model.*;
import com.oms.app.ui.tables_board.application.port.out.TablesBoardReadModelPort;
import com.oms.app.ui.tables_board.application.port.out.TablesBoardWebSocketPort;
import com.oms.app.visits.events.VisitClosedEvent;
import com.oms.app.visits.events.VisitOpenedEvent;
import com.oms.app.visits.events.VisitTransferredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
@Slf4j
public class TablesBoardProjectionUpdater {

    private final TablesBoardReadModelPort readModelPort;
    private final TablesBoardWebSocketPort webSocketPort;
    private final TablesBoardDisplayStatusResolver statusResolver;

    public void on(TableCreatedEvent event) {
        saveAndPublish(TablesBoardItem.createFree(
                event.tableId(),
                event.tableCode(),
                event.tableCapacity(),
                new TablesBoardPlacement(
                        event.x(),
                        event.y(),
                        event.width(),
                        event.height()
                )
        ));
    }

    public void on(TablePlacementChangedEvent event) {
        updateItem(
                event.tableId(),
                "table placement changed",
                item -> item.withPlacement(new TablesBoardPlacement(
                        event.x(),
                        event.y(),
                        event.width(),
                        event.height()
                ))
        );
    }

    public void on(ReservationAttentionAddedEvent event) {
        updateItem(
                event.tableId(),
                "reservation attention added",
                item -> item.withReservationAttentionAdded(
                        event.reservationId(),
                        event.attention()
                )
        );
    }

    public void on(ReservationAttentionRemovedEvent event) {
        updateItem(
                event.tableId(),
                "reservation attention removed",
                item -> item.withReservationAttentionRemoved(
                        event.reservationId(),
                        event.attention()
                )
        );
    }

    public void on(ReservationCreatedEvent event) {
        TablesBoardReservationInfo reservationInfo = TablesBoardReservationInfo.of(
                event.reservationId(),
                event.startTime(),
                event.endTime(),
                event.customerName(),
                event.partySize()
        );

        updateItem(
                event.tableId(),
                "reservation created",
                item -> item.withUpcomingReservation(reservationInfo)
        );
    }

    public void on(ReservationWalkInBlockStartedEvent event) {
        updateItem(
                event.tableId(),
                "walk-in block started",
                item -> item.markReservationWalkInBlocked(event.reservationId())
        );
    }

    public void on(ReservationCancelledEvent event) {
        removeReservation(event.tableId(), event.reservationId(), "cancelled");
    }

    public void on(ReservationExpiredEvent event) {
        removeReservation(event.tableId(), event.reservationId(), "expired");
    }

    public void on(ReservationSeatedEvent event) {
        updateItem(
                event.tableId(),
                "reservation seated",
                item -> seatReservation(item, event.reservationId())
        );
    }

    public void on(VisitOpenedEvent event) {
        TablesBoardActiveVisitInfo activeVisit = new TablesBoardActiveVisitInfo(
                event.visitId(),
                event.type(),
                event.openedAt()
        );

        updateItem(
                event.tableId(),
                "visit opened",
                item -> item.withActiveVisit(activeVisit)
        );
    }

    public void on(TableRemovedFromLayoutEvent event) {
        readModelPort.deleteItemByTableId(event.tableId());
        webSocketPort.publishTableRemoved(event.tableId());
    }

    public void on(VisitClosedEvent event) {
        updateItem(
                event.tableId(),
                "visit closed",
                item -> {
                    if (!item.hasActiveVisit()) {
                        return item;
                    }
                    if (!item.activeVisit().visitId().equals(event.visitId())) {
                        log.warn("[TablesBoardProjection] ignoring visit closed for non-active visit tableId={} eventVisitId={} activeVisitId={}", event.tableId(), event.visitId(), item.activeVisit().visitId());
                        return item;
                    }
                    return item.withoutActiveVisit();
                }
        );
    }

    public void on(VisitTransferredEvent event) {
        transferVisit(event);
    }

    public void on(TableMarkedOutOfServiceEvent event) {
        updateItem(
                event.tableId(),
                "table marked out of service",
                TablesBoardItem::markOutOfService
        );
    }

    public void on(TableReturnedToServiceEvent event) {
        updateItem(
                event.tableId(),
                "table returned to service",
                TablesBoardItem::returnToService
        );
    }

    public void on(TableCapacityChangedEvent event) {
        updateItem(
                event.tableId(),
                "table capacity changed",
                item -> item.withCapacity(event.newCapacity())
        );
    }

    private void removeReservation(
            UUID tableId,
            UUID reservationId,
            String reason
    ) {
        updateItem(
                tableId,
                "reservation " + reason,
                item -> {
                    TablesBoardItem updated = item.withoutUpcomingReservation(reservationId);

                    if (updated.currentReservation() != null
                            && updated.currentReservation().reservationId().equals(reservationId)) {
                        return updated.withoutCurrentReservation();
                    }

                    return updated;
                }
        );
    }

    private TablesBoardItem seatReservation(
            TablesBoardItem item,
            UUID reservationId
    ) {
        Optional<TablesBoardReservationInfo> reservationInfo = item.upcomingReservations().stream()
                .filter(reservation -> reservation.reservationId().equals(reservationId))
                .findFirst();

        TablesBoardItem updated = item.withoutUpcomingReservation(reservationId);

        if (reservationInfo.isEmpty()) {
            log.warn(
                    "[TablesBoardProjection] reservation info not found for seated reservationId={} tableId={}",
                    reservationId,
                    item.tableId()
            );
            return updated;
        }

        TablesBoardReservationInfo info = reservationInfo.get();

        return updated.withCurrentReservation(TablesBoardCurrentReservationInfo.of(
                info.reservationId(),
                info.reservationStartTime(),
                info.customerName(),
                info.partySize(),
                info.attentions()
        ));
    }

    private void transferVisit(VisitTransferredEvent event) {
        Optional<TablesBoardItem> sourceItem = readModelPort.findItemByTableId(event.fromTableId());
        Optional<TablesBoardItem> targetItem = readModelPort.findItemByTableId(event.toTableId());

        if (sourceItem.isEmpty()) {
            log.warn("[TablesBoardProjection] source table item not found for visit transfer fromTableId={} visitId={}", event.fromTableId(), event.visitId());
            return;
        }
        if (targetItem.isEmpty()) {
            log.warn("[TablesBoardProjection] target table item not found for visit transfer toTableId={} visitId={}", event.toTableId(), event.visitId());
            return;
        }
        if (!sourceItem.get().hasActiveVisit()) {
            log.warn("[TablesBoardProjection] source table has no active visit for transfer fromTableId={} visitId={}", event.fromTableId(), event.visitId());
            return;
        }
        TablesBoardActiveVisitInfo activeVisit = sourceItem.get().activeVisit();
        if (!activeVisit.visitId().equals(event.visitId())) {
            log.warn("[TablesBoardProjection] source table active visit does not match transferred visit fromTableId={} expectedVisitId={} actualVisitId={}", event.fromTableId(), event.visitId(), activeVisit.visitId());
            return;
        }

        TablesBoardItem updatedSource = statusResolver.recalculate(
                sourceItem.get().withoutActiveVisit()
        );

        TablesBoardItem updatedTarget = statusResolver.recalculate(
                targetItem.get().withActiveVisit(activeVisit)
        );

        saveAndPublish(updatedSource);
        saveAndPublish(updatedTarget);
    }

    private void updateItem(
            UUID tableId,
            String action,
            UnaryOperator<TablesBoardItem> update
    ) {
        readModelPort.findItemByTableId(tableId)
                .map(update)
                .map(statusResolver::recalculate)
                .ifPresentOrElse(
                        this::saveAndPublish,
                        () -> log.warn(
                                "[TablesBoardProjection] table item not found for {} tableId={}",
                                action,
                                tableId
                        )
                );
    }

    private void saveAndPublish(TablesBoardItem item) {
        readModelPort.saveItem(item);
        webSocketPort.publishTablePatch(item);
    }
}