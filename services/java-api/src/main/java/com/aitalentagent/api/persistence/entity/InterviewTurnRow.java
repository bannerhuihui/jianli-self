package com.aitalentagent.api.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_turns")
public class InterviewTurnRow {

    @Id
    private String id;
    @Column(name = "session_id")
    private String sessionId;
    private String role;
    private String content;
    @Column(name = "question_reason")
    private String questionReason;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_capabilities")
    private List<String> targetCapabilities = new ArrayList<>();
    @Column(name = "created_at")
    private Instant createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getQuestionReason() { return questionReason; }
    public void setQuestionReason(String questionReason) { this.questionReason = questionReason; }
    public List<String> getTargetCapabilities() { return targetCapabilities; }
    public void setTargetCapabilities(List<String> targetCapabilities) { this.targetCapabilities = targetCapabilities; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
