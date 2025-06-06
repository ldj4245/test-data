package org.leedae.testdata.controller;

import lombok.RequiredArgsConstructor;
import org.leedae.testdata.dto.request.TableSchemaExportRequest;
import org.leedae.testdata.dto.request.TableSchemaPreviewRequest;
import org.leedae.testdata.dto.response.DataPreviewResponse;
import org.leedae.testdata.dto.security.GithubUser;
import org.leedae.testdata.service.DataPreviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
