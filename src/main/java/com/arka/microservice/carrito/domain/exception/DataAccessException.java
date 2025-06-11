package com.arka.microservice.carrito.domain.exception;


import com.arka.microservice.carrito.domain.exception.error.CommonErrorCode;
import com.arka.microservice.carrito.domain.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;

/**
 * Exception for database or external service errors
 */
@AllArgsConstructor
@Builder
public class DataAccessException extends BusinessException {
    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.DATA_ACCESS_ERROR;
    private static final String DEFAULT_CODE = DEFAULT_ERROR_CODE.getCode();
    private static final int STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public DataAccessException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    public DataAccessException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DataAccessException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DataAccessException(String code, String message) {
        super(code, message, STATUS_CODE);
    }
}
