package com.aitalentagent.api.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ResumeVersionEntity {

    private String id;
    private String journeyId;
    private String versionKey;
    private String title;
    private String content;
    private String contentFormat = "plain";
    private String confidence = "medium";
    private List<String> warnings = new ArrayList<>();
    private List<String> usedEvidenceIds = new ArrayList<>();
    private Instant generatedAt = Instant.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getVersionKey() {
        return versionKey;
    }

    public void setVersionKey(String versionKey) {
        this.versionKey = versionKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentFormat() {
        return contentFormat;
    }

    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public List<String> getUsedEvidenceIds() {
        return usedEvidenceIds;
    }

    public void setUsedEvidenceIds(List<String> usedEvidenceIds) {
        this.usedEvidenceIds = usedEvidenceIds;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
