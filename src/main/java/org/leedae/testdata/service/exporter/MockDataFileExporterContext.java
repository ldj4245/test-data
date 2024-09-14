package org.leedae.testdata.service.exporter;


import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.TableSchemaDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MockDataFileExporterContext {

    private final Map<ExportFileType, MockDataFileExporter> mockDataFileExporterMap;

    public MockDataFileExporterContext(List<MockDataFileExporter> mockDataFileExporters){
        this.mockDataFileExporterMap = mockDataFileExporters.stream()
                .collect(Collectors.toMap(MockDataFileExporter::getType, Function.identity()));

    }

    public String export(ExportFileType fileType, TableSchemaDto dto, Integer rowCount){
        //원하는 구현체 (전략 패턴의 일종)
        MockDataFileExporter fileExporter = mockDataFileExporterMap.get(fileType);

        return fileExporter.export(dto, rowCount);

    }

}
