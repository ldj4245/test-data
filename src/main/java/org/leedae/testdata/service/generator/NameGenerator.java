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
 * 영문 이름을 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class NameGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 인기 있는 영어 이름 (First Name)
    private static final List<String> FIRST_NAMES = List.of(
            // 남성 이름
            "James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas", "Charles",
            "Daniel", "Matthew", "Anthony", "Mark", "Donald", "Steven", "Paul", "Andrew", "Joshua", "Kenneth",
            "Kevin", "Brian", "George", "Timothy", "Ronald", "Jason", "Edward", "Jeffrey", "Ryan", "Jacob",
            // 여성 이름
            "Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Karen",
            "Lisa", "Nancy", "Betty", "Sandra", "Margaret", "Ashley", "Kimberly", "Emily", "Donna", "Michelle",
            "Carol", "Amanda", "Dorothy", "Melissa", "Deborah", "Stephanie", "Rebecca", "Laura", "Emma", "Olivia"
    );

    // 흔한 영어 성 (Last Name)
    private static final List<String> LAST_NAMES = List.of(
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
            "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin",
            "Lee", "Perez", "Thompson", "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson",
            "Walker", "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
            "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell", "Mitchell", "Carter", "Roberts"
    );

    @Override
    public MockDataType getType() {
        return MockDataType.NAME;
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

        Option option = new Option("full", false, null, null); // 기본 옵션 - 풀네임, 중간 이름 없음
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                Option parsedOption = mapper.readValue(typeOptionJson, Option.class);
                // null 체크를 통해 기본값 유지
                option = new Option(
                        parsedOption.format() != null ? parsedOption.format() : "full",
                        parsedOption.includeMiddleName() != null ? parsedOption.includeMiddleName() : false,
                        parsedOption.firstName(),
                        parsedOption.lastName()
                );
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        String firstName = option.firstName() != null && !option.firstName().isBlank() ?
                option.firstName() : FIRST_NAMES.get(RANDOM.nextInt(FIRST_NAMES.size()));

        String lastName = option.lastName() != null && !option.lastName().isBlank() ?
                option.lastName() : LAST_NAMES.get(RANDOM.nextInt(LAST_NAMES.size()));

        // 중간 이름 생성 (옵션 설정 시)
        String middleName = "";
        if (option.includeMiddleName() != null && option.includeMiddleName()) {
            char middleInitial = (char) ('A' + RANDOM.nextInt(26));
            middleName = " " + middleInitial + ".";
        }

        // 이름 형식에 따라 반환
        return switch (option.format().toLowerCase()) {
            case "first" -> firstName;
            case "last" -> lastName;
            case "full" -> firstName + middleName + " " + lastName;
            case "lastfirst" -> lastName + ", " + firstName + middleName;
            default -> firstName + middleName + " " + lastName;
        };
    }

    /**
     * 옵션 클래스
     */
    public record Option(String format, Boolean includeMiddleName, String firstName, String lastName) {
        // format: 이름 형식 ("full", "first", "last", "lastfirst")
        // includeMiddleName: 중간 이름(이니셜) 포함 여부
        // firstName: 특정 이름 지정 (null이면 랜덤)
        // lastName: 특정 성 지정 (null이면 랜덤)
    }
}
