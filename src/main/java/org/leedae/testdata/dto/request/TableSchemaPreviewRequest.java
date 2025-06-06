package org.leedae.testdata.dto.request;

import lombok.*;
import org.leedae.testdata.dto.TableSchemaDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TableSchemaPreviewRequest {
    private String schemaName;
    private List<SchemaFieldRequest> schemaFields;
    private int previewRowCount;

    public TableSchemaDto toDto(String userId) {
        return TableSchemaDto.of(
                schemaName,
                userId,
                null,
                schemaFields.stream().map(SchemaFieldRequest::toDto).collect(Collectors.toSet())
        );
    }
}
