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
@Table(name = "resume_versions")
public class ResumeVersionRow {

    @Id
    private String id;
    @Column(name = "journey_id")
    private String journeyId;
    @Column(name = "version_key")
    private String versionKey;
    private String title;
    private String content;
    @Column(name = "content_format")
    private String contentFormat;
    private String confidence;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> warnings = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "used_evidence_ids")
    private List<String> usedEvidenceIds = new ArrayList<>();
    @Column(name = "generated_at")
    private Instant generatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String journeyId) { this.journeyId = journeyId; }
    public String getVersionKey() { return versionKey; }
    public void setVersionKey(String versionKey) { this.versionKey = versionKey; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getContentFormat() { return contentFormat; }
    public void setContentFormat(String contentFormat) { this.contentFormat = contentFormat; }
    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    public List<String> getUsedEvidenceIds() { return usedEvidenceIds; }
    public void setUsedEvidenceIds(List<String> usedEvidenceIds) { this.usedEvidenceIds = usedEvidenceIds; }
    public Instant getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }
}
