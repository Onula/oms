package com.oms.app.tables.infra.adapters.out.persistence;

import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.TablePlacement;
import com.oms.app.tables.domain.TableStatus;
import com.oms.app.tables.domain.vo.TableCapacity;
import com.oms.app.tables.domain.vo.TableCode;
import com.oms.app.tables.domain.vo.TableId;
import com.oms.app.tables.infra.adapters.out.persistence.jpa.TableJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TablePersistenceMapper {

    public Table toDomain(TableJpaEntity e) {
        return Table.reconstitute(
                TableId.of(e.getId()),
                TableCode.of(e.getCode()),
                TableCapacity.of(e.getCapacity()),
                TableStatus.valueOf(e.getStatus()),
                new TablePlacement(
                        e.getPlacementX(),
                        e.getPlacementY(),
                        e.getPlacementWidth(),
                        e.getPlacementHeight()
                )
        );
    }

    public TableJpaEntity toJpaEntity(Table table) {
        return new TableJpaEntity(
                table.getId().value(),
                table.getCode().value(),
                table.getStatus().name(),
                table.getCapacity().value(),
                table.getPlacement().x(),
                table.getPlacement().y(),
                table.getPlacement().width(),
                table.getPlacement().height()
        );
    }

    public void updateJpaEntity(TableJpaEntity entity, Table table) {
        entity.updateFromDomain(
                table.getCode().value(),
                table.getStatus().name(),
                table.getCapacity().value(),
                table.getPlacement().x(),
                table.getPlacement().y(),
                table.getPlacement().width(),
                table.getPlacement().height()
        );
    }
}