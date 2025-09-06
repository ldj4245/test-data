package org.leedae.testdata.service;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.dto.MockDataDto;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.dto.response.DataPreviewResponse;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.leedae.testdata.service.generator.RowNumberGenerator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataPreviewService {

    private final MockDataGeneratorContext mockDataGeneratorContext;
    private final RowNumberGenerator rowNumberGenerator;

    /**
     * 테이블 스키마 정보를 기반으로 미리보기 데이터를 생성합니다.
     *
     * @param schemaDto 테이블 스키마 정보
     * @param rowCount  생성할 행 수
     * @return 미리보기 데이터 응답 객체
     */
    public DataPreviewResponse generatePreview(TableSchemaDto schemaDto, int rowCount) {
        try {
            // ROW_NUMBER 타입의 카운터를 초기화하여 매번 1부터 시작하도록 함
            rowNumberGenerator.resetAllCounters();

            // fieldOrder에 따라 필드를 정렬
            List<SchemaFieldDto> sortedFields = schemaDto.schemaFields().stream()
                    .sorted((a, b) -> Integer.compare(a.fieldOrder(), b.fieldOrder()))
                    .collect(Collectors.toList());

            List<String> headers = sortedFields.stream()
                    .map(SchemaFieldDto::fieldName)
                    .collect(Collectors.toList());

            List<Map<String, String>> rows = new ArrayList<>();

            for (int i = 0; i < rowCount; i++) {
                Map<String, String> row = new LinkedHashMap<>();

                for (SchemaFieldDto field : sortedFields) {
                    MockDataType dataType = field.mockDataType();
                    String fieldName = field.fieldName();

                    // 필요한 필드 값들을 Record 스타일로 얻기
                    String typeOptionJson = field.typeOptionJson();
                    String forceValue = field.forceValue();
                    Integer blankPercent = field.blankPercent();

                    // MockDataGeneratorContext의 generate 메서드의 실제 시그니��에 맞게 호출
                    String mockDataValue = mockDataGeneratorContext.generate(
                            dataType,
                            blankPercent,
                            typeOptionJson,
                            forceValue
                    );

                    row.put(fieldName, mockDataValue);
                }

                rows.add(row);
            }

            return DataPreviewResponse.builder()
                    .schemaName(schemaDto.schemaName())
                    .headers(headers)
                    .rows(rows)
                    .success(true)
                    .build();

        } catch (Exception e) {
            return DataPreviewResponse.builder()
                    .success(false)
                    .message("데이터 생성 중 오류가 발생했습니다: " + e.getMessage())
                    .build();
        }
    }
}
