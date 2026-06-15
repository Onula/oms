package com.oms.app.tables.application.port.in.command;

import com.oms.app.tables.domain.vo.TableCapacity;
import com.oms.app.tables.domain.vo.TableId;

import java.util.UUID;

public interface ChangeTableCapacityUseCasePort {

    void handle(Command cmd);

    record Command(
            TableId tableId,
            TableCapacity capacity
    ) {

        public static Command of(UUID tableId, int capacity) {
            return new Command(
                    TableId.of(tableId),
                    TableCapacity.of(capacity)
            );
        }
    }
}
