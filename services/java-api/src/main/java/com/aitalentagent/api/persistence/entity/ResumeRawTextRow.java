package com.aitalentagent.api.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "resume_raw_text")
public class ResumeRawTextRow {

    @Id
    private String id;
    @Column(name = "journey_id")
    private String journeyId;
    @Column(name = "resume_file_id")
    private String resumeFileId;
    @Column(name = "extracted_text")
    private String extractedText;
    @Column(name = "extract_method")
    private String extractMethod;
    @Column(name = "extracted_at")
    private Instant extractedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String journeyId) { this.journeyId = journeyId; }
    public String getResumeFileId() { return resumeFileId; }
    public void setResumeFileId(String resumeFileId) { this.resumeFileId = resumeFileId; }
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
    public String getExtractMethod() { return extractMethod; }
    public void setExtractMethod(String extractMethod) { this.extractMethod = extractMethod; }
    public Instant getExtractedAt() { return extractedAt; }
    public void setExtractedAt(Instant extractedAt) { this.extractedAt = extractedAt; }
}
