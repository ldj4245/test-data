package org.leedae.testdata.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.leedae.testdata.exception.custom.InvalidDataException;
import org.leedae.testdata.exception.custom.OptionParsingException;
import org.leedae.testdata.exception.custom.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 애플리케이션 전체에 대한 예외 처리를 담당하는 전역 예외 처리기
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        log.error("Business exception occurred: {}", ex.getMessage());

        ErrorResponse errorResponse = isDevelopmentMode() ?
                ErrorResponse.withDebugInfo(
                        ex.getStatus().value(),
                        ex.getStatus().getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI(),
                        getStackTrace(ex)
                ) :
                ErrorResponse.of(
                        ex.getStatus().value(),
                        ex.getStatus().getReasonPhrase(),
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

    /**
     * 리소스를 찾을 수 없는 예외 처리
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        log.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 옵션 파싱 예외 처리
     */
    @ExceptionHandler(OptionParsingException.class)
    public ResponseEntity<ErrorResponse> handleOptionParsingException(
            OptionParsingException ex, HttpServletRequest request) {

        log.warn("Option parsing error: {}", ex.getMessage());

        ErrorResponse errorResponse = isDevelopmentMode() ?
                ErrorResponse.withDebugInfo(
                        HttpStatus.BAD_REQUEST.value(),
                        "Option Parsing Error",
                        ex.getMessage(),
                        request.getRequestURI(),
                        getStackTrace(ex)
                ) :
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "Option Parsing Error",
                        ex.getMessage(),
                        request.getRequestURI()
                );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 데이터 유효성 예외 처리
     */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(
            InvalidDataException ex, HttpServletRequest request) {

        log.warn("Invalid data error: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Data",
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(
            Exception ex, HttpServletRequest request) {

        log.error("Unhandled exception occurred", ex);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = isDevelopmentMode() ?
                ErrorResponse.withDebugInfo(
                        status.value(),
                        status.getReasonPhrase(),
                        "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.",
                        request.getRequestURI(),
                        getStackTrace(ex)
                ) :
                ErrorResponse.of(
                        status.value(),
                        status.getReasonPhrase(),
                        "서버 내부 오류가 발생했습니다. 관리자에게 문의하세요.",
                        request.getRequestURI()
                );

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * 개발 모드인지 확인
     */
    private boolean isDevelopmentMode() {
        return "dev".equals(activeProfile) || "local".equals(activeProfile);
    }

    /**
     * 스택 트레이스를 문자열로 변환
     */
    private String getStackTrace(Exception ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getClass().getName()).append(": ").append(ex.getMessage()).append("\n");

        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");

            // 스택 트레이스를 일정 길이로 제한
            if (sb.length() > 2000) {
                sb.append("...(truncated)");
                break;
            }
        }

        return sb.toString();
    }
}
