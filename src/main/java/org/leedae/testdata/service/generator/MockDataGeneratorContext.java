package org.leedae.testdata.service.generator;

import org.leedae.testdata.domain.constant.MockDataType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MockDataGeneratorContext {


    private final Map<MockDataType, MockDataGenerator> mockDataGeneratorMap;

    public MockDataGeneratorContext(List<MockDataGenerator> mockDataGenerators) {
        this.mockDataGeneratorMap = mockDataGenerators.stream()
                .collect(Collectors.toMap(MockDataGenerator::getType, Function.identity()));
    }

    public String generate(MockDataType mockDataType, Integer blankPercent, String typeOptionJson, String forceValue){
        MockDataGenerator generator = mockDataGeneratorMap.get(mockDataType);


        //TODO: 다양한 가짜 데이터 생성기가 도입되면, 이 기본값 강제 설정 코드를 삭제할 예정
        if(generator == null){
            generator = mockDataGeneratorMap.get(MockDataType.STRING);
        }


        return generator.generate(blankPercent, typeOptionJson, forceValue);
    }
}
