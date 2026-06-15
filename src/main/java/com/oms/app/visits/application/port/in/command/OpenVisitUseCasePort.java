package com.oms.app.visits.application.port.in.command;

import com.oms.app.visits.domain.VisitType;
import com.oms.app.visits.domain.vo.VisitId;
import com.oms.app.visits.domain.vo.VisitTableId;

import java.util.Objects;
import java.util.UUID;

public interface OpenVisitUseCasePort {

    Result open(Command cmd);

    record Command(
            VisitTableId tableId,
            VisitType type
    ) {
        public Command {
            Objects.requireNonNull(tableId, "tableId must not be null");
            Objects.requireNonNull(type, "type must not be null");
        }

        public static Command of(
                UUID tableId,
                VisitType type
        ) {
            return new Command(
                    VisitTableId.of(tableId),
                    type
            );
        }
    }

    record Result(
            UUID visitId
    ) {
        public static Result of(VisitId visitId) {
            return new Result(visitId.value());
        }
    }
}