package org.leedae.testdata.service.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * 불리언 값을 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class BooleanGenerator implements MockDataGenerator {

    private static final Random RANDOM = new Random();

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

        // 50% 확률로 true/false 반환
        return RANDOM.nextBoolean() ? "true" : "false";
    }
}
