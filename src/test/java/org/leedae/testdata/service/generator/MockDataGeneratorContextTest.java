package org.leedae.testdata.service.generator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@DisplayName("[IntegrationTest] 파일 출력기 컨텍스트 테스트")
@SpringBootTest
record MockDataGeneratorContextTest(@Autowired MockDataGeneratorContext sut) {

    @DisplayName("문자열 가짜 데이터 타입이 주어지면, 랜덤 문자열을 반환한다.")
    @Test
    void givenStringType_whenGeneratingMockData_thenReturnsRandomString(){
        // given


        // when
        String result = sut.generate(MockDataType.STRING, 0, null, null);

        // then
        System.out.println(result);
        assertThat(result).containsPattern("^[가-힣]{1,10}$");
    }

}