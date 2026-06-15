package com.oms.app.ui.tables_board.infra.adapters.in.rest;

import com.oms.app.ui.tables_board.application.model.TablesBoard;
import com.oms.app.ui.tables_board.application.port.in.GetTablesBoardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TablesBoardController.BASE_PATH)
@RequiredArgsConstructor
public class TablesBoardController {

    static final String BASE_PATH = "/api/v1/tables-board";

    private final GetTablesBoardUseCase getTablesBoardUseCase;

    @GetMapping
    public TablesBoard getBoard() {
        return getTablesBoardUseCase.handle();
    }
}
