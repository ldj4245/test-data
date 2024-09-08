package org.leedae.testdata.dto.response;

import org.leedae.testdata.dto.TableSchemaDto;

import java.util.List;

public record TableSchemaResponse(
        String schemaName,
        String userId,
        List<SchemaFieldResponse> schemaFields
) {

    public static TableSchemaResponse fromDto(TableSchemaDto dto){
        return new TableSchemaResponse(
                dto.schemaName(),
                dto.userId(),
                dto.schemaFields().stream().map(SchemaFieldResponse::fromDto).toList()
        );

    }


}
