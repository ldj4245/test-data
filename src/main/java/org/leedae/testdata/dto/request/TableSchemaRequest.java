package org.leedae.testdata.dto.request;

import org.leedae.testdata.dto.TableSchemaDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record TableSchemaRequest(
        String schemaName,
        String userId,
        List<SchemaFieldRequest> schemaFields
)
{
 

    public TableSchemaDto toDto(){
        return TableSchemaDto.of(
                schemaName,
                userId,
                null,
                schemaFields.stream().map(SchemaFieldRequest::toDto).collect(Collectors.toSet())
        );

    }
}
