package com.oms.app.tables.application.port.in.command;

import com.oms.app.tables.domain.TablePlacement;
import com.oms.app.tables.domain.vo.TableId;

import java.util.UUID;

public interface ChangeTablePlacementUseCasePort {

    void handle(Command cmd);

    record Command(
            TableId tableId,
            TablePlacement newPlacement
    ) {

        public static Command of(UUID tableId, int x, int y, int width, int height) {
            return new Command(
                    TableId.of(tableId),
                    TablePlacement.of(x, y, width, height)
            );

        }
    }
}
