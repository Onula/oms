package com.oms.app.orders.infra.adapters.out.persistence.jpa;

import com.oms.app.orders.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderJpaEntity {

    @Id
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "visit_id", nullable = false)
    private UUID visitId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "submitted_by")
    private UUID submittedBy;

    @Column(name = "prepared_at")
    private Instant preparedAt;

    @Column(name = "prepared_by")
    private UUID preparedBy;

    @Column(name = "ready_at")
    private Instant readyAt;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancelled_by")
    private UUID cancelledBy;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    @Version
    @Column(name = "order_version", nullable = false)
    private Long version;

    public OrderJpaEntity(
            UUID id,
            UUID visitId,
            OrderStatus status,
            Instant createdAt,
            Instant preparedAt,
            UUID preparedBy,
            Instant readyAt,
            Instant cancelledAt,
            UUID cancelledBy,
            String note,
            Long version
    ) {
        this.id = id;
        this.visitId = visitId;
        this.status = status;
        this.createdAt = createdAt;
        this.preparedAt = preparedAt;
        this.preparedBy = preparedBy;
        this.readyAt = readyAt;
        this.cancelledAt = cancelledAt;
        this.cancelledBy = cancelledBy;
        this.note = note;
        this.version = version;
    }
}