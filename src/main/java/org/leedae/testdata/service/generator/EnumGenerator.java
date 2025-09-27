package org.leedae.testdata.service.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * 열거형(ENUM) 값을 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class EnumGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 기본 열거형 값들
    private static final List<String> DEFAULT_VALUES = List.of(
            "ACTIVE", "INACTIVE", "PENDING", "COMPLETED", "CANCELLED",
            "ENABLED", "DISABLED", "DRAFT", "PUBLISHED", "ARCHIVED",
            "SUCCESS", "FAILED", "WARNING", "INFO", "ERROR",
            "HIGH", "MEDIUM", "LOW", "URGENT", "NORMAL",
            "PUBLIC", "PRIVATE", "PROTECTED", "INTERNAL",
            "YES", "NO", "MAYBE", "UNKNOWN", "OTHER"
    );

    @Override
    public MockDataType getType() {
        return MockDataType.ENUM;
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

        Option option = new Option(DEFAULT_VALUES, null); // 기본 옵션
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                
                List<String> values = DEFAULT_VALUES;
                if (parsedOption.values() != null && !parsedOption.values().isEmpty()) {
                    values = parsedOption.values();
                } else if (parsedOption.pattern() != null && !parsedOption.pattern().isBlank()) {
                    // 패턴이 제공된 경우 (예: "A,B,C" 또는 "VALUE1|VALUE2|VALUE3")
                    String[] splitValues = parsedOption.pattern().split("[,|;]");
                    values = Arrays.stream(splitValues)
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .toList();
                }
                
                option = new Option(values, parsedOption.pattern());
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        if (option.values().isEmpty()) {
            return DEFAULT_VALUES.get(RANDOM.nextInt(DEFAULT_VALUES.size()));
        }

        // 제공된 값들 중에서 랜덤 선택
        return option.values().get(RANDOM.nextInt(option.values().size()));
    }

    /**
     * 옵션 클래스
     */
    public record Option(List<String> values, String pattern) {
        // values: 열거형 값들의 리스트
        // pattern: 쉼표나 파이프로 구분된 값들 (예: "A,B,C" 또는 "VALUE1|VALUE2")
    }
}
