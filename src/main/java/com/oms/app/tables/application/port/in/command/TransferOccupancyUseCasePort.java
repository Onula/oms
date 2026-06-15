package com.oms.app.tables.application.port.in.command;

import com.oms.app.tables.domain.vo.TableId;

import java.util.Objects;
import java.util.UUID;

public interface TransferOccupancyUseCasePort {

    void handle(Command cmd);

    record Command(
            TableId fromTableId,
            TableId toTableId
    ) {
        public Command {
            Objects.requireNonNull(fromTableId, "fromTableId must not be null");
            Objects.requireNonNull(toTableId, "toTableId must not be null");
        }

        public static Command of(UUID fromTableId, UUID toTableId) {

            return new Command(TableId.of(fromTableId), TableId.of(toTableId));
        }
    }
}