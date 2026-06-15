package com.oms.app.visits.application.port.in.query;

import com.oms.app.visits.domain.vo.VisitId;

import java.util.UUID;

public interface CheckVisitActiveUseCasePort {

    Result check(Query query);

    record Query(VisitId visitId) {
        public static Query of(UUID visitId) {
            return new Query(VisitId.of(visitId));
        }
    }

    record Result(boolean active) {
        public static Result of(boolean active) {
            return new Result(active);
        }
    }
}
