package com.aitalentagent.api.common;

import java.time.Instant;

public record ErrorResponse(ErrorBody error, String requestId, Instant timestamp) {

    public static ErrorResponse of(ErrorBody error, String requestId) {
        return new ErrorResponse(error, requestId, Instant.now());
    }
}
