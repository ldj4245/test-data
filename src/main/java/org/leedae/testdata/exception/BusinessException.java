package org.leedae.testdata.exception;

import org.springframework.http.HttpStatus;

/**
 * 애플리케이션의 비즈니스 로직 관련 기본 예외 클래스
 */
public abstract class BusinessException extends RuntimeException {

    private final HttpStatus status;

    protected BusinessException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    protected BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    protected BusinessException(String message, Throwable cause) {
        this(message, cause, HttpStatus.BAD_REQUEST);
    }

    protected BusinessException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
