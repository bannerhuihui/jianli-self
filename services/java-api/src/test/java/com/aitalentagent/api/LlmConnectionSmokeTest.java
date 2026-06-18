package com.aitalentagent.api;

import com.aitalentagent.api.llm.LlmClient;
import com.aitalentagent.api.llm.LlmCompletionOptions;
import com.aitalentagent.api.llm.LlmMessage;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 手动联调火山方舟：仅在存在 application-local.yml 且 mode=llm 时运行。
 * mvn test -Dtest=LlmConnectionSmokeTest
 */
@SpringBootTest(properties = {
        "spring.config.import=optional:file:./application-local.yml",
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class LlmConnectionSmokeTest {

    @Autowired
    private LlmClient llmClient;

    @Autowired
    private com.aitalentagent.api.config.AppProperties appProperties;

    @Test
    void volcengineEndpointsAreReachable() throws Exception {
        Assumptions.assumeTrue(Files.exists(Path.of("application-local.yml")), "缺少 application-local.yml");
        Assumptions.assumeTrue(appProperties.getAgents().getMode().equalsIgnoreCase("llm"), "agents.mode 不是 llm");

        String interviewEndpoint = appProperties.getLlm().getVolcengine().getEndpoints().getInterview();
        Assumptions.assumeTrue(interviewEndpoint != null && !interviewEndpoint.isBlank(), "未配置 interview endpoint");

        var result = llmClient.complete(
                interviewEndpoint,
                List.of(
                        new LlmMessage("system", "你是助手，只回复 OK"),
                        new LlmMessage("user", "回复 OK")
                ),
                new LlmCompletionOptions(0.1, false)
        );

        assertThat(result.content()).isNotBlank();
    }
}
