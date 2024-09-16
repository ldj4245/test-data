package org.leedae.testdata.service.exporter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@DisplayName("[Logic] TSV 파일 출력기 테스트")
@ExtendWith(MockitoExtension.class)
class TSVFileExporterTest {

    @InjectMocks private TSVFileExporter sut;

    @Mock private MockDataGeneratorContext mockDataGeneratorContext;


    @DisplayName("테이블 스키마 정보와 행 수가 주어지면, TSV 형식의 문자열을 생성한다.")
    @Test
    void givenSchemaAndRowCount_whenExporting_thenReturnsTSVFormattedString(){
        // Given
         ExportFileType exportFileType = ExportFileType.TSV;
         TableSchemaDto dto = TableSchemaDto.of(
                 "test_schema",
                 "uno",
                 null,
                 Set.of(
                         SchemaFieldDto.of("id", MockDataType.ROW_NUMBER,1,0,null,null),
                         SchemaFieldDto.of("name",MockDataType.NAME,2,0,null,null),
                         SchemaFieldDto.of("created_at",MockDataType.DATETIME,5,0,null,null),
                         SchemaFieldDto.of("age",MockDataType.NUMBER,3,0,null,null),
                         SchemaFieldDto.of("car", MockDataType.CAR,4,0,null,null)
                 )

         );
         int rowCount = 10;
         given(mockDataGeneratorContext.generate(any(),any(),any(),any())).willReturn("test-value");

        // When
        String result = sut.export(dto,rowCount);


         // Then
         assertThat(result).startsWith("id\tname\tage\tcar\tcreated_at");
         then(mockDataGeneratorContext).should(times(rowCount * dto.schemaFields().size())).generate(any(),any(),any(),any());
    }

}