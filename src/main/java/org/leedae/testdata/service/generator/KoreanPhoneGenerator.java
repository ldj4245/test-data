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
 * 한국 전화번호를 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class KoreanPhoneGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 이동통신사 번호 (010, 011, 016, 017, 018, 019)
    private static final List<String> MOBILE_PREFIXES = List.of(
            "010", "010", "010", "010", "010", "010", "010", "010", // 010이 압도적으로 많음
            "011", "016", "017", "018", "019"
    );

    // 지역 번호 (대표적인 것들만)
    private static final List<String> AREA_CODES = List.of(
            "02",  // 서울
            "031", // 경기
            "032", // 인천
            "033", // 강원
            "041", // 충남
            "042", // 대전
            "043", // 충북
            "044", // 세종
            "051", // 부산
            "052", // 울산
            "053", // 대구
            "054", // 경북
            "055", // 경남
            "061", // 전남
            "062", // 광주
            "063", // 전북
            "064"  // 제주
    );

    // 기업 대표번호
    private static final List<String> OFFICE_PREFIXES = List.of(
            "1566", "1577", "1588", "1599", "1600", "1644", "1661", "1670", "1688", "1800", "1833", "1855", "1899"
    );

    @Override
    public MockDataType getType() {
        return MockDataType.KOREAN_PHONE;
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

        Option option = new Option("mobile"); // 기본 옵션은 휴대폰 번호
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.phoneType() != null ? parsedOption.phoneType() : "mobile"
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        switch (option.phoneType().toLowerCase()) {
            case "mobile":
                return generateMobilePhone();
            case "home":
                return generateHomePhone();
            case "office":
                return generateOfficePhone();
            default:
                return generateMobilePhone(); // 기본값은 휴대폰 번호
        }
    }

    /**
     * 휴대폰 번호 생성
     * 형식: 010-XXXX-XXXX (또는 다른 이동통신사 번호)
     */
    private String generateMobilePhone() {
        String prefix = MOBILE_PREFIXES.get(RANDOM.nextInt(MOBILE_PREFIXES.size()));
        String middle = String.format("%04d", RANDOM.nextInt(10000));
        String last = String.format("%04d", RANDOM.nextInt(10000));

        // 하이픈 포함 여부 랜덤
        if (RANDOM.nextBoolean()) {
            return String.format("%s-%s-%s", prefix, middle, last);
        } else {
            return String.format("%s%s%s", prefix, middle, last);
        }
    }

    /**
     * 집 전화번호 생성
     * 형식: 02-XXXX-XXXX 또는 지역번호-XXX-XXXX
     */
    private String generateHomePhone() {
        String areaCode = AREA_CODES.get(RANDOM.nextInt(AREA_CODES.size()));

        String middle, last;
        // 서울(02)인 경우 중간 번호가 3~4자리, 나머지 지역은 중간 번호가 3자리
        if (areaCode.equals("02")) {
            middle = String.format("%04d", RANDOM.nextInt(10000));
            last = String.format("%04d", RANDOM.nextInt(10000));
        } else {
            middle = String.format("%03d", RANDOM.nextInt(1000));
            last = String.format("%04d", RANDOM.nextInt(10000));
        }

        // 하이픈 포함 여부 랜덤
        if (RANDOM.nextBoolean()) {
            return String.format("%s-%s-%s", areaCode, middle, last);
        } else {
            return String.format("%s%s%s", areaCode, middle, last);
        }
    }

    /**
     * 회사/기업 전화번호 생성
     * 형식: 1588-XXXX 등의 대표번호 또는 지역번호로 시작하는 일반 전화번호
     */
    private String generateOfficePhone() {
        // 50% 확률로 대표번호 또는 일반 전화번호
        if (RANDOM.nextBoolean()) {
            // 대표번호 (1588-XXXX 등)
            String prefix = OFFICE_PREFIXES.get(RANDOM.nextInt(OFFICE_PREFIXES.size()));
            String last = String.format("%04d", RANDOM.nextInt(10000));

            return String.format("%s-%s", prefix, last);
        } else {
            // 지역번호로 시작하는 일반 번호
            return generateHomePhone(); // 일반 집 전화번호와 동일한 형식 사용
        }
    }

    /**
     * 전화번호 타입별 옵션을 정의하는 내부 클래스
     */
    public record Option(String phoneType) {}
}
