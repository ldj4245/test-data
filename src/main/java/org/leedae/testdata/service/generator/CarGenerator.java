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
 * 자동차 관련 데이터를 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class CarGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 자동차 제조사 목록
    private static final List<String> MANUFACTURERS = List.of(
            "현대", "기아", "제네시스", "르노코리아", "쌍용", "BMW", "벤츠", "아우디", "폭스바겐",
            "토요타", "렉서스", "혼다", "닛산", "포드", "쉐보레", "테슬라", "볼보", "페라리",
            "람보르기니", "포르쉐", "재규어", "랜드로버", "링컨", "캐딜락", "지프"
    );

    // 자동차 모델 접두사/접미사
    private static final List<String> MODEL_PREFIXES = List.of(
            "그랜드", "뉴", "올 뉴", "더 뉴", "스포티", "프리미엄", "시그니처", "익스클루시브",
            "스페셜", "슈퍼", "울트라", "GT", "RS", "프로", "어드밴스드", "하이브리드", "일렉트릭"
    );

    // 자동차 모델명
    private static final List<String> MODEL_NAMES = List.of(
            "소나타", "아반떼", "그랜저", "K5", "K8", "K9", "스포티지", "카니발", "쏘렌토",
            "투싼", "싼타페", "팰리세이드", "코나", "3시리즈", "5시리즈", "X5", "E클래스", "S클래스",
            "A4", "Q7", "티구안", "골프", "캠리", "코롤라", "아코드", "알티마", "머스탱", "F150",
            "말리부", "카마로", "모델S", "모델3", "XC90", "458", "아벤타도르", "911", "케이맨",
            "F-페이스", "레인지로버", "내비게이터", "에스컬레이드", "랭글러", "체로키"
    );

    // 차체 유형
    private static final List<String> BODY_TYPES = List.of(
            "세단", "SUV", "해치백", "쿠페", "왜건", "컨버터블", "픽업트럭", "미니밴", "크로스오버"
    );

    // 연료 유형
    private static final List<String> FUEL_TYPES = List.of(
            "가솔린", "디젤", "LPG", "전기", "하이브리드", "플러그인 하이브리드", "수소", "CNG"
    );

    // 색상
    private static final List<String> COLORS = List.of(
            "검정색", "흰색", "은색", "회색", "파란색", "빨간색", "녹색", "갈색", "베이지색",
            "진주색", "청색", "노란색", "오렌지색", "자주색", "금색", "청록색"
    );

    @Override
    public MockDataType getType() {
        return MockDataType.CAR;
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

        Option option = new Option("full"); // 기본 옵션: 모든 정보 포함
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.format() != null ? parsedOption.format() : "full"
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        // 제조사 선택
        String manufacturer = MANUFACTURERS.get(RANDOM.nextInt(MANUFACTURERS.size()));

        // 모델명 생성
        String modelName = MODEL_NAMES.get(RANDOM.nextInt(MODEL_NAMES.size()));
        // 50% 확률로 접두사 추가
        if (RANDOM.nextBoolean()) {
            modelName = MODEL_PREFIXES.get(RANDOM.nextInt(MODEL_PREFIXES.size())) + " " + modelName;
        }

        // 연식 생성 (2000-2024)
        int year = 2000 + RANDOM.nextInt(25);

        // 차체 유형
        String bodyType = BODY_TYPES.get(RANDOM.nextInt(BODY_TYPES.size()));

        // 연료 유형
        String fuelType = FUEL_TYPES.get(RANDOM.nextInt(FUEL_TYPES.size()));

        // 색상
        String color = COLORS.get(RANDOM.nextInt(COLORS.size()));

        // 옵션에 따라 다양한 형식으로 반환
        return switch (option.format.toLowerCase()) {
            case "manufacturer" -> manufacturer;
            case "model" -> modelName;
            case "year" -> String.valueOf(year);
            case "bodytype" -> bodyType;
            case "fueltype" -> fuelType;
            case "color" -> color;
            case "manufacturermodel" -> manufacturer + " " + modelName;
            case "yearmodel" -> year + "년형 " + modelName;
            case "full" -> year + "년형 " + manufacturer + " " + modelName + " (" + bodyType + ", " + fuelType + ", " + color + ")";
            default -> year + "년형 " + manufacturer + " " + modelName;
        };
    }

    /**
     * 옵션 클래스
     */
    public record Option(String format) {
        // format: 출력 형식
        // - "manufacturer": 제조사만
        // - "model": 모델명만
        // - "year": 연식만
        // - "bodytype": 차체 유형만
        // - "fueltype": 연료 유형만
        // - "color": 색상만
        // - "manufacturermodel": 제조사 + 모델명
        // - "yearmodel": 연식 + 모델명
        // - "full": 모든 정보 포함
    }
}
