package com.oms.app.tables.application.port.out;

import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.TablePlacement;
import com.oms.app.tables.domain.vo.TableId;

import java.util.List;
import java.util.Optional;

public interface TableRepositoryPort {
    Optional<Table> findById(TableId tableId);

    void save(Table table);

    Optional<Table> findByIdWithLock(TableId tableId);

    List<TablePlacement> findActivePlacements();

    List<TablePlacement> findActivePlacementsExcept(TableId tableId);

}
