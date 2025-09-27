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
 * 단락(여러 문장)을 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class ParagraphGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 다양한 주제의 한국어 단락들
    private static final List<String> SAMPLE_PARAGRAPHS = List.of(
            "현대 사회에서 기술의 발전은 놀라운 속도로 진행되고 있습니다. 인공지능과 빅데이터 분석 기술이 우리의 일상생활 깊숙이 자리 잡고 있습니다. 이러한 변화는 새로운 기회와 도전을 동시에 제공하고 있습니다.",
            
            "건강한 라이프스타일을 유지하는 것은 매우 중요합니다. 규칙적인 운동과 균형 잡힌 식단이 필수적입니다. 충분한 수면과 스트레스 관리도 건강한 삶의 핵심 요소라고 할 수 있습니다.",
            
            "독서는 지식을 쌓고 사고력을 기르는 최고의 방법 중 하나입니다. 다양한 장르의 책을 읽으면서 새로운 관점을 얻을 수 있습니다. 책을 통해 간접 경험을 쌓고 창의성을 발휘할 수 있습니다.",
            
            "팀워크는 조직의 성공을 위한 필수 조건입니다. 서로 다른 배경과 경험을 가진 사람들이 협력할 때 시너지가 발생합니다. 원활한 의사소통과 상호 존중이 효과적인 협업의 기반이 됩니다.",
            
            "환경 보호는 우리 모두의 책임입니다. 일상생활에서 작은 실천들이 모여 큰 변화를 만들어냅니다. 재활용, 에너지 절약, 친환경 제품 사용 등이 지구를 지키는 방법입니다.",
            
            "평생 학습의 시대가 도래했습니다. 급변하는 사회에 적응하기 위해서는 지속적인 학습이 필요합니다. 새로운 기술과 지식을 습득하는 것이 경쟁력을 유지하는 핵심입니다.",
            
            "여행은 새로운 문화와 사람들을 만나는 소중한 기회입니다. 다른 지역의 음식과 전통을 경험하면서 세상을 보는 시야가 넓어집니다. 여행을 통해 얻은 추억과 경험은 평생의 자산이 됩니다.",
            
            "창의성은 문제 해결의 핵심 역량입니다. 고정관념에서 벗어나 새로운 시각으로 접근할 때 혁신적인 해결책을 찾을 수 있습니다. 다양한 경험과 학습이 창의적 사고를 기르는 데 도움이 됩니다."
    );

    @Override
    public MockDataType getType() {
        return MockDataType.PARAGRAPH;
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

        Option option = new Option(1, 1); // 기본 옵션: 1개 단락
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.minParagraphs() != null ? parsedOption.minParagraphs() : 1,
                        parsedOption.maxParagraphs() != null ? parsedOption.maxParagraphs() : 1
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        if (option.minParagraphs() < 1) {
            throw new IllegalArgumentException("[가짜 데이터 생성 옵션 오류] 최소 단락 수가 1보다 작습니다 - option: " + typeOptionJson);
        }
        if (option.maxParagraphs() < option.minParagraphs()) {
            throw new IllegalArgumentException("[가짜 데이터 생성 옵션 오류] 최대 단락 수가 최소 단락 수보다 작습니다 - option: " + typeOptionJson);
        }

        // 단락 수 결정
        int paragraphCount = option.minParagraphs();
        if (option.maxParagraphs() > option.minParagraphs()) {
            paragraphCount = RANDOM.nextInt(option.maxParagraphs() - option.minParagraphs() + 1) + option.minParagraphs();
        }

        // 단락들 선택하고 연결
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < paragraphCount; i++) {
            if (i > 0) {
                result.append("\n\n");  // 단락 간 구분
            }
            result.append(SAMPLE_PARAGRAPHS.get(RANDOM.nextInt(SAMPLE_PARAGRAPHS.size())));
        }

        return result.toString();
    }

    /**
     * 옵션 클래스
     */
    public record Option(Integer minParagraphs, Integer maxParagraphs) {
        // minParagraphs: 최소 단락 수
        // maxParagraphs: 최대 단락 수
    }
}
