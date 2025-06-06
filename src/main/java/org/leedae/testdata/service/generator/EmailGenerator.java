package org.leedae.testdata.service.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * 이메일 주소를 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class EmailGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 이메일 사용자명에 사용할 문자
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    // 일반적인 이메일 도메인 목록
    private static final List<String> EMAIL_DOMAINS = List.of(
            "gmail.com", "naver.com", "daum.net", "hotmail.com", "yahoo.com",
            "outlook.com", "icloud.com", "protonmail.com", "example.com",
            "company.co.kr", "hanmail.net", "kakao.com", "me.com", "live.com"
    );

    @Override
    public MockDataType getType() {
        return MockDataType.EMAIL;
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

        Option option = new Option(null, null, 5, 10); // 기본 옵션
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.username(),
                        parsedOption.domain(),
                        parsedOption.minLength() != null ? parsedOption.minLength() : 5,
                        parsedOption.maxLength() != null ? parsedOption.maxLength() : 10
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        // 도메인 선택 또는 랜덤 생성
        String domain = option.domain;
        if (domain == null || domain.isBlank()) {
            domain = EMAIL_DOMAINS.get(RANDOM.nextInt(EMAIL_DOMAINS.size()));
        }

        // 사용자 이름 선택 또는 랜덤 생성
        String username = option.username;
        if (username == null || username.isBlank()) {
            username = generateUsername(option.minLength, option.maxLength);
        }

        // 최종 이메일 생성
        return username + "@" + domain;
    }

    /**
     * 랜덤 사용자 이름 생성
     */
    private String generateUsername(int minLength, int maxLength) {
        if (minLength < 1) minLength = 1;
        if (maxLength < minLength) maxLength = minLength;

        int length = RANDOM.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder username = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char c = CHARS.charAt(RANDOM.nextInt(CHARS.length()));
            username.append(c);
        }

        // 난독성 향상을 위해 간혹 특수 문자와 숫자 조합 추가
        if (RANDOM.nextInt(100) < 30) {
            String[] specials = {".", "_", "-"};
            String special = specials[RANDOM.nextInt(specials.length)];

            int insertPosition = RANDOM.nextInt(username.length());
            username.insert(insertPosition, special);

            // 특수문자 뒤에 숫자 추가
            if (RANDOM.nextInt(100) < 70) {
                username.append(RANDOM.nextInt(1000));
            }
        }

        return username.toString();
    }

    /**
     * 옵션 클래스
     */
    public record Option(String username, String domain, Integer minLength, Integer maxLength) {
        // username: 특정 사용자 이름 지정 (null이면 랜덤 생성)
        // domain: 특정 도메인 지정 (null이면 랜덤 선택)
        // minLength: 랜덤 생성 시 사용자 이름 최소 길이
        // maxLength: 랜덤 생성 시 사용자 이름 최대 길이
    }
}
