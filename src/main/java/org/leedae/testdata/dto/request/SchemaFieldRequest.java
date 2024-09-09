package org.leedae.testdata.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.dto.SchemaFieldDto;

@NoArgsConstructor
@Data
@AllArgsConstructor(staticName = "of")
public class SchemaFieldRequest {

    private String fieldName;
    private MockDataType mockDataType;
    private Integer fieldOrder;
    private Integer blankPercent;
    private String typeOptionJson;
    private String forceValue;


    public SchemaFieldDto toDto(){
        return SchemaFieldDto.of(
                fieldName,
                mockDataType,
                fieldOrder,
                blankPercent,
                typeOptionJson,
                forceValue
        );

    }

}




