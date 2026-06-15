package com.oms.app.tables.application.service.query;

import com.oms.app.tables.application.port.in.query.GetTableByIdUseCasePort;
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
public class GetTableByIdQueryServicePort implements GetTableByIdUseCasePort {
    private final TableRepositoryPort tableRepository;

    @Transactional(readOnly = true)
    public TableDTO handle(Query query) {

        TableId tableId = TableId.of(query.tableId().value());

        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> tableNotFound( tableId.value() ));

        return TableDTO.of(table);

    }
}
