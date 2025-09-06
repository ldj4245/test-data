package org.leedae.testdata.service;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.TableSchema;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.repository.TableSchemaRepository;
import org.leedae.testdata.service.exporter.MockDataFileExporterContext;
import org.leedae.testdata.service.generator.RowNumberGenerator;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchemaExportService {

    private final MockDataFileExporterContext mockDataFileExporterContext;
    private final TableSchemaRepository tableSchemaRepository;
    private final RowNumberGenerator rowNumberGenerator;

    public String export(ExportFileType fileType, TableSchemaDto dto, Integer rowCount) {
        // ROW_NUMBER 타입의 카운터를 초기화하여 매번 1부터 시작하도록 함
        rowNumberGenerator.resetAllCounters();

        if(dto.userId() != null){
            tableSchemaRepository.findByUserIdAndSchemaName(dto.userId(),dto.schemaName())
                    .ifPresent(TableSchema::markExported);

        }
        return mockDataFileExporterContext.export(fileType, dto, rowCount);
    }

}
