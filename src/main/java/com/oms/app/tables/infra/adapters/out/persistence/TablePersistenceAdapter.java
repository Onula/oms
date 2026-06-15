package com.oms.app.tables.infra.adapters.out.persistence;

import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.TablePlacement;
import com.oms.app.tables.domain.vo.TableId;
import com.oms.app.tables.infra.adapters.out.persistence.jpa.TableJpaEntity;
import com.oms.app.tables.infra.adapters.out.persistence.jpa.TableJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TablePersistenceAdapter implements TableRepositoryPort {

    private final TableJpaRepository tableJpaRepository;
    private final TablePersistenceMapper mapper;

    @Override
    public Optional<Table> findById(TableId tableId) {
        return tableJpaRepository.findById(tableId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Table> findByIdWithLock(TableId tableId) {
        return tableJpaRepository.findByIdWithLock(tableId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void save(Table table) {
        TableJpaEntity entity = tableJpaRepository.findById(table.getId().value())
                .map(existingEntity -> {
                    mapper.updateJpaEntity(existingEntity, table);
                    return existingEntity;
                })
                .orElseGet(() -> mapper.toJpaEntity(table));

        tableJpaRepository.save(entity);
    }

    @Override
    public List<TablePlacement> findActivePlacements() {
        return tableJpaRepository.findActiveLayoutTables()
                .stream()
                .map(entity -> new TablePlacement(
                        entity.getPlacementX(),
                        entity.getPlacementY(),
                        entity.getPlacementWidth(),
                        entity.getPlacementHeight()
                ))
                .toList();
    }
    @Override
    public List<TablePlacement> findActivePlacementsExcept(TableId tableId) {
        return tableJpaRepository.findActiveLayoutTablesExcept(tableId.value())
                .stream()
                .map(entity -> new TablePlacement(
                        entity.getPlacementX(),
                        entity.getPlacementY(),
                        entity.getPlacementWidth(),
                        entity.getPlacementHeight()
                ))
                .toList();
    }
}