package com.oms.app.tables.application.port.in.command;

import com.oms.app.tables.domain.vo.TableId;

import java.util.UUID;

public interface SetTableOutOfServiceUseCasePort {

    void handle(Command cmd);

    record Command(TableId tableId) {

        public static Command of(UUID tableId) {
            return new Command(TableId.of(tableId));
        }
    }
}
