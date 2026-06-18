package com.aitalentagent.api.llm;

import com.aitalentagent.api.common.ApiException;
import com.aitalentagent.api.config.AppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Component
public class VolcengineLlmClient implements LlmClient {

    private static final Logger log = LoggerFactory.getLogger(VolcengineLlmClient.class);

    private final AppProperties appProperties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public VolcengineLlmClient(AppProperties appProperties, ObjectMapper objectMapper) {
        this.appProperties = appProperties;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    @Override
    public LlmCompletionResult complete(String endpointId, List<LlmMessage> messages, LlmCompletionOptions options) {
        String apiKey = resolveApiKey();
        if (apiKey.isBlank()) {
            throw new ApiException("LLM_CONFIG_MISSING", "未配置 VOLCENGINE_API_KEY", HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (endpointId == null || endpointId.isBlank()) {
            throw new ApiException("LLM_CONFIG_MISSING", "未配置推理接入点 Endpoint ID", HttpStatus.SERVICE_UNAVAILABLE);
        }

        long started = System.currentTimeMillis();
        try {
            ObjectNode body = objectMapper.createObjectNode();
            body.put("model", endpointId);
            ArrayNode messageArray = body.putArray("messages");
            for (LlmMessage message : messages) {
                ObjectNode item = messageArray.addObject();
                item.put("role", message.role());
                item.put("content", message.content());
            }
            body.put("temperature", options.temperature());
            if (options.jsonMode()) {
                ObjectNode responseFormat = body.putObject("response_format");
                responseFormat.put("type", "json_object");
            }

            String baseUrl = appProperties.getLlm().getVolcengine().getBaseUrl();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/chat/completions"))
                    .timeout(Duration.ofSeconds(120))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.warn("Volcengine API error status={} body={}", response.statusCode(), response.body());
                throw new ApiException("LLM_REQUEST_FAILED", "大模型调用失败: HTTP " + response.statusCode(), HttpStatus.SERVICE_UNAVAILABLE);
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode choices = root.path("choices");
            if (!choices.isArray() || choices.isEmpty()) {
                throw new ApiException("LLM_REQUEST_FAILED", "大模型返回为空", HttpStatus.SERVICE_UNAVAILABLE);
            }
            String content = choices.get(0).path("message").path("content").asText("");
            String model = root.path("model").asText(endpointId);
            long latencyMs = System.currentTimeMillis() - started;
            log.info("LLM completed model={} latencyMs={} contentLength={}", model, latencyMs, content.length());
            return new LlmCompletionResult(content, model, latencyMs);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Volcengine LLM call failed", ex);
            throw new ApiException("LLM_REQUEST_FAILED", "大模型调用异常: " + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private String resolveApiKey() {
        String configured = appProperties.getLlm().getVolcengine().getApiKey();
        if (configured != null && !configured.isBlank()) {
            return configured;
        }
        String env = System.getenv("VOLCENGINE_API_KEY");
        return env == null ? "" : env;
    }
}
