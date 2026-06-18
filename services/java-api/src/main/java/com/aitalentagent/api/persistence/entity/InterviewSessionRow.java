package com.aitalentagent.api.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_sessions")
public class InterviewSessionRow {

    @Id
    private String id;
    @Column(name = "journey_id")
    private String journeyId;
    private String stage;
    private String status;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "missing_evidence")
    private List<String> missingEvidence = new ArrayList<>();
    @Column(name = "question_index")
    private int questionIndex;
    @Column(name = "can_generate_profile")
    private boolean canGenerateProfile;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String journeyId) { this.journeyId = journeyId; }
    public String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<String> getMissingEvidence() { return missingEvidence; }
    public void setMissingEvidence(List<String> missingEvidence) { this.missingEvidence = missingEvidence; }
    public int getQuestionIndex() { return questionIndex; }
    public void setQuestionIndex(int questionIndex) { this.questionIndex = questionIndex; }
    public boolean isCanGenerateProfile() { return canGenerateProfile; }
    public void setCanGenerateProfile(boolean canGenerateProfile) { this.canGenerateProfile = canGenerateProfile; }
}
