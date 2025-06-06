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
 * 한국 주소를 생성하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class KoreanAddressGenerator implements MockDataGenerator {

    private final ObjectMapper mapper;
    private static final Random RANDOM = new Random();

    // 시/도 목록
    private static final List<String> PROVINCES = List.of(
            "서울특별시", "부산광역시", "인천광역시", "대구광역시", "대전광역시", "광주광역시", "울산광역시", "세종특별자치시",
            "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"
    );

    // 서울시 구 목록
    private static final List<String> SEOUL_DISTRICTS = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구",
            "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );

    // 경기도 시/군 목록
    private static final List<String> GYEONGGI_DISTRICTS = List.of(
            "수원시", "성남시", "의정부시", "안양시", "부천시", "광명시", "평택시", "동두천시", "안산시", "고양시",
            "과천시", "구리시", "남양주시", "오산시", "시흥시", "군포시", "의왕시", "하남시", "용인시", "파주시",
            "이천시", "안성시", "김포시", "화성시", "광주시", "양주시", "포천시", "여주시", "연천군", "가평군", "양평군"
    );

    // 일반 시/군 목록 (비수도권)
    private static final List<String> COMMON_DISTRICTS = List.of(
            "창원시", "포항시", "진주시", "울산시", "청주시", "천안시", "전주시", "목포시", "여수시", "순천시", "안동시",
            "구미시", "춘천시", "원주시", "강릉시", "충주시", "제주시", "서귀포시", "익산시", "군산시", "경주시"
    );

    // 동/읍/면 목록
    private static final List<String> NEIGHBORHOODS = List.of(
            "중앙동", "신도시동", "행복동", "미래동", "꿈동", "희망동", "사랑동", "행운동", "번영동", "푸른동",
            "소망동", "새솔동", "평화동", "으뜸동", "바다동", "산동", "하늘동", "강동", "가람동", "화목동",
            "도안동", "상생동", "번창동", "송정동", "고운동", "아름동", "보람동", "다정동", "충만동", "해맞이동",
            "가온읍", "중심읍", "원흥읍", "능내읍", "송백읍", "내린읍", "한솔읍", "맑은읍", "문화읍", "진동읍",
            "사과면", "배꽃면", "논길면", "솔바람면", "햇살면", "신록면", "산내면", "호수면", "들판면", "논시면"
    );

    // 도로명 목록
    private static final List<String> ROAD_NAMES = List.of(
            "중앙로", "백제대로", "한강대로", "강남대로", "테헤란로", "삼성로", "압구정로", "종로", "세종대로", "광화문로",
            "을지로", "충청로", "영남로", "호남로", "중부로", "봉명로", "대학로", "과학로", "문화로", "평화로",
            "번영로", "소망로", "삼일대로", "국채보상로", "동대구로", "동해안로", "경부대로", "무역로", "번화로", "상업로",
            "산업로", "원도심로", "신도시로", "센트럴로", "메인로", "서해안로", "남해안로", "동해안로", "경의로", "경춘로"
    );

    @Override
    public MockDataType getType() {
        return MockDataType.KOREAN_ADDRESS;
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

        Option option = new Option("roadAddress"); // 기본 옵션은 도로명 주소
        try {
            if (typeOptionJson != null && !typeOptionJson.isBlank()) {
                option = mapper.readValue(typeOptionJson, Option.class);
            }
        } catch (JsonProcessingException e) {
            log.warn("Json 옵션 정보를 읽어들이는데 실패했습니다. 기본 옵션으로 동작합니다 - 입력 옵션 : {}, 필요 옵션 예 : {}", typeOptionJson, option);
        }

        // 시/도 선택
        String province = PROVINCES.get(RANDOM.nextInt(PROVINCES.size()));

        // 시/군/구 선택
        String district;
        if (province.equals("서울특별시")) {
            district = SEOUL_DISTRICTS.get(RANDOM.nextInt(SEOUL_DISTRICTS.size()));
        } else if (province.equals("경기도")) {
            district = GYEONGGI_DISTRICTS.get(RANDOM.nextInt(GYEONGGI_DISTRICTS.size()));
        } else {
            district = COMMON_DISTRICTS.get(RANDOM.nextInt(COMMON_DISTRICTS.size()));
        }

        if ("roadAddress".equalsIgnoreCase(option.addressType)) {
            return generateRoadAddress(province, district);
        } else {
            return generateJibunAddress(province, district);
        }
    }

    /**
     * 도로명 주소 생성
     */
    private String generateRoadAddress(String province, String district) {
        String roadName = ROAD_NAMES.get(RANDOM.nextInt(ROAD_NAMES.size()));
        int roadNumber = RANDOM.nextInt(200) + 1;

        // 건물 번호
        int buildingNumber = RANDOM.nextInt(100) + 1;
        String building = RANDOM.nextBoolean() ?
            buildingNumber + "번지 " + generateBuildingName() :
            buildingNumber + "번지";

        // 상세 주소
        String detail = generateDetailAddress();

        // 우편번호
        String zipCode = generateZipCode();

        return String.format("(%s) %s %s %s %s %s",
                zipCode, province, district, roadName, building, detail);
    }

    /**
     * 지번 주소 생성
     */
    private String generateJibunAddress(String province, String district) {
        String neighborhood = NEIGHBORHOODS.get(RANDOM.nextInt(NEIGHBORHOODS.size()));
        int mainNumber = RANDOM.nextInt(1000) + 1;
        int subNumber = RANDOM.nextBoolean() ? RANDOM.nextInt(30) : 0;

        // 상세 주소
        String detail = generateDetailAddress();

        // 우편번호
        String zipCode = generateZipCode();

        if (subNumber > 0) {
            return String.format("(%s) %s %s %s %d-%d %s",
                    zipCode, province, district, neighborhood, mainNumber, subNumber, detail);
        } else {
            return String.format("(%s) %s %s %s %d %s",
                    zipCode, province, district, neighborhood, mainNumber, detail);
        }
    }

    /**
     * 건물명 생성
     */
    private String generateBuildingName() {
        List<String> buildingPrefixes = List.of("행복", "미래", "그린", "블루", "신세계", "무지개", "하늘", "강산", "푸른", "해피", "드림");
        List<String> buildingSuffixes = List.of("아파트", "빌딩", "오피스텔", "상가", "타워", "센터", "프라자", "맨션", "파크", "하우스");

        String prefix = buildingPrefixes.get(RANDOM.nextInt(buildingPrefixes.size()));
        String suffix = buildingSuffixes.get(RANDOM.nextInt(buildingSuffixes.size()));

        return prefix + " " + suffix;
    }

    /**
     * 상세 주소 생성
     */
    private String generateDetailAddress() {
        List<String> detailPrefixes = List.of("", "가동", "나동", "다동", "A동", "B동");

        if (RANDOM.nextInt(100) < 30) { // 30% 확률로 상세주소 생성
            String prefix = detailPrefixes.get(RANDOM.nextInt(detailPrefixes.size()));
            int floor = RANDOM.nextInt(20) + 1;
            int room = RANDOM.nextInt(15) + 1;

            if (prefix.isEmpty()) {
                return String.format("%d층 %d호", floor, room);
            } else {
                return String.format("%s %d층 %d호", prefix, floor, room);
            }
        }

        return "";
    }

    /**
     * 우편번호 생성
     */
    private String generateZipCode() {
        return String.format("%05d", RANDOM.nextInt(90000) + 10000);
    }

    /**
     * 옵션 클래스 - 주소 타입 지정 가능
     */
    public record Option(String addressType) {
        // addressType: "roadAddress", "jibunAddress"
    }
}
