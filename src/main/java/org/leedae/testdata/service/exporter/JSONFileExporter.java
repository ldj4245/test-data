package org.leedae.testdata.service.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class JSONFileExporter implements MockDataFileExporter {

    private final MockDataGeneratorContext mockDataGeneratorContext;
    private final ObjectMapper objectMapper;

    @Override
    public String getDelimiter() {
        return null;
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.JSON;
    }

    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        List<Object> data = IntStream.range(0, rowCount)
                .mapToObj(i -> dto.schemaFields().stream()
                        .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                        .collect(Collectors.toMap(
                                SchemaFieldDto::fieldName,
                                field -> mockDataGeneratorContext.generate(
                                        field.mockDataType(),
                                        field.blankPercent(),
                                        field.typeOptionJson(),
                                        field.forceValue()
                                )
                        ))
                )
                .collect(Collectors.toList());

        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JSON data", e);
        }
    }
}
