package org.leedae.testdata.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.leedae.testdata.domain.constant.ExportFileType;
import org.leedae.testdata.dto.TableSchemaDto;

import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class TableSchemaExportRequest {
    private String schemaName;
    private Integer rowCount;
    private ExportFileType fileType;
    private List<SchemaFieldRequest> schemaFields;


    public TableSchemaDto toDto(String userId){
        return TableSchemaDto.of(
                schemaName,
                userId,
                null,
                schemaFields.stream().map(SchemaFieldRequest::toDto).collect(Collectors.toSet())
        );

    }
}
