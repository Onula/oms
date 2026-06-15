package com.oms.app.visits.application.port.out;

import java.util.UUID;

public interface VisitTablesPort {

    void occupyTableForVisit(UUID tableId);

    void releaseTableFromVisit(UUID tableId);

    void transferTableOccupancy(UUID sourceTableId, UUID targetTableId);
}