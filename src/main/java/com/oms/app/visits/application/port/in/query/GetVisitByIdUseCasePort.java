package com.oms.app.visits.application.port.in.query;

import java.util.Objects;
import java.util.UUID;

import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.domain.vo.VisitId;

public interface GetVisitByIdUseCasePort {

    Result get(Query query);

    record Query(
            VisitId visitId
    ) {
        public static Query of(UUID visitId) {
            return new Query(VisitId.of(visitId));
        }
    }

    record Result(
            VisitDTO visit
    ) {
        public static Result from(Visit visit) {
            return new Result(VisitDTO.from(visit));
        }
    }
}
