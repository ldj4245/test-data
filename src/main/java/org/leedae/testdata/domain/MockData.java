package org.leedae.testdata.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.leedae.testdata.domain.constant.MockDataType;

@Getter
@Setter
@ToString
public class MockData {

    private MockDataType mockDatType;
    private String mockDataValue;
}
