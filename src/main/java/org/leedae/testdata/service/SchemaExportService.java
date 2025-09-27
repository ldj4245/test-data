package org.leedae.testdata.service;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.TableSchema;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.repository.TableSchemaRepository;
import org.leedae.testdata.service.exporter.MockDataFileExporterContext;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SchemaExportService {

    private final MockDataFileExporterContext mockDataFileExporterContext;
    private final TableSchemaRepository tableSchemaRepository;

    public String export(ExportFileType fileType, TableSchemaDto dto, Integer rowCount) {
        if(dto.userId() != null){
            tableSchemaRepository.findByUserIdAndSchemaName(dto.userId(),dto.schemaName())
                    .ifPresent(TableSchema::markExported);

        }
        return mockDataFileExporterContext.export(fileType, dto, rowCount);
    }

}
