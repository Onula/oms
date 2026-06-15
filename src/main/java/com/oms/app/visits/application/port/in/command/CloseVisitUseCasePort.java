package com.oms.app.visits.application.port.in.command;

import com.oms.app.visits.domain.vo.VisitId;

import java.util.Objects;
import java.util.UUID;

public interface CloseVisitUseCasePort {

    void close(Command cmd);

    record Command(
            VisitId visitId
    ) {
        public Command {
            Objects.requireNonNull(visitId, "visitId must not be null");
        }

        public static Command of(UUID visitId) {
            return new Command(VisitId.of(visitId));
        }
    }
}