package com.oms.app.visits.infra.adapters.out.persistence.jpa;

import com.oms.app.visits.domain.VisitStatus;
import com.oms.app.visits.domain.VisitType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "visits")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VisitJpaEntity {

    @Id
    @Column(name = "visit_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "table_id", nullable = false)
    private UUID tableId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false)
    private VisitType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VisitStatus status;

    @Column(name = "opened_at", nullable = false)
    private Instant openedAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Version
    @Column(name = "visit_version", nullable = false)
    private long version;

    public VisitJpaEntity(
            UUID id,
            UUID tableId,
            VisitType type,
            VisitStatus status,
            Instant openedAt,
            Instant closedAt,
            Instant cancelledAt
    ) {
        this.id = id;
        this.tableId = tableId;
        this.type = type;
        this.status = status;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
        this.cancelledAt = cancelledAt;
    }

    public void updateFromDomain(
            UUID tableId,
            VisitType type,
            VisitStatus status,
            Instant openedAt,
            Instant closedAt,
            Instant cancelledAt
    ) {
        this.tableId = tableId;
        this.type = type;
        this.status = status;
        this.openedAt = openedAt;
        this.closedAt = closedAt;
        this.cancelledAt = cancelledAt;
    }
}