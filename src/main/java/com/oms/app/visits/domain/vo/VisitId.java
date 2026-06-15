package com.oms.app.visits.domain.vo;

import java.io.Serializable;
import java.util.UUID;

import static com.oms.app.visits.domain.exception.VisitsDomainExceptions.visitIdRequired;

public record VisitId(UUID value) implements Serializable {

    public VisitId {
        if (value == null) throw visitIdRequired();
    }

    public static VisitId generate() {
        return new VisitId(UUID.randomUUID());
    }

    public static VisitId of(UUID value) {
        return new VisitId(value);
    }
}