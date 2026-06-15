package com.oms.app.ui.tables_board.application.model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record TablesBoard(
        List<TablesBoardItem> items
) {

    public TablesBoard {
        Objects.requireNonNull(items, "tables board items must not be null");

        items = items.stream()
                .sorted(Comparator.comparing(TablesBoardItem::tableCode))
                .toList();
    }

    public static TablesBoard of(List<TablesBoardItem> items) {
        return new TablesBoard(items);
    }

    public static TablesBoard empty() {
        return new TablesBoard(List.of());
    }
}