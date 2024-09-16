package org.leedae.testdata.service.exporter;

import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.SchemaFieldDto;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.service.generator.MockDataGeneratorContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class TSVFileExporter extends DelimiterBasedFileExporter implements MockDataFileExporter{


    public TSVFileExporter(MockDataGeneratorContext mockDataGeneratorContext) {
        super(mockDataGeneratorContext);
    }

    @Override
    public String getDelimiter() {
        return "\t";
    }

    @Override
    public ExportFileType getType() {
        return ExportFileType.TSV;
    }


}
