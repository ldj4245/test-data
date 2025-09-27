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
 * 문장을 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class SentenceGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 다양한 주제의 한국어 문장들
    private static final List<String> SAMPLE_SENTENCES = List.of(
            "오늘 날씨가 정말 좋습니다.",
            "회의가 내일 오후 2시에 예정되어 있습니다.",
            "새로운 프로젝트를 시작하게 되었습니다.",
            "커피 한 잔의 여유가 필요한 시간입니다.",
            "책을 읽으며 하루를 마무리하겠습니다.",
            "가족과 함께 보내는 시간이 소중합니다.",
            "운동을 통해 건강을 관리하고 있습니다.",
            "새로운 기술을 배우는 것은 즐거운 일입니다.",
            "음악을 들으며 산책하는 것을 좋아합니다.",
            "요리를 하면서 스트레스를 해소합니다.",
            "친구들과의 만남이 기대됩니다.",
            "독서는 지식을 쌓는 좋은 방법입니다.",
            "여행을 통해 새로운 경험을 쌓고 싶습니다.",
            "일과 삶의 균형을 찾는 것이 중요합니다.",
            "창의적인 아이디어가 필요한 상황입니다.",
            "팀워크가 성공의 핵심입니다.",
            "지속적인 학습이 성장의 원동력입니다.",
            "긍정적인 마음가짐으로 하루를 시작합니다.",
            "도전을 통해 더 나은 사람이 되겠습니다.",
            "감사하는 마음으로 매일을 살아갑니다."
    );

    @Override
    public MockDataType getType() {
        return MockDataType.SENTENCE;
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

        Option option = new Option(1, 1); // 기본 옵션: 1개 문장
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.minSentences() != null ? parsedOption.minSentences() : 1,
                        parsedOption.maxSentences() != null ? parsedOption.maxSentences() : 1
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        if (option.minSentences() < 1) {
            throw new IllegalArgumentException("[가짜 데이터 생성 옵션 오류] 최소 문장 수가 1보다 작습니다 - option: " + typeOptionJson);
        }
        if (option.maxSentences() < option.minSentences()) {
            throw new IllegalArgumentException("[가짜 데이터 생성 옵션 오류] 최대 문장 수가 최소 문장 수보다 작습니다 - option: " + typeOptionJson);
        }

        // 문장 수 결정
        int sentenceCount = option.minSentences();
        if (option.maxSentences() > option.minSentences()) {
            sentenceCount = RANDOM.nextInt(option.maxSentences() - option.minSentences() + 1) + option.minSentences();
        }

        // 문장들 선택하고 연결
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sentenceCount; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(SAMPLE_SENTENCES.get(RANDOM.nextInt(SAMPLE_SENTENCES.size())));
        }

        return result.toString();
    }

    /**
     * 옵션 클래스
     */
    public record Option(Integer minSentences, Integer maxSentences) {
        // minSentences: 최소 문장 수
        // maxSentences: 최대 문장 수
    }
}
