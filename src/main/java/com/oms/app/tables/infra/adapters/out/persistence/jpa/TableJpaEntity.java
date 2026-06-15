package com.oms.app.tables.infra.adapters.out.persistence.jpa;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@jakarta.persistence.Table(name = "tables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TableJpaEntity {

    @Id
    @Column(name = "table_id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "table_code", nullable = false, length = 5, unique = true)
    private String code;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "table_capacity", nullable = false)
    private int capacity;

    @Column(name = "placement_x", nullable = false)
    private int placementX;

    @Column(name = "placement_y", nullable = false)
    private int placementY;

    @Column(name = "placement_width", nullable = false)
    private int placementWidth;

    @Column(name = "placement_height", nullable = false)
    private int placementHeight;

    @Version
    @Column(name = "table_version", nullable = false)
    private long version;

    public TableJpaEntity(
            UUID id,
            String code,
            String status,
            int capacity,
            int placementX,
            int placementY,
            int placementWidth,
            int placementHeight
    ) {
        this.id = id;
        this.code = code;
        this.status = status;
        this.capacity = capacity;
        this.placementX = placementX;
        this.placementY = placementY;
        this.placementWidth = placementWidth;
        this.placementHeight = placementHeight;
    }

    public void updateFromDomain(
            String code,
            String status,
            int capacity,
            int placementX,
            int placementY,
            int placementWidth,
            int placementHeight
    ) {
        this.code = code;
        this.status = status;
        this.capacity = capacity;
        this.placementX = placementX;
        this.placementY = placementY;
        this.placementWidth = placementWidth;
        this.placementHeight = placementHeight;
    }
}