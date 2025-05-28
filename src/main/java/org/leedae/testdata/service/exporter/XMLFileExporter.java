package org.leedae.testdata.service.exporter;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class XMLFileExporter implements MockDataFileExporter {

    private final MockDataGeneratorContext mockDataGeneratorContext;

    @Override
    public ExportFileType getType() {
        return ExportFileType.XML;
    }

    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        StringBuilder sb = new StringBuilder();

        // XML 헤더 및 루트 요소 시작
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<data>\n");

        // 각 행 생성
        IntStream.range(0, rowCount).forEach(i -> {
            sb.append("  <row>\n");

            dto.schemaFields().stream()
                    .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                    .forEach(field -> {
                        String value = mockDataGeneratorContext.generate(
                                field.mockDataType(),
                                field.blankPercent(),
                                field.typeOptionJson(),
                                field.forceValue()
                        );

                        // XML 태그 생성
                        sb.append("    <")
                          .append(escapeXmlTagName(field.fieldName()))
                          .append(">");

                        // 값이 있으면 XML 이스케이프 처리
                        if (value != null && !value.isEmpty()) {
                            sb.append(escapeXml(value));
                        }

                        sb.append("</")
                          .append(escapeXmlTagName(field.fieldName()))
                          .append(">\n");
                    });

            sb.append("  </row>\n");
        });

        // 루트 요소 닫기
        sb.append("</data>");

        return sb.toString();
    }

    /**
     * XML 특수 문자 이스케이프 처리
     */
    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    /**
     * XML 태그 이름으로 사용할 수 있도록 문자열 처리
     */
    private String escapeXmlTagName(String name) {
        // XML 태그 이름에 사용할 수 없는 문자 제거 또는 변환
        return name.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
}
