package com.arka.microservice.carrito.domain.exception;


import com.arka.microservice.carrito.domain.exception.error.CommonErrorCode;
import com.arka.microservice.carrito.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * Exception for security-related errors
 */
@AllArgsConstructor
@Builder
public class SecurityException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.FORBIDDEN;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.FORBIDDEN.value();

    public SecurityException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    public SecurityException(ErrorCode errorCode) {
        super(errorCode);
    }

    public SecurityException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public SecurityException(String code, String message) {
        super(code, message, STATUS_CODE);
    }
}