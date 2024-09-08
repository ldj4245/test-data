package org.leedae.testdata.dto.response;

import org.leedae.testdata.dto.TableSchemaDto;

public record SimpleTableSchemaResponse(
        String schemaName,
        String userId
) {

    public static SimpleTableSchemaResponse fromDto(TableSchemaDto dto){
        return new SimpleTableSchemaResponse(
                dto.schemaName(),
                dto.userId()
        );
    }
}
