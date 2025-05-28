package org.leedae.testdata.service.exporter;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class SQLInsertFileExporter implements MockDataFileExporter {

    private final MockDataGeneratorContext mockDataGeneratorContext;

    @Override
    public ExportFileType getType() {
        return ExportFileType.SQL_INSERT;
    }

    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        StringBuilder sb = new StringBuilder();

        // 테이블 이름 설정
        String tableName = dto.schemaName();

        // 컬럼 이름 정렬해서 가져오기
        String columns = dto.schemaFields().stream()
                .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                .map(SchemaFieldDto::fieldName)
                .collect(Collectors.joining(", "));

        // 각 행에 대해 INSERT 문 생성
        IntStream.range(0, rowCount).forEach(i -> {
            String values = dto.schemaFields().stream()
                    .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                    .map(field -> {
                        String value = mockDataGeneratorContext.generate(
                                field.mockDataType(),
                                field.blankPercent(),
                                field.typeOptionJson(),
                                field.forceValue()
                        );

                        // NULL 값 처리
                        if (value == null || value.isEmpty()) {
                            return "NULL";
                        }

                        // 문자열은 따옴표로 감싸기
                        return "'" + value.replace("'", "''") + "'";
                    })
                    .collect(Collectors.joining(", "));

            sb.append("INSERT INTO ")
              .append(tableName)
              .append(" (")
              .append(columns)
              .append(") VALUES (")
              .append(values)
              .append(");\n");
        });

        return sb.toString();
    }
}
