package org.leedae.testdata.service.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.leedae.testdata.domain.MockData;
import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.repository.MockDataRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("[Logic] 문자열 데이터 생성기 테스트")
@ExtendWith(MockitoExtension.class)
class StringGeneratorTest {

    @InjectMocks private StringGenerator sut;

    @Mock private MockDataRepository mockDataRepository;
    @Spy private ObjectMapper mapper;

    @DisplayName("이 테스트는 문자열 타입의 가짜 데이터를 다룬다.")
    @Test
    void givenNothing_whenCheckingType_thenReturnsStringType(){
        //Given

        //When

        //Then
        assertThat(sut.getType()).isEqualTo(MockDataType.STRING);
    }

    @DisplayName("옵션에 따라 정규식을 통과하는 랜덤 문자열을 생성한다.")
    @RepeatedTest(10)
    void givenParams_whenGenerating_thenReturnsRandomText() throws JsonProcessingException {
        // Given
        MockDataType mockDataType = MockDataType.STRING;
        StringGenerator.Option option = new StringGenerator.Option(1,10);
        String optionJson = mapper.writeValueAsString(option);
        given(mockDataRepository.findByMockDataType(mockDataType)).willReturn(
                List.of(
                        MockData.of(mockDataType,"하얗게 피어난 얼음 꽃 하나가 달가운 바람에 얼굴을 내밀어 아무 "),
                        MockData.of(mockDataType,"도로에 떨어진 빗방울이 빛나는 것은 빛이 아니라 빗방울 자체가 빛나는 것이다."),
                        MockData.of(mockDataType,"눈이 내리는 날에는 눈이 내리는 것만으로도 행복하다.")
                )
        );

        // When
        String result = sut.generate(0,optionJson, null);

        // Then
        System.out.println("result = " + result);
        assertThat(result).containsPattern("^[가-힣]{1,10}$");
        then(mapper).should().readValue(optionJson, StringGenerator.Option.class);
        then(mockDataRepository).should().findByMockDataType(mockDataType);

    }

    @CsvSource(delimiter = '|', textBlock = """
            {"minLength":1,"maxLength":1}   |   ^[가-힣]{1,1}$
            {"minLength":1,"maxLength":2}   |   ^[가-힣]{1,2}$
            {"minLength":1,"maxLength":3}   |   ^[가-힣]{1,3}$
            {"minLength":1,"maxLength":10}  |   ^[가-힣]{1,10}$
            {"minLength":5,"maxLength":10}  |   ^[가-힣]{5,10}$
            {"minLength":6,"maxLength":10}  |   ^[가-힣]{6,10}$
            {"minLength":7,"maxLength":10}  |   ^[가-힣]{7,10}$
            {"minLength":9,"maxLength":10}  |   ^[가-힣]{9,10}$
    """)
    @DisplayName("옵션에 따라 정규식을 통과하는 랜덤 문자열을 생성한다.")
    @ParameterizedTest(name = "{index}. {0} ===> 통과 정규식 : {1}")
    void givenParams_whenGenerating_thenReturnsRandomText(String optionJson, String expectedRegex) throws JsonProcessingException {
        // Given
        MockDataType mockDataType = MockDataType.STRING;
        given(mockDataRepository.findByMockDataType(mockDataType)).willReturn(
                List.of(
                        MockData.of(mockDataType,"하얗게 피어난 얼음 꽃 하나가 달가운 바람에 얼굴을 내밀어 아무 "),
                        MockData.of(mockDataType,"도로에 떨어진 빗방울이 빛나는 것은 빛이 아니라 빗방울 자체가 빛나는 것이다."),
                        MockData.of(mockDataType,"눈이 내리는 날에는 눈이 내리는 것만으로도 행복하다.")
                )
        );

        // When
        String result = sut.generate(0,optionJson, null);

        // Then
        System.out.println("result = " + result);
        assertThat(result).containsPattern(expectedRegex);
        then(mapper).should().readValue(optionJson, StringGenerator.Option.class);
        then(mockDataRepository).should().findByMockDataType(mockDataType);

    }

