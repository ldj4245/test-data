package org.leedae.testdata.service.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.random.RandomGenerator;

/**
 * 숫자 데이터를 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class NumberGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;

    @Override
    public MockDataType getType() {
        return MockDataType.NUMBER;
    }

    @Override
    public String generate(Integer blankPercent, String typeOptionJson, String forceValue) {
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        if (randomGenerator.nextInt(100) < blankPercent) {
            return null;
        }

        if (forceValue != null && !forceValue.isBlank()) {
            return forceValue;
        }

        Option option = new Option(0.0, 100.0, 0); // 기본 옵션 (0~100 사이의 정수)
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.min() != null ? parsedOption.min() : 0.0,
                        parsedOption.max() != null ? parsedOption.max() : 100.0,
                        parsedOption.decimals() != null ? parsedOption.decimals() : 0
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        if (option.min > option.max) {
            throw new IllegalArgumentException("[가짜 데이터 생성 옵션 오류] 최소값이 최대값보다 큽니다 - option: " + typeOptionJson);
        }

        double range = option.max - option.min;
        double random = randomGenerator.nextDouble() * range + option.min;

        if (option.decimals <= 0) {
            // 정수 반환
            return String.valueOf((int) random);
        } else {
            // 소수점 자리 수 지정하여 반환
            BigDecimal bd = new BigDecimal(random);
            bd = bd.setScale(option.decimals, RoundingMode.HALF_UP);
            return bd.toString();
        }
    }

    /**
     * 옵션 클래스
     */
    public record Option(Double min, Double max, Integer decimals) {
        // min: 최소값
        // max: 최대값
        // decimals: 소수점 자릿수 (0이면 정수)
    }
}
