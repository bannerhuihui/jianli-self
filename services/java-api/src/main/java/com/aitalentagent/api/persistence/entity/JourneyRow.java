package com.aitalentagent.api.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "journeys")
public class JourneyRow {

    @Id
    private String id;
    @Column(name = "user_id")
    private String userId;
    private String status;
    @Column(name = "current_step")
    private String currentStep;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> steps = new HashMap<>();
    @Column(name = "resume_file_id")
    private String resumeFileId;
    @Column(name = "structured_resume_id")
    private String structuredResumeId;
    @Column(name = "interview_session_id")
    private String interviewSessionId;
    @Column(name = "talent_profile_id")
    private String talentProfileId;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCurrentStep() { return currentStep; }
    public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
    public Map<String, Object> getSteps() { return steps; }
    public void setSteps(Map<String, Object> steps) { this.steps = steps; }
    public String getResumeFileId() { return resumeFileId; }
    public void setResumeFileId(String resumeFileId) { this.resumeFileId = resumeFileId; }
    public String getStructuredResumeId() { return structuredResumeId; }
    public void setStructuredResumeId(String structuredResumeId) { this.structuredResumeId = structuredResumeId; }
    public String getInterviewSessionId() { return interviewSessionId; }
    public void setInterviewSessionId(String interviewSessionId) { this.interviewSessionId = interviewSessionId; }
    public String getTalentProfileId() { return talentProfileId; }
    public void setTalentProfileId(String talentProfileId) { this.talentProfileId = talentProfileId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
