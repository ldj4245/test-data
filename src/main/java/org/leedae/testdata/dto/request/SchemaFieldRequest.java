package org.leedae.testdata.dto.request;

import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.dto.SchemaFieldDto;

public record SchemaFieldRequest(
        String fieldName,
        MockDataType mockDataType,
        Integer fieldOrder,
        Integer blankPercent,
        String typeOptionJson,
        String forceValue

)
{
    public static SchemaFieldRequest of(String fieldName, MockDataType mockDataType, Integer fieldOrder, Integer blankPercent, String typeOptionJson, String forceValue
    ){
        return new SchemaFieldRequest(fieldName, mockDataType, fieldOrder, blankPercent, typeOptionJson, forceValue);
    }

    public SchemaFieldDto toDto(){
        return SchemaFieldDto.of(
                fieldName(),
                mockDataType(),
                fieldOrder(),
                blankPercent(),
                typeOptionJson(),
                forceValue()
        );

    }

}




