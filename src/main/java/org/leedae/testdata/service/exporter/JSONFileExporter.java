package org.leedae.testdata.service.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class JSONFileExporter implements MockDataFileExporter {

    private final MockDataGeneratorContext mockDataGeneratorContext;
    private final ObjectMapper objectMapper;

    @Override
    public ExportFileType getType() {
        return ExportFileType.JSON;
    }

    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        try {
            List<Map<String, Object>> resultList = new ArrayList<>();

            // 각 행 생성
            IntStream.range(0, rowCount).forEach(i -> {
                Map<String, Object> row = new LinkedHashMap<>();

                dto.schemaFields().stream()
                        .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                        .forEach(field -> {
                            String value = mockDataGeneratorContext.generate(
                                    field.mockDataType(),
                                    field.blankPercent(),
                                    field.typeOptionJson(),
                                    field.forceValue()
                            );
                            row.put(field.fieldName(), value);
                        });

                resultList.add(row);
            });

            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultList);
        } catch (Exception e) {
            throw new RuntimeException("JSON 내보내기 중 오류 발생", e);
        }
    }
}
