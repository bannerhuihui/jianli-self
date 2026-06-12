package com.aitalentagent.api.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class StructuredResumeEntity {

    private String id;
    private String journeyId;
    private CandidateInfo basicInfo = new CandidateInfo();
    private List<String> education = new ArrayList<>();
    private List<String> workExperience = new ArrayList<>();
    private List<String> projects = new ArrayList<>();
    private List<String> skills = new ArrayList<>();
    private double parseQualityScore;
    private String confidence = "medium";
    private List<String> warnings = new ArrayList<>();
    private List<String> missingFields = new ArrayList<>();
    private Instant updatedAt = Instant.now();

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

    public CandidateInfo getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(CandidateInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(List<String> workExperience) {
        this.workExperience = workExperience;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public double getParseQualityScore() {
        return parseQualityScore;
    }

    public void setParseQualityScore(double parseQualityScore) {
        this.parseQualityScore = parseQualityScore;
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

    public List<String> getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(List<String> missingFields) {
        this.missingFields = missingFields;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
