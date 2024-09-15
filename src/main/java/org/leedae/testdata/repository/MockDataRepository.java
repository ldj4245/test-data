package org.leedae.testdata.repository;

import org.leedae.testdata.domain.MockData;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MockDataRepository extends JpaRepository<MockData,Long> {
    List<MockData> findByMockDataType(MockDataType mockDataType);

}

