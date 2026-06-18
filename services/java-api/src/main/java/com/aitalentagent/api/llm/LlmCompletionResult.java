package com.aitalentagent.api.llm;

public record LlmCompletionResult(String content, String model, long latencyMs) {
}
