package com.aitalentagent.api.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TalentProfileEntity {

    private String id;
    private String journeyId;
    private CandidateInfo candidate = new CandidateInfo();
    private String summary;
    private int overallScore;
    private List<CapabilityScoreEntity> capabilities = new ArrayList<>();
    private List<String> strengths = new ArrayList<>();
    private List<String> risks = new ArrayList<>();
    private List<String> preferences = new ArrayList<>();
    private List<String> recommendedRoles = new ArrayList<>();
    private String confidence = "medium";
    private List<EvidenceEntity> evidence = new ArrayList<>();
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

    public CandidateInfo getCandidate() {
        return candidate;
    }

    public void setCandidate(CandidateInfo candidate) {
        this.candidate = candidate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public List<CapabilityScoreEntity> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityScoreEntity> capabilities) {
        this.capabilities = capabilities;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getRisks() {
        return risks;
    }

    public void setRisks(List<String> risks) {
        this.risks = risks;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public List<String> getRecommendedRoles() {
        return recommendedRoles;
    }

    public void setRecommendedRoles(List<String> recommendedRoles) {
        this.recommendedRoles = recommendedRoles;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public List<EvidenceEntity> getEvidence() {
        return evidence;
    }

    public void setEvidence(List<EvidenceEntity> evidence) {
        this.evidence = evidence;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }
}
