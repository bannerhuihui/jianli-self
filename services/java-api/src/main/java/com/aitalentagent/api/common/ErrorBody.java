package com.aitalentagent.api.common;

import java.util.Map;

public record ErrorBody(String code, String message, Map<String, Object> details) {
}
