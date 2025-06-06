package org.leedae.testdata.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum MockDataType {
    // 기본 데이터 타입
    STRING(Set.of("minLength", "maxLength"), null, "문자열", "기본"),
    NUMBER(Set.of("min", "max", "decimals"), null, "숫자", "기본"),
    BOOLEAN(Set.of(), null, "불리언", "기본"),
    DATETIME(Set.of("from", "to"), null, "날짜/시간", "기본"),
    ENUM(Set.of("minLength", "maxLength", "pattern"), null, "열거형", "기본"),

    // 확장 데이터 타입
    SENTENCE(Set.of("minSentences", "maxSentences"), STRING, "문장", "텍스트"),
    PARAGRAPH(Set.of("minParagraphs", "maxParagraphs"), STRING, "단락", "텍스트"),
    UUID(Set.of(), STRING, "UUID", "식별자"),
    EMAIL(Set.of(), STRING, "이메일", "개인정보"),
    CAR(Set.of(), STRING, "자동차", "기타"),
    ROW_NUMBER(Set.of("start, step"), NUMBER, "행 번호", "식별자"),
    NAME(Set.of(), STRING, "이름", "개인정보"),

    // 한글 데이터 생성을 위한 타입 추가
    KOREAN_NAME(Set.of("gender"), STRING, "한국인 이름", "한국어"),
    KOREAN_ADDRESS(Set.of("addressType"), STRING, "한국 주소", "한국어"),
    KOREAN_PHONE(Set.of("phoneType"), STRING, "한국 전화번호", "한국어")
    ;

    private final Set<String> requiredOptions;
    private final MockDataType baseType;
    private final String description; // 한글 설명 추가
    private final String category;    // 카테고리 추가

    private static final List<MockDataTypeObject> objects = Stream.of(values())
            .map(MockDataType::toObject)
            .toList();

    // 카테고리별로 그룹화된 MockDataTypeObject 목록 반환
    public static Map<String, List<MockDataTypeObject>> toGroupedObjects() {
        return Stream.of(values())
                .map(MockDataType::toObject)
                .collect(Collectors.groupingBy(MockDataTypeObject::category));
    }

    public static List<MockDataTypeObject> toObjects() {
        return objects;
    }

    public boolean isBaseType() {
        return baseType == null;
    }

    public MockDataTypeObject toObject() {
        return new MockDataTypeObject(
                this.name(),
                this.requiredOptions,
                this.baseType == null ? null : this.baseType.name(),
                this.description,
                this.category
        );
    }


    public record MockDataTypeObject(
            String name,
            Set<String> requiredOptions,
            String baseType,
            String description,
            String category
    ){}
}
