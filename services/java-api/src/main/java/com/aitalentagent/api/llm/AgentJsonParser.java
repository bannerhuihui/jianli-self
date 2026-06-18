package com.aitalentagent.api.llm;

import com.aitalentagent.api.common.ApiException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AgentJsonParser {

    private static final Pattern JSON_BLOCK = Pattern.compile("```(?:json)?\\s*([\\s\\S]*?)```", Pattern.CASE_INSENSITIVE);

    private final ObjectMapper objectMapper;

    public AgentJsonParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new ApiException("LLM_PARSE_FAILED", "大模型返回内容为空", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        String trimmed = raw.trim();
        try {
            return objectMapper.readTree(trimmed);
        } catch (Exception ignored) {
            // continue
        }

        Matcher matcher = JSON_BLOCK.matcher(trimmed);
        if (matcher.find()) {
            try {
                return objectMapper.readTree(matcher.group(1).trim());
            } catch (Exception ex) {
                throw new ApiException("LLM_PARSE_FAILED", "无法解析大模型 JSON 输出", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        int start = trimmed.indexOf('{');
        int end = trimmed.lastIndexOf('}');
        if (start >= 0 && end > start) {
            try {
                return objectMapper.readTree(trimmed.substring(start, end + 1));
            } catch (Exception ex) {
                throw new ApiException("LLM_PARSE_FAILED", "无法解析大模型 JSON 输出", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }

        throw new ApiException("LLM_PARSE_FAILED", "无法解析大模型 JSON 输出", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
