package com.oms.app.reservations.infra.adapters.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "reservations",
        indexes = {
                @Index(
                        name = "idx_reservations_active_overlap",
                        columnList = "table_id, start_time, end_time",
                        options = "WHERE status NOT IN ('CANCELLED', 'SEATED', 'EXPIRED')"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReservationJpaEntity {

    @Id
    @Column(name = "reservation_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "table_id", nullable = false, updatable = false)
    private UUID tableId;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_phone", nullable = false, length = 30)
    private String customerPhone;

    @Column(name = "party_size", nullable = false)
    private int partySize;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "expiration_time", nullable = false)
    private Instant expirationTime;

    @Column(name = "walkin_block_time", nullable = false)
    private Instant walkInBlockTime;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "visit_id")
    private UUID visitId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "reservation_attentions",
            joinColumns = @JoinColumn(name = "reservation_id")
    )
    @Column(name = "attention", nullable = false, length = 60)
    private Set<String> attentions = new HashSet<>();

    @Version
    @Column(name = "version", nullable = false)
    private Long version;


    public ReservationJpaEntity(
            UUID id,
            UUID tableId,
            String customerName,
            String customerPhone,
            int partySize,
            Instant startTime,
            Instant endTime,
            Instant expirationTime,
            Instant walkInBlockTime,
            String notes,
            String status,
            Instant createdAt,
            UUID visitId,
            Set<String> attentions
    ) {
        this.id = id;
        this.tableId = tableId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.partySize = partySize;
        this.startTime = startTime;
        this.endTime = endTime;
        this.expirationTime = expirationTime;
        this.walkInBlockTime = walkInBlockTime;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.visitId = visitId;
        this.attentions = attentions == null
                ? new HashSet<>()
                : new HashSet<>(attentions);
    }

}