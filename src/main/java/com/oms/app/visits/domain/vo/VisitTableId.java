package com.oms.app.visits.domain.vo;

import java.io.Serializable;
import java.util.UUID;

import static com.oms.app.visits.domain.exception.VisitsDomainExceptions.visitTableIdRequired;

public record VisitTableId(UUID value) implements Serializable {

    public VisitTableId {
        if (value == null) throw visitTableIdRequired();
    }

    public static VisitTableId of(UUID value) {
        return new VisitTableId(value);
    }
}