package org.leedae.testdata.service.exporter;

import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CSVFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter{


    @Override
    public String getDelimiter() {
        return ",";
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.CSV;
    }

    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        StringBuilder sb = new StringBuilder();

        // 헤더 만들기
        sb.append(dto.schemaFields().stream()
                        .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                .map(SchemaFieldDto::fieldName)
                .collect(Collectors.joining(getDelimiter()))
        );

        sb.append("\n");

        //데이터 부분
        IntStream.range(0,rowCount).forEach(i -> {
            sb.append(dto.schemaFields().stream()
                    .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                    .map(field -> "가짜-데이터")
                    .map(v -> v == null ? "" : v)
                    .collect(Collectors.joining(getDelimiter()))

            );
            sb.append("\n");
        });

        return sb.toString();

    }
}
