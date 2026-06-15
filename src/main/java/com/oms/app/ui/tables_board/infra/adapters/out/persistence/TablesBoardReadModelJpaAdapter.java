package com.oms.app.ui.tables_board.infra.adapters.out.persistence;


import com.oms.app.ui.tables_board.application.model.*;
import com.oms.app.ui.tables_board.application.port.out.TablesBoardReadModelPort;
import com.oms.app.ui.tables_board.infra.adapters.out.persistence.jpa.TablesBoardItemJpaEntity;
import com.oms.app.ui.tables_board.infra.adapters.out.persistence.jpa.TablesBoardItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
class TablesBoardReadModelJpaAdapter implements TablesBoardReadModelPort {

    private static final TypeReference<List<TablesBoardReservationInfo>> UPCOMING_RESERVATIONS_TYPE =
            new TypeReference<>() {
            };
    private static final TypeReference<Set<String>> STRING_SET_TYPE =
            new TypeReference<>() {
            };

    private final TablesBoardItemJpaRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public TablesBoard findBoard() {
        List<TablesBoardItem> items = repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();

        return TablesBoard.of(items);
    }

    @Override
    public Optional<TablesBoardItem> findItemByTableId(UUID tableId) {
        return repository.findById(tableId)
                .map(this::toDomain);
    }

    @Override
    public void saveItem(TablesBoardItem item) {
        repository.save(toEntity(item));
    }

    @Override
    public void deleteItemByTableId(UUID tableId) {
        repository.deleteById(tableId);
    }

    private TablesBoardItem toDomain(TablesBoardItemJpaEntity entity) {
        TablesBoardActiveVisitInfo activeVisit = toActiveVisit(entity);
        TablesBoardCurrentReservationInfo currentReservation = toCurrentReservation(entity);

        return new TablesBoardItem(
                entity.getTableId(),
                entity.getTableCode(),
                entity.getCapacity(),
                entity.getDisplayStatus(),
                new TablesBoardPlacement(
                        entity.getPlacementX(),
                        entity.getPlacementY(),
                        entity.getPlacementWidth(),
                        entity.getPlacementHeight()
                ),
                activeVisit,
                currentReservation,
                deserializeUpcomingReservations(entity.getUpcomingReservationsJson())
        );
    }

    private TablesBoardItemJpaEntity toEntity(TablesBoardItem item) {
        return new TablesBoardItemJpaEntity(
                item.tableId(),
                item.tableCode(),
                item.capacity(),
                item.displayStatus(),

                item.placement().x(),
                item.placement().y(),
                item.placement().width(),
                item.placement().height(),

                item.activeVisit() == null ? null : item.activeVisit().visitId(),
                item.activeVisit() == null ? null : item.activeVisit().visitType(),
                item.activeVisit() == null ? null : item.activeVisit().openedAt(),

                item.currentReservation() == null ? null : item.currentReservation().reservationId(),
                item.currentReservation() == null ? null : item.currentReservation().reservationStartTime(),
                item.currentReservation() == null ? null : item.currentReservation().customerName(),
                item.currentReservation() == null ? null : item.currentReservation().partySize(),
                item.currentReservation() == null
                        ? "[]"
                        : serializeStringSet(item.currentReservation().attentions()),

                serializeUpcomingReservations(item.upcomingReservations())
        );
    }

    private TablesBoardActiveVisitInfo toActiveVisit(TablesBoardItemJpaEntity entity) {
        if (entity.getActiveVisitId() == null) {
            return null;
        }

        return new TablesBoardActiveVisitInfo(
                entity.getActiveVisitId(),
                entity.getActiveVisitType(),
                entity.getOccupiedAt()
        );
    }

    private TablesBoardCurrentReservationInfo toCurrentReservation(TablesBoardItemJpaEntity entity) {
        if (entity.getCurrentReservationId() == null) {
            return null;
        }

        return TablesBoardCurrentReservationInfo.of(
                entity.getCurrentReservationId(),
                entity.getCurrentReservationStartTime(),
                entity.getCurrentCustomerName(),
                entity.getCurrentPartySize(),
                deserializeStringSet(entity.getCurrentReservationAttentionsJson())
        );
    }

    private String serializeUpcomingReservations(List<TablesBoardReservationInfo> reservations) {
        try {
            return objectMapper.writeValueAsString(reservations);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not serialize tables board upcoming reservations", e);
        }
    }

    private List<TablesBoardReservationInfo> deserializeUpcomingReservations(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(json, UPCOMING_RESERVATIONS_TYPE);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not deserialize tables board upcoming reservations", e);
        }
    }

    private String serializeStringSet(Set<String> values) {
        try {
            return objectMapper.writeValueAsString(values == null ? Set.of() : values);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not serialize tables board current reservation attentions", e);
        }
    }

    private Set<String> deserializeStringSet(String json) {
        if (json == null || json.isBlank()) {
            return Set.of();
        }

        try {
            return objectMapper.readValue(json, STRING_SET_TYPE);
        } catch (JacksonException e) {
            throw new IllegalStateException("Could not deserialize tables board current reservation attentions", e);
        }
    }
}
