package com.arka.microservice.carrito.domain.exception;


import com.arka.microservice.carrito.domain.exception.error.CommonErrorCode;
import com.arka.microservice.carrito.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * Exception for validation errors in input data
 */
@AllArgsConstructor
@Builder
public class ValidationException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.VALIDATION_ERROR;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.BAD_REQUEST.value();

    public ValidationException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ValidationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ValidationException(String code, String message) {
        super(code, message, STATUS_CODE);
    }
}