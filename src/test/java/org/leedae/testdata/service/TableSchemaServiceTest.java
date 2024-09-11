package org.leedae.testdata.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.leedae.testdata.domain.TableSchema;
import org.leedae.testdata.dto.TableSchemaDto;
import org.leedae.testdata.repository.TableSchemaRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@DisplayName("[Service] 테이블 스키마 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableSchemaServiceTest {

    @InjectMocks private TableSchemaService sut;

    @Mock private TableSchemaRepository tableSchemaRepository;


    @DisplayName("사용자 ID가 주어지면, 테이블 스키마 목록을 반환한다.")
    @Test
    void givenUserId_whenLoadingMySchemas_thenReturnsTableSchemaList() {
        // Given
        String userId = "userId";
        given(tableSchemaRepository.findByUserId(userId, Pageable.unpaged())).willReturn(new PageImpl<>(List.of(
                TableSchema.of("table1", userId),
                TableSchema.of("table2", userId),
                TableSchema.of("table3", userId)
        )));

        // When
        List<TableSchemaDto> result = sut.loadMySchemas(userId);

        // Then
        assertThat(result)
                .hasSize(3)
                .extracting("schemaName")
                .containsExactly("table1", "table2", "table3");
        then(tableSchemaRepository).should().findByUserId(userId, Pageable.unpaged());
    }

}