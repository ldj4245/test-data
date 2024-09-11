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


    //기존엔 TableSchemaRequest 필드에 userId도 포함이 되었지만, 외부에서 따로 주입해줘야 하므로 메서드 인자로 따로 받기
    public TableSchemaDto toDto(String userId){
        return TableSchemaDto.of(
                schemaName,
                userId,
                null,
                schemaFields.stream().map(SchemaFieldRequest::toDto).collect(Collectors.toSet())
        );

    }
}
