package com.aitalentagent.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CandidateFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void candidateFlowWorksWithMockAgents() throws Exception {
        MvcResult sessionResult = mockMvc.perform(post("/api/v1/auth/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "authProvider": "anonymous",
                                  "deviceId": "test-device-001",
                                  "platform": "h5"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String token = readData(sessionResult).path("accessToken").asText();

        MvcResult journeyResult = mockMvc.perform(post("/api/v1/journeys")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();
        String journeyId = readData(journeyResult).path("id").asText();

        mockMvc.perform(multipart("/api/v1/journeys/" + journeyId + "/resume-files")
                        .file(new MockMultipartFile(
                                "file",
                                "resume.pdf",
                                "application/pdf",
                                "mock resume".getBytes()
                        ))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());

        MvcResult parseResult = mockMvc.perform(post("/api/v1/journeys/" + journeyId + "/parse-resume")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted())
                .andReturn();
        String taskId = readData(parseResult).path("taskId").asText();

        JsonNode task = waitForTaskSuccess(token, taskId);
        assertThat(task.path("status").asText()).isEqualTo("succeeded");

        mockMvc.perform(get("/api/v1/journeys/" + journeyId + "/structured-resume")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/journeys/" + journeyId + "/structured-resume/confirm")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/journeys/" + journeyId + "/interview/turns")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"我负责架构设计和核心模块开发。"}
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/journeys/" + journeyId + "/interview/complete")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        MvcResult profileTaskResult = mockMvc.perform(post("/api/v1/journeys/" + journeyId + "/profile/generate")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isAccepted())
                .andReturn();
        String profileTaskId = readData(profileTaskResult).path("taskId").asText();
        waitForTaskSuccess(token, profileTaskId);

        mockMvc.perform(get("/api/v1/journeys/" + journeyId + "/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        MvcResult resumeTaskResult = mockMvc.perform(post("/api/v1/journeys/" + journeyId + "/resume-versions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"versionKey":"ats"}
                                """))
                .andExpect(status().isAccepted())
                .andReturn();
        String resumeTaskId = readData(resumeTaskResult).path("taskId").asText();
        waitForTaskSuccess(token, resumeTaskId);

        mockMvc.perform(get("/api/v1/journeys/" + journeyId + "/resume-versions/ats")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private JsonNode waitForTaskSuccess(String token, String taskId) throws Exception {
        for (int i = 0; i < 20; i++) {
            MvcResult result = mockMvc.perform(get("/api/v1/tasks/" + taskId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andReturn();
            JsonNode task = readData(result);
            String status = task.path("status").asText();
            if ("succeeded".equals(status) || "failed".equals(status)) {
                return task;
            }
            Thread.sleep(500);
        }
        throw new IllegalStateException("task timeout: " + taskId);
    }

    private JsonNode readData(MvcResult result) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString()).path("data");
    }
}
