package org.leedae.testdata.dto.response;

import org.leedae.testdata.domain.TableSchema;
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

    public static TableSchemaResponse fromEntity(TableSchema entity) {
        return new TableSchemaResponse(
                entity.getSchemaName(),
                entity.getUserId(),
                entity.getSchemaFields().stream().map(SchemaFieldResponse::fromEntity).toList()
        );
    }
}
