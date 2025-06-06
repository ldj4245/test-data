package org.leedae.testdata.exception.custom;

import org.leedae.testdata.exception.BusinessException;
import org.springframework.http.HttpStatus;

/**
 * 옵션 JSON 파싱 과정에서 발생하는 예외
 */
public class OptionParsingException extends BusinessException {

    public OptionParsingException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public OptionParsingException(String message, Throwable cause) {
        super(message, cause, HttpStatus.BAD_REQUEST);
    }

    public OptionParsingException(String dataType, String optionJson, Throwable cause) {
        super(String.format("Failed to parse options for %s type. Invalid JSON: %s",
                dataType, optionJson), cause, HttpStatus.BAD_REQUEST);
    }
}
