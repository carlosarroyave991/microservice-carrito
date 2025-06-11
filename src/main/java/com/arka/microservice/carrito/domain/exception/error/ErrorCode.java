package com.arka.microservice.carrito.domain.exception.error;

public interface ErrorCode {
    String getCode();

    String getMessage();

    /**
     * Gets the HTTP status code associated with this error.
     * @return The HTTP status code
     */
    int getStatusCode();

    /**
     * Gets the error category.
     * @return The error category
     */
    ErrorCategory getCategory();
}
