package org.leedae.testdata.service.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.random.RandomGenerator;

/**
 * 행 번호(ROW_NUMBER)를 생성하는 클래스
 * 스키마 이름 기준으로 행 번호를 관리하여 연속된 번호를 생성합니다.
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class RowNumberGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;

    // 스키마 이름별로 현재 행 번호를 추적하기 위한 맵
    private static final Map<String, Integer> SCHEMA_ROW_COUNTERS = new ConcurrentHashMap<>();

    @Override
    public MockDataType getType() {
        return MockDataType.ROW_NUMBER;
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

        Option option = new Option(1, 1, null); // 기본 옵션: 시작 1, 증가치 1
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.start() != null ? parsedOption.start() : 1,
                        parsedOption.step() != null ? parsedOption.step() : 1,
                        parsedOption.schemaName()
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        // 스키마 식별용 키 생성
        String schemaKey;
        if (option.schemaName != null && !option.schemaName.isBlank()) {
            schemaKey = option.schemaName;
        } else {
            // 스키마명이 없으면 옵션 기반 키 생성 (동일한 옵션이면 연속된 번호 사용)
            schemaKey = "default_start" + option.start + "_step" + option.step;
        }

        // option.start 값을 final 변수에 저장하여 lambda에서 참조할 수 있게 함
        final int startValue = option.start;
        
        // 현재 행 번호 계산 - 해당 스키마의 현재 값 또는 시작값 사용
        Integer currentRowNumber = SCHEMA_ROW_COUNTERS.computeIfAbsent(schemaKey, k -> startValue);

        // 현재 행 번호 반환 후 다음 번호 계산을 위해 증가
        int result = currentRowNumber;
        SCHEMA_ROW_COUNTERS.put(schemaKey, currentRowNumber + option.step);

        return String.valueOf(result);
    }

    /**
     * 특정 스키마의 행 번호 카운터를 리셋합니다.
     * @param schemaName 스키마 이름
     * @param startValue 시작 값
     */
    public void resetCounter(String schemaName, int startValue) {
        SCHEMA_ROW_COUNTERS.put(schemaName, startValue);
    }

    /**
     * 모든 스키마의 행 번호 카운터를 리셋합니다.
     */
    public void resetAllCounters() {
        SCHEMA_ROW_COUNTERS.clear();
    }

    /**
     * 옵션 클래스
     */
    public record Option(Integer start, Integer step, String schemaName) {
        // start: 시작 번호 (기본값: 1)
        // step: 증가치 (기본값: 1)
        // schemaName: 스키마 이름 (여러 스키마에서 각각 독립적인 행 번호 관리)
    }
}
