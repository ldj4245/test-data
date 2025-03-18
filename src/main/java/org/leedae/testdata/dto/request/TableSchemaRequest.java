package org.leedae.testdata.dto.request;

import lombok.*;
import org.leedae.testdata.dto.TableSchemaDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class TableSchemaRequest {
    private String schemaName;
    private List<SchemaFieldRequest> schemaFields;
    private String userId;

    public TableSchemaDto toDto(String userId){
        return TableSchemaDto.of(
                schemaName,
                userId,
                null,
                schemaFields.stream().map(SchemaFieldRequest::toDto).collect(Collectors.toSet())
        );

    }
}
