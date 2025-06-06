package org.leedae.testdata.service.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.random.RandomGenerator;

/**
 * Boolean 데이터를 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class BooleanGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;

    @Override
    public MockDataType getType() {
        return MockDataType.BOOLEAN;
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

        Option option = new Option(50); // 기본 옵션: 50% 확률로 true
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                option = mapper.readValue(typeOptionJson, Option.class);
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        // truePercent에 따라 true/false 결정
        boolean value = randomGenerator.nextInt(100) < option.truePercent;
        return String.valueOf(value);
    }

    /**
     * 옵션 클래스
     */
    public record Option(Integer truePercent) {
        // truePercent: true가 나올 확률 (0~100)
        // 50이면 true와 false가 동일한 확률
        // 75이면 true가 false보다 3배 더 자주 발생
    }
}
