package com.oms.app.ui.tables_board.infra.adapters.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TablesBoardItemJpaRepository extends JpaRepository<TablesBoardItemJpaEntity, UUID> {
}
