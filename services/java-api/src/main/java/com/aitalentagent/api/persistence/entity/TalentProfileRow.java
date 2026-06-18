package com.aitalentagent.api.persistence.entity;

import com.aitalentagent.api.domain.CandidateInfo;
import com.aitalentagent.api.domain.CapabilityScoreEntity;
import com.aitalentagent.api.domain.EvidenceEntity;
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
@Table(name = "talent_profiles")
public class TalentProfileRow {

    @Id
    private String id;
    @Column(name = "journey_id")
    private String journeyId;
    @JdbcTypeCode(SqlTypes.JSON)
    private CandidateInfo candidate = new CandidateInfo();
    private String summary;
    @Column(name = "overall_score")
    private int overallScore;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<CapabilityScoreEntity> capabilities = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> strengths = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> risks = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> preferences = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "recommended_roles")
    private List<String> recommendedRoles = new ArrayList<>();
    private String confidence;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<EvidenceEntity> evidence = new ArrayList<>();
    @Column(name = "generated_at")
    private Instant generatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String journeyId) { this.journeyId = journeyId; }
    public CandidateInfo getCandidate() { return candidate; }
    public void setCandidate(CandidateInfo candidate) { this.candidate = candidate; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public int getOverallScore() { return overallScore; }
    public void setOverallScore(int overallScore) { this.overallScore = overallScore; }
    public List<CapabilityScoreEntity> getCapabilities() { return capabilities; }
    public void setCapabilities(List<CapabilityScoreEntity> capabilities) { this.capabilities = capabilities; }
    public List<String> getStrengths() { return strengths; }
    public void setStrengths(List<String> strengths) { this.strengths = strengths; }
    public List<String> getRisks() { return risks; }
    public void setRisks(List<String> risks) { this.risks = risks; }
    public List<String> getPreferences() { return preferences; }
    public void setPreferences(List<String> preferences) { this.preferences = preferences; }
    public List<String> getRecommendedRoles() { return recommendedRoles; }
    public void setRecommendedRoles(List<String> recommendedRoles) { this.recommendedRoles = recommendedRoles; }
    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }
    public List<EvidenceEntity> getEvidence() { return evidence; }
    public void setEvidence(List<EvidenceEntity> evidence) { this.evidence = evidence; }
    public Instant getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(Instant generatedAt) { this.generatedAt = generatedAt; }
}
