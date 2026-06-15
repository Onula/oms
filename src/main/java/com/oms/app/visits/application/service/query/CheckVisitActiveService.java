package com.oms.app.visits.application.service.query;

import com.oms.app.visits.application.port.in.query.CheckVisitActiveUseCasePort;
import com.oms.app.visits.application.port.out.VisitRepositoryPort;
import com.oms.app.visits.domain.VisitStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheckVisitActiveService implements CheckVisitActiveUseCasePort {

    private final VisitRepositoryPort visitRepositoryPort;

    @Override
    public Result check(Query query) {
        boolean active = visitRepositoryPort.existsByIdAndStatus(query.visitId().value(), VisitStatus.ACTIVE);

        return Result.of(active);
    }
}
