package org.leedae.testdata.repository;

import org.leedae.testdata.domain.TableSchema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableSchemaRepository extends JpaRepository<TableSchema, Long> {
}
