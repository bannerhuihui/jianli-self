package com.aitalentagent.api.llm;

import java.util.List;

public interface LlmClient {

    LlmCompletionResult complete(String endpointId, List<LlmMessage> messages, LlmCompletionOptions options);
}