    @DisplayName("잘못된 옵션 내용이 주어지면, 예외를 던진다.")
    @CsvSource(delimiter = '|', textBlock = """
            {"minLength":0,"maxLength":0}
            {"minLength":0,"maxLength":1}
            {"minLength":0,"maxLength":10}
            {"minLength":0,"maxLength":11}
            {"minLength":1,"maxLength":11}
            {"minLength":2,"maxLength":1}
            {"minLength":5,"maxLength":1}
            {"minLength":10,"maxLength":9}
            """)
    @ParameterizedTest(name = "{index}. 잘못된 옵션 - {0}")
    void givenWrongOption_whenGenerating_thenThrowsException(String optionJson) throws JsonProcessingException {
        // Given

        // When
        Throwable t = catchThrowable(() -> sut.generate(0,optionJson, null));

        // Then
        assertThat(t)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("[가짜 데이터 생성 옵션 오류]");
        then(mapper).should().readValue(optionJson, StringGenerator.Option.class);
        then(mockDataRepository).shouldHaveNoInteractions();


    }

    @DisplayName("옵션이 없으면, 기본 옵션으로 정규식을 통과하는 랜덤 문자열을 생성한다.")
    @RepeatedTest(10)
    void givenNoParams_whenGenerating_thenReturnsRandomText(){
        //Given
        MockDataType mockDataType = sut.getType();
        given(mockDataRepository.findByMockDataType(mockDataType)).willReturn(
                List.of(
                        MockData.of(mockDataType,"하얗게 피어난 얼음 꽃 하나가 달가운 바람에 얼굴을 내밀어 아무 "),
                        MockData.of(mockDataType,"도로에 떨어진 빗방울이 빛나는 것은 빛이 아니라 빗방울 자체가 빛나는 것이다."),
                        MockData.of(mockDataType,"눈이 내리는 날에는 눈이 내리는 것만으로도 행복하다.")
                ));

        // when
        String result = sut.generate(0, null, null);

        //Then
        System.out.println(result);
        assertThat(result).containsPattern("^[가-힣]{1,10}$");
        then(mapper).shouldHaveNoInteractions();
        then(mockDataRepository).should().findByMockDataType(mockDataType);

    }

    @DisplayName("옵션 json 형식이 잘못되었으면, 기본 옵션으로 정규식을 통과하는 랜덤 문자열을 생성한다.")
    @Test
    void givenWrongOptionJson_whenGenerating_thenReturnsRandomText() throws Exception{
        //Given
        MockDataType mockDataType = sut.getType();
        String wrongJson = "{wrong json format}";
        given(mockDataRepository.findByMockDataType(mockDataType)).willReturn(
                List.of(
                        MockData.of(mockDataType,"하얗게 피어난 얼음 꽃 하나가 달가운 바람에 얼굴을 내밀어 아무 "),
                        MockData.of(mockDataType,"도로에 떨어진 빗방울이 빛나는 것은 빛이 아니라 빗방울 자체가 빛나는 것이다."),
                        MockData.of(mockDataType,"눈이 내리는 날에는 눈이 내리는 것만으로도 행복하다.")
                ));

        // When
        String result = sut.generate(0, wrongJson, null);

        // Then
        System.out.println("result = " + result);
        assertThat(result).containsPattern("^[가-힣]{1,10}$");
        then(mapper).should().readValue(wrongJson, StringGenerator.Option.class);
        then(mockDataRepository).should().findByMockDataType(mockDataType);

    }

    @DisplayName("blank 옵션이 100%면, null을 생성한다.")
    @RepeatedTest(10)
    void givenParams_whenGenerating_thenReturnsNull(){
        //Given
        MockDataType mockDataType = MockDataType.STRING;

        //When
        String result = sut.generate(100, null, null);

        //Then
        assertThat(result).isEqualTo(null);
        then(mapper).shouldHaveNoInteractions();
        then(mockDataRepository).shouldHaveNoInteractions();
    }

    @DisplayName("강제 값이 주어지면, 그 값을 반환한다.")
    @Test
    void givenForceValue_whenGenerating_thenReturnsForceValue(){
        //Given
        String forceValue = "강제로 주어진 값";

        //When
        String result = sut.generate(0, null, forceValue);

        //Then
        assertThat(result).isEqualTo(forceValue);
        then(mapper).shouldHaveNoInteractions();
        then(mockDataRepository).shouldHaveNoInteractions();
    }

    @DisplayName("강제 값이 주어지더라도, blank 옵션에 따라 null을 반환할 수 있다.")
    @Test
    void givenBlankOptionAndForceValue_whenGenerating_thenReturnsNull(){
        //Given
        int blankPercent = 100;
        String forceValue = "강제로 주어진 값";

        //When
        String result = sut.generate(blankPercent, null, forceValue);

        //Then
        assertThat(result).isEqualTo(null);
        then(mapper).shouldHaveNoInteractions();
        then(mockDataRepository).shouldHaveNoInteractions();
    }






}