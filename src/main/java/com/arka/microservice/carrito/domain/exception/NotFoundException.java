package com.arka.microservice.carrito.domain.exception;


import com.arka.microservice.carrito.domain.exception.error.CommonErrorCode;
import com.arka.microservice.carrito.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * Exception for when a resource is not found
 */
@AllArgsConstructor
@Builder
public class NotFoundException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.RESOURCE_NOT_FOUND;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.NOT_FOUND.value();

    public NotFoundException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotFoundException(String code, String message) {
        super(code, message, STATUS_CODE);
    }
}