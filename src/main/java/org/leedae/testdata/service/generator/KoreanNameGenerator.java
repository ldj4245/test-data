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
 * 한국인 이름을 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class KoreanNameGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 흔한 한국 성씨 (인구 분포 비율 고려)
    private static final List<String> LAST_NAMES = List.of(
            "김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안", "송", "류", "전",
            "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주", "우",
            "구", "신", "임", "나", "전", "민", "유", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염",
            "양", "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "노", "위", "표", "명"
    );

    // 남성 이름에 자주 사용되는 글자
    private static final List<String> MALE_NAME_CHARS = List.of(
            "준", "민", "현", "우", "건", "도", "영", "수", "상", "정", "재", "호", "성", "석", "진", "용", "동", "찬", "형", "빈",
            "태", "훈", "규", "일", "근", "상", "원", "명", "환", "인", "범", "식", "철", "권", "혁", "대", "열", "휘", "종", "선",
            "광", "기", "승", "윤", "창", "경", "연", "중", "균", "완", "학", "관", "석", "효", "덕"
    );

    // 여성 이름에 자주 사용되는 글자
    private static final List<String> FEMALE_NAME_CHARS = List.of(
            "서", "지", "예", "하", "윤", "수", "민", "채", "영", "정", "은", "현", "진", "주", "나", "승", "유", "혜", "원", "미",
            "다", "연", "아", "소", "선", "희", "인", "재", "빈", "랑", "림", "지", "려", "온", "해", "슬", "솔", "린", "율", "희",
            "라", "보", "나", "별", "이", "가", "람", "비", "설", "진", "경", "유", "리", "송", "환", "담", "화", "영", "민"
    );

    @Override
    public MockDataType getType() {
        return MockDataType.KOREAN_NAME;
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

        Option option = new Option(null); // 기본 옵션은 성별 랜덤
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                option = mapper.readValue(typeOptionJson, Option.class);
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        // 성 선택 (인구 분포 고려한 가중치 반영됨)
        String lastName = LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));

        // 성별에 따른 이름 생성
        String firstName;
        if ("male".equalsIgnoreCase(option.gender)) {
            firstName = generateFirstName(MALE_NAME_CHARS);
        } else if ("female".equalsIgnoreCase(option.gender)) {
            firstName = generateFirstName(FEMALE_NAME_CHARS);
        } else {
            // 성별이 지정되지 않은 경우 남/여 랜덤 선택
            firstName = RANDOM.nextBoolean()
                ? generateFirstName(MALE_NAME_CHARS)
                : generateFirstName(FEMALE_NAME_CHARS);
        }

        return lastName + firstName;
    }

    /**
     * 이름 부분 생성 (1~2글자)
     */
    private String generateFirstName(List<String> nameChars) {
        int nameLength = RANDOM.nextInt(100) < 80 ? 2 : 1; // 80% 확률로 2글자, 20% 확률로 1글자 이름
        StringBuilder name = new StringBuilder();

        for (int i = 0; i < nameLength; i++) {
            String ch = nameChars.get(RANDOM.nextInt(nameChars.size()));
            name.append(ch);
        }

        return name.toString();
    }

    /**
     * 옵션 클래스 - 성별 지정 가능
     */
    public record Option(String gender) {
        // gender: "male", "female", null(랜덤)
    }
}
