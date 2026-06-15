package com.oms.app.tables.application.service.query;

import com.oms.app.tables.application.port.in.query.GetTableByIdWithLockUseCase;
import com.oms.app.tables.application.port.in.query.TableDTO;
import com.oms.app.tables.application.port.out.TableRepositoryPort;
import com.oms.app.tables.domain.Table;
import com.oms.app.tables.domain.vo.TableId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.oms.app.tables.application.exception.TableApplicationExceptions.tableNotFound;

@Service
@RequiredArgsConstructor
public class GetTableByIdWithLockService implements GetTableByIdWithLockUseCase {
    private final TableRepositoryPort tableRepository;

    @Transactional(readOnly = true)
    public TableDTO handle(Query query) {

        TableId tableId = TableId.of(query.tableId().value());

        Table table = tableRepository.findByIdWithLock(tableId)
                .orElseThrow(() -> tableNotFound( tableId.value() ));

        return TableDTO.of(table);

    }
}