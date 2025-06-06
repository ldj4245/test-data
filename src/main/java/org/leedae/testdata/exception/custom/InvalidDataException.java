package org.leedae.testdata.exception.custom;

import org.leedae.testdata.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * 데이터가 유효하지 않을 때 발생하는 예외
 */
public class InvalidDataException extends BusinessException {

    public InvalidDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST);
    }
}
