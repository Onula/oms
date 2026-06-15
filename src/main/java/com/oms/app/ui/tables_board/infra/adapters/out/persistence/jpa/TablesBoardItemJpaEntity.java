package com.oms.app.ui.tables_board.infra.adapters.out.persistence.jpa;

import com.oms.app.ui.tables_board.application.model.TableDisplayStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tables_board_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TablesBoardItemJpaEntity {

    @Id
    @Column(name = "table_id", nullable = false, updatable = false)
    private UUID tableId;

    @Column(name = "table_code", nullable = false)
    private String tableCode;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "display_status", nullable = false)
    private TableDisplayStatus displayStatus;

    @Column(name = "placement_x", nullable = false)
    private Integer placementX;

    @Column(name = "placement_y", nullable = false)
    private Integer placementY;

    @Column(name = "placement_width", nullable = false)
    private Integer placementWidth;

    @Column(name = "placement_height", nullable = false)
    private Integer placementHeight;

    @Column(name = "active_visit_id")
    private UUID activeVisitId;

    @Column(name = "active_visit_type")
    private String activeVisitType;

    @Column(name = "occupied_at")
    private Instant occupiedAt;

    @Column(name = "current_reservation_id")
    private UUID currentReservationId;

    @Column(name = "current_reservation_start_time")
    private Instant currentReservationStartTime;

    @Column(name = "current_customer_name")
    private String currentCustomerName;

    @Column(name = "current_party_size")
    private Integer currentPartySize;

    @Column(name = "current_reservation_attentions_json", nullable = false, columnDefinition = "TEXT")
    private String currentReservationAttentionsJson = "[]";

    @Column(name = "upcoming_reservations_json", nullable = false, columnDefinition = "TEXT")
    private String upcomingReservationsJson = "[]";
}