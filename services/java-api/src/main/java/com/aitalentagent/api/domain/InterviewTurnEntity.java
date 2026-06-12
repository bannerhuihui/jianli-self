package com.aitalentagent.api.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class InterviewTurnEntity {

    private String id;
    private String role;
    private String content;
    private String questionReason;
    private List<String> targetCapabilities = new ArrayList<>();
    private Instant createdAt = Instant.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestionReason() {
        return questionReason;
    }

    public void setQuestionReason(String questionReason) {
        this.questionReason = questionReason;
    }

    public List<String> getTargetCapabilities() {
        return targetCapabilities;
    }

    public void setTargetCapabilities(List<String> targetCapabilities) {
        this.targetCapabilities = targetCapabilities;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
