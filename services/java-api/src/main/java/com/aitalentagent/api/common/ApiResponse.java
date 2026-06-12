package com.aitalentagent.api.common;

import java.time.Instant;

public record ApiResponse<T>(T data, String requestId, Instant timestamp) {

    public static <T> ApiResponse<T> of(T data, String requestId) {
        return new ApiResponse<>(data, requestId, Instant.now());
    }
}
