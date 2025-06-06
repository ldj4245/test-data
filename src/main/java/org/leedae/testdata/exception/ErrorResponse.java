package org.leedae.testdata.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * API 오류 응답을 위한 공통 형식 클래스
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    // 개발 모드에서만 포함될 상세 정보
    private final String debugMessage;

    /**
     * 기본 오류 응답 생성 메서드
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }

    /**
     * 개발용 상세 정보를 포함한 오류 응답 생성 메서드
     */
    public static ErrorResponse withDebugInfo(int status, String error, String message, String path, String debugMessage) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .debugMessage(debugMessage)
                .build();
    }
}
