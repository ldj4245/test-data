package org.leedae.testdata.service.exporter;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ExcelFileExporter implements MockDataFileExporter {

    private final MockDataGeneratorContext mockDataGeneratorContext;

    @Override
    public ExportFileType getType() {
        return ExportFileType.EXCEL;
    }

    @Override
    public String export(TableSchemaDto dto, Integer rowCount) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(dto.schemaName());

            // 정렬된 필드 목록 가져오기
            List<SchemaFieldDto> sortedFields = dto.schemaFields().stream()
                    .sorted(Comparator.comparing(SchemaFieldDto::fieldOrder))
                    .collect(Collectors.toList());

            // 헤더 행 생성
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < sortedFields.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(sortedFields.get(i).fieldName());
            }

            // 데이터 행 생성
            for (int rowNum = 1; rowNum <= rowCount; rowNum++) {
                Row dataRow = sheet.createRow(rowNum);

                for (int colNum = 0; colNum < sortedFields.size(); colNum++) {
                    SchemaFieldDto field = sortedFields.get(colNum);
                    Cell cell = dataRow.createCell(colNum);

                    String value = mockDataGeneratorContext.generate(
                            field.mockDataType(),
                            field.blankPercent(),
                            field.typeOptionJson(),
                            field.forceValue()
                    );

                    if (value != null && !value.isEmpty()) {
                        cell.setCellValue(value);
                    }
                }
            }

            // 자동 열 너비 조정
            for (int i = 0; i < sortedFields.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // 워크북을 Base64 인코딩된 문자열로 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Excel 내보내기 중 오류 발생", e);
        }
    }
}
