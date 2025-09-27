package org.leedae.testdata.service;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.dto.response.DataPreviewResponse;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataPreviewService {

    private final MockDataGeneratorContext mockDataGeneratorContext;

    /**
     * 테이블 스키마 정보를 기반으로 미리보기 데이터를 생성합니다.
     *
     * @param schemaDto 테이블 스키마 정보
     * @param rowCount  생성할 행 수
     * @return 미리보기 데이터 응답 객체
     */
    public DataPreviewResponse generatePreview(TableSchemaDto schemaDto, int rowCount) {
        try {
            List<String> headers = schemaDto.schemaFields().stream()
                    .map(SchemaFieldDto::fieldName)
                    .collect(Collectors.toList());

            List<Map<String, String>> rows = new ArrayList<>();

            for (int i = 0; i < rowCount; i++) {
                Map<String, String> row = new LinkedHashMap<>();

                for (SchemaFieldDto field : schemaDto.schemaFields()) {
                    MockDataType dataType = field.mockDataType();
                    String fieldName = field.fieldName();

                    // 필요한 필드 값들을 Record 스타일로 얻기
                    String typeOptionJson = field.typeOptionJson();
                    String forceValue = field.forceValue();
                    Integer blankPercent = field.blankPercent();

                    // MockDataGeneratorContext의 generate 메서드의 실제 시그니처에 맞게 호출
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

    /**
     * 단일 필드에 대한 값을 생성합니다.
     * 개별 필드 미리보기에서 사용됩니다.
     *
     * @param dataType 데이터 타입
     * @param blankPercent 빈 값 비율
     * @param typeOptionJson 타입 옵션 JSON
     * @return 생성된 값
     */
    public String generateSingleFieldValue(MockDataType dataType, Integer blankPercent, String typeOptionJson) {
        try {
            String result = mockDataGeneratorContext.generate(
                    dataType,
                    blankPercent,
                    typeOptionJson,
                    null // forceValue는 미리보기에서는 사용하지 않음
            );

            // null이나 빈 문자열이 반환되면 기본값으로 대체
            if (result == null || result.trim().isEmpty() || "null".equals(result)) {
                return generateDefaultValue(dataType);
            }

            return result;
        } catch (Exception e) {
            return "생성 오류: " + e.getMessage();
        }
    }

    /**
     * 데이터 타입별 기본값을 생성합니다.
     */
    private String generateDefaultValue(MockDataType dataType) {
        switch (dataType) {
            case STRING:
                return "샘플 문자열";
            case NUMBER:
                return "42";
            case BOOLEAN:
                return "true";
            case DATETIME:
                return "2024-01-01";
            case KOREAN_NAME:
                return "김철수";
            case KOREAN_ADDRESS:
                return "서울시 강남구";
            case KOREAN_PHONE:
                return "010-1234-5678";
            case EMAIL:
                return "example@example.com";
            case CAR:
                return "현대 쏘나타";
            case ROW_NUMBER:
                return "1";
            case UUID:
                return "550e8400-e29b-41d4-a716-446655440000";
            case NAME:
                return "John Doe";
            case SENTENCE:
                return "이것은 샘플 문장입니다.";
            case PARAGRAPH:
                return "이것은 샘플 단락입니다. 여러 문장으로 구성됩니다.";
            case ENUM:
                return "샘플값";
            default:
                return "샘플 데이터";
        }
    }
}
