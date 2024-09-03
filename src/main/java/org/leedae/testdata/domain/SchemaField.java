package org.leedae.testdata.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.leedae.testdata.domain.constant.MockDataType;

@Getter
@Setter
@ToString
public class SchemaField {

    private String fieldName;
    private MockDataType mockDateType;
    private Integer fieldOrder;
    private Integer blankPercent;
    private String typeOptionJson;
    private String forceValue;

}
