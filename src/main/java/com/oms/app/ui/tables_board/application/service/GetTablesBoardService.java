package com.oms.app.ui.tables_board.application.service;

import com.oms.app.ui.tables_board.application.port.in.GetTablesBoardUseCase;
import com.oms.app.ui.tables_board.application.port.out.TablesBoardReadModelPort;
import com.oms.app.ui.tables_board.application.model.TablesBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetTablesBoardService implements GetTablesBoardUseCase {

    private final TablesBoardReadModelPort readModelPort;

    @Override
    @Transactional(readOnly = true)
    public TablesBoard handle() {
        return readModelPort.findBoard();
    }
}