package com.oms.app.tables.application.port.in.query;

import com.oms.app.tables.domain.vo.TableId;

import java.util.Objects;
import java.util.UUID;

public interface GetTableByIdWithLockUseCase {

    TableDTO handle(Query query);

    record Query(TableId tableId) {
        public Query {
            Objects.requireNonNull(tableId, "tableId must not be null");
        }
        public static Query of(UUID tableId) {
            return new Query(TableId.of(tableId));
        }
    }

}
