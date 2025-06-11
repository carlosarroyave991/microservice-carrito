package com.arka.microservice.carrito.domain.exception.error;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ErrorCodeRegistry {
    private static final Map<String, ErrorCode> errorCodeMap = new HashMap<>();

    static {
        for (CommonErrorCode errorCode : CommonErrorCode.values()) {
            register(errorCode);
        }

    }

    public static void register(ErrorCode errorCode) {
        if (errorCodeMap.containsKey(errorCode.getCode())) {
            throw new IllegalArgumentException("Error code " + errorCode.getCode() + " is already registered");
        }
        errorCodeMap.put(errorCode.getCode(), errorCode);
    }

    public static Optional<ErrorCode> lookup(String code) {
        return Optional.ofNullable(errorCodeMap.get(code));
    }

    public static ErrorCode getOrDefault(String code, ErrorCode defaultErrorCode) {
        return lookup(code).orElse(defaultErrorCode);
    }

    public static boolean isRegistered(String code) {
        return errorCodeMap.containsKey(code);
    }

    public static Map<String, ErrorCode> getAllErrorCodes() {
        return new HashMap<>(errorCodeMap);
    }
}
