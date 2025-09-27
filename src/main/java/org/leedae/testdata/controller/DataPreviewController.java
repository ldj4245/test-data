package org.leedae.testdata.controller;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.domain.constant.MockDataType;
import org.leedae.testdata.dto.request.TableSchemaPreviewRequest;
import org.leedae.testdata.dto.response.DataPreviewResponse;
import org.leedae.testdata.dto.security.GithubUser;
import org.leedae.testdata.service.DataPreviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DataPreviewController {

    private final DataPreviewService dataPreviewService;

    /**
     * 테이블 스키마를 기반으로 데이터 미리보기 생성
     * 클라이언트에서 Ajax로 호출하여 미리보기 데이터를 가져옴
     * 비로그인 상태에서도 미리보기 기능 사용 가능
     */
    @PostMapping("/api/preview")
    public ResponseEntity<DataPreviewResponse> previewData(
            @AuthenticationPrincipal GithubUser githubUser,
            @RequestBody TableSchemaPreviewRequest request) {

        // 로그인 상태에 관계없이 미리보기 데이터 생성
        DataPreviewResponse response = dataPreviewService.generatePreview(
                request.toDto(null), // userId를 null로 설정하여 비로그인 상태에서도 사용 가능
                request.getPreviewRowCount()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 개별 필드에 대한 실시간 미리보기 데이터 생성
     * 필드 타입과 옵션이 변경될 때마다 Ajax로 호출하여 샘플 데이터 표시
     */
    @PostMapping("/api/field-preview")
    public ResponseEntity<Map<String, Object>> previewSingleField(
            @AuthenticationPrincipal GithubUser githubUser,
            @RequestBody Map<String, Object> fieldRequest) {

        try {
            String fieldName = (String) fieldRequest.get("fieldName");
            String mockDataType = (String) fieldRequest.get("mockDataType");
            Integer blankPercent = (Integer) fieldRequest.getOrDefault("blankPercent", 0);
            String typeOptionJson = (String) fieldRequest.getOrDefault("typeOptionJson", "{}");

            // 5개의 샘플 데이터 생성
            List<String> samples = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                String sampleValue = dataPreviewService.generateSingleFieldValue(
                        MockDataType.valueOf(mockDataType),
                        blankPercent,
                        typeOptionJson
                );
                samples.add(sampleValue);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("fieldName", fieldName);
            response.put("samples", samples);
            response.put("count", samples.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "필드 미리보기 생성 중 오류 발생: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
}
