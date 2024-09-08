package org.leedae.testdata.dto;

import org.leedae.testdata.domain.MockData;
import org.leedae.testdata.domain.constant.MockDataType;

public record MockDataDto(
        Long id,
        MockDataType mockDataType,
        String mockDataValue)
{

    public static MockDataDto fromEntity(MockData entity){
        return new MockDataDto(
                entity.getId(),
                entity.getMockDataType(),
                entity.getMockDataValue());
    }

}
