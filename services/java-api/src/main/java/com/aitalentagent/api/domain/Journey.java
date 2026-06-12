package com.aitalentagent.api.domain;

import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;

public class Journey {

    private String id;
    private String userId;
    private JourneyStatus status = JourneyStatus.CREATED;
    private JourneyStep currentStep = JourneyStep.UPLOAD;
    private Map<JourneyStep, StepProgress> steps = new EnumMap<>(JourneyStep.class);
    private String resumeFileId;
    private String structuredResumeId;
    private String interviewSessionId;
    private String talentProfileId;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Journey() {
        for (JourneyStep step : JourneyStep.values()) {
            steps.put(step, new StepProgress(false, null));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public JourneyStatus getStatus() {
        return status;
    }

    public void setStatus(JourneyStatus status) {
        this.status = status;
    }

    public JourneyStep getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(JourneyStep currentStep) {
        this.currentStep = currentStep;
    }

    public Map<JourneyStep, StepProgress> getSteps() {
        return steps;
    }

    public void setSteps(Map<JourneyStep, StepProgress> steps) {
        this.steps = steps;
    }

    public String getResumeFileId() {
        return resumeFileId;
    }

    public void setResumeFileId(String resumeFileId) {
        this.resumeFileId = resumeFileId;
    }

    public String getStructuredResumeId() {
        return structuredResumeId;
    }

    public void setStructuredResumeId(String structuredResumeId) {
        this.structuredResumeId = structuredResumeId;
    }

    public String getInterviewSessionId() {
        return interviewSessionId;
    }

    public void setInterviewSessionId(String interviewSessionId) {
        this.interviewSessionId = interviewSessionId;
    }

    public String getTalentProfileId() {
        return talentProfileId;
    }

    public void setTalentProfileId(String talentProfileId) {
        this.talentProfileId = talentProfileId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public record StepProgress(boolean completed, Instant completedAt) {
    }
}
