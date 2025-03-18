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
    public String getDelimiter() {
        return null;
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.SQL_INSERT;
    }

    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        StringBuilder sb = new StringBuilder();

        String tableName = dto.schemaName();
        String columns = dto.schemaFields().stream()
                .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                .map(SchemaFieldDto::fieldName)
                .collect(Collectors.joining(", "));

        IntStream.range(0, rowCount).forEach(i -> {
            String values = dto.schemaFields().stream()
                    .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                    .map(field -> mockDataGeneratorContext.generate(
                            field.mockDataType(),
                            field.blankPercent(),
                            field.typeOptionJson(),
                            field.forceValue()
                    ))
                    .map(value -> value == null ? "NULL" : "'" + value + "'")
                    .collect(Collectors.joining(", "));

            sb.append("INSERT INTO ").append(tableName).append(" (").append(columns).append(") VALUES (").append(values).append(");\n");
        });

        return sb.toString();
    }
}
