package com.arka.microservice.carrito.domain.exception;


import com.arka.microservice.carrito.domain.exception.error.CommonErrorCode;
import com.arka.microservice.carrito.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * Exception for unauthorized access
 */
@AllArgsConstructor
@Builder
public class UnauthorizedException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.UNAUTHORIZED;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.UNAUTHORIZED.value();

    public UnauthorizedException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UnauthorizedException(String code, String message) {
        super(code, message, STATUS_CODE);
    }
}