package org.leedae.testdata.dto;

import org.leedae.testdata.domain.SchemaField;
import org.leedae.testdata.domain.constant.MockDataType;

import java.io.Serializable;
import java.time.LocalDateTime;


public record SchemaFieldDto(
        Long id,
        String fieldName,
        MockDataType mockDataType,
        Integer fieldOrder,
        Integer blankPercent,
        String typeOptionJson,
        String forceValue,

        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static SchemaFieldDto of(
            Long id,
            String fieldName,
            MockDataType mockDataType,
            Integer fieldOrder,
            Integer blankPercent,
            String typeOptionJson,
            String forceValue,
            LocalDateTime createdAt,
            String createdBy,
            LocalDateTime modifiedAt,
            String modifiedBy
    ) {
        return new SchemaFieldDto(
                id,
                fieldName,
                mockDataType,
                fieldOrder,
                blankPercent,
                typeOptionJson,
                forceValue,
                createdAt,
                createdBy,
                modifiedAt,
                modifiedBy
        );
    }

    public static SchemaFieldDto of(String fieldName, MockDataType mockDataType, Integer fieldOrder, Integer blankPercent, String typeOptionJson, String forceValue) {
        return new SchemaFieldDto(
                null,
                fieldName,
                mockDataType,
                fieldOrder,
                blankPercent,
                typeOptionJson,
                forceValue,
                null,
                null,
                null,
                null
        );
    }




    public static SchemaFieldDto fromEntity(SchemaField entity) {
        return new SchemaFieldDto(
                entity.getId(),
                entity.getFieldName(),
                entity.getMockDataType(),
                entity.getFieldOrder(),
                entity.getBlankPercent(),
                entity.getTypeOptionJson(),
                entity.getForceValue(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy());
    }

    public SchemaField createdEntity() {
        return SchemaField.of(
                this.fieldName(),
                this.mockDataType(),
                this.fieldOrder(),
                this.blankPercent(),
                this.typeOptionJson(),
                this.forceValue()
        );
    }


}
