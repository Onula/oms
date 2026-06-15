package com.oms.app.tables.infra.adapters.in.forVisits;

import java.util.UUID;

public interface TableForVisitsApi {

    void occupyTable(UUID tableId);

    void releaseTable(UUID tableId);

    void transferTableOccupy(UUID fromTableId, UUID toTableId);

}
