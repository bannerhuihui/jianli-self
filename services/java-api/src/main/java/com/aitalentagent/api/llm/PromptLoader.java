package com.aitalentagent.api.llm;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PromptLoader {

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String load(String promptName) {
        return cache.computeIfAbsent(promptName, this::readPrompt);
    }

    private String readPrompt(String promptName) {
        try {
            ClassPathResource resource = new ClassPathResource("prompts/" + promptName);
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("Prompt not found: " + promptName, ex);
        }
    }
}
