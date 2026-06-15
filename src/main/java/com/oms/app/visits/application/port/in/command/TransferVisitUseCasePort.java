package com.oms.app.visits.application.port.in.command;

import com.oms.app.visits.domain.vo.VisitId;
import com.oms.app.visits.domain.vo.VisitTableId;

import java.util.Objects;
import java.util.UUID;

public interface TransferVisitUseCasePort {

    void transfer(Command cmd);

    record Command(
            VisitId visitId,
            VisitTableId toTableId
    ) {
        public Command {
            Objects.requireNonNull(visitId, "visitId must not be null");
            Objects.requireNonNull(toTableId, "toTableId must not be null");
        }

        public static Command of(
                UUID visitId,
                UUID toTableId
        ) {
            return new Command(
                    VisitId.of(visitId),
                    VisitTableId.of(toTableId)
            );
        }
    }
}