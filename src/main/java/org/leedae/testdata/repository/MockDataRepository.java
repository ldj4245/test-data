package org.leedae.testdata.repository;

import org.leedae.testdata.domain.MockData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataRepository extends JpaRepository<MockData,Long> {

}

