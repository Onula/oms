package com.oms.app.tables.application.port.in.command;

import com.oms.app.tables.domain.vo.TableId;

import java.util.Objects;
import java.util.UUID;

public interface ReleaseTableUseCasePort {

    void handle(Command cmd);

    record Command(
            TableId tableId
    ) {
        public Command {
            Objects.requireNonNull(tableId, "tableId must not be null");
        }

        public static Command of(UUID tableId) {

            return new Command(TableId.of(tableId));
        }
    }
}