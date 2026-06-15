package com.oms.app.visits.infra.adapters.out.persistence;

import com.oms.app.visits.domain.Visit;
import com.oms.app.visits.domain.vo.VisitId;
import com.oms.app.visits.domain.vo.VisitTableId;
import com.oms.app.visits.infra.adapters.out.persistence.jpa.VisitJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VisitPersistenceMapper {

    public Visit toDomain(VisitJpaEntity entity) {
        return Visit.reconstitute(
                VisitId.of(entity.getId()),
                VisitTableId.of(entity.getTableId()),
                entity.getType(),
                entity.getStatus(),
                entity.getOpenedAt(),
                entity.getClosedAt(),
                entity.getCancelledAt()
        );
    }

    public VisitJpaEntity toJpaEntity(Visit visit) {
        return new VisitJpaEntity(
                visit.getId().value(),
                visit.getTableId().value(),
                visit.getType(),
                visit.getStatus(),
                visit.getTiming().openedAt(),
                visit.getTiming().closedAt(),
                visit.getTiming().cancelledAt()
        );
    }

    public void updateJpaEntity(VisitJpaEntity entity, Visit visit) {
        entity.updateFromDomain(
                visit.getTableId().value(),
                visit.getType(),
                visit.getStatus(),
                visit.getTiming().openedAt(),
                visit.getTiming().closedAt(),
                visit.getTiming().cancelledAt()
        );
    }
}