package org.leedae.testdata.service.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;

/**
 * 날짜/시간 데이터를 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class DateTimeGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public MockDataType getType() {
        return MockDataType.DATETIME;
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

        // 기본 옵션: 현재 시간 기준으로 1년 전부터 1년 후까지의 날짜/시간
        LocalDateTime now = LocalDateTime.now();
        Option option = new Option(
                now.minusYears(1).format(DEFAULT_FORMATTER),
                now.plusYears(1).format(DEFAULT_FORMATTER),
                "yyyy-MM-dd'T'HH:mm:ss"
        );

        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.from() != null ? parsedOption.from() : option.from(),
                        parsedOption.to() != null ? parsedOption.to() : option.to(),
                        parsedOption.format() != null ? parsedOption.format() : "yyyy-MM-dd'T'HH:mm:ss"
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        DateTimeFormatter formatter;
        try {
            formatter = DateTimeFormatter.ofPattern(option.format);
        } catch (IllegalArgumentException e) {
            log.warn("날짜 형식 패턴이 잘못되었습니다. 기본 형식을 사용합니다: {}", e.getMessage());
            formatter = DEFAULT_FORMATTER;
        }

        try {
            // 시작 날짜와 종료 날짜 파싱
            LocalDateTime fromDate = LocalDateTime.parse(option.from, DEFAULT_FORMATTER);
            LocalDateTime toDate = LocalDateTime.parse(option.to, DEFAULT_FORMATTER);

            if (fromDate.isAfter(toDate)) {
                throw new IllegalArgumentException("[가짜 데이터 생성 옵션 오류] 시작 날짜가 종료 날짜보다 늦습니다 - option: " + typeOptionJson);
            }

            // 시작과 종료 사이의 랜덤 시간 생성
            long startSeconds = fromDate.atZone(ZoneId.systemDefault()).toEpochSecond();
            long endSeconds = toDate.atZone(ZoneId.systemDefault()).toEpochSecond();
            long randomSeconds = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);

            LocalDateTime randomDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(randomSeconds), ZoneId.systemDefault());

            // 지정된 형식으로 포맷팅하여 반환
            return randomDate.format(formatter);
        } catch (Exception e) {
            log.error("날짜 생성 중 오류 발생: {}", e.getMessage(), e);
            return LocalDateTime.now().format(formatter);  // 오류 발생 시 현재 시간 반환
        }
    }

    /**
     * 옵션 클래스
     */
    public record Option(String from, String to, String format) {
        // from: 시작 날짜/시간 (ISO 형식: yyyy-MM-dd'T'HH:mm:ss)
        // to: 종료 날짜/시간 (ISO 형식: yyyy-MM-dd'T'HH:mm:ss)
        // format: 출력 형식 (예: yyyy-MM-dd, yyyy/MM/dd HH:mm:ss)
    }
}
