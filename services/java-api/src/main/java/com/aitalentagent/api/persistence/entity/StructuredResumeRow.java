package com.aitalentagent.api.persistence.entity;

import com.aitalentagent.api.domain.CandidateInfo;
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
@Table(name = "structured_resumes")
public class StructuredResumeRow {

    @Id
    private String id;
    @Column(name = "journey_id")
    private String journeyId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "basic_info")
    private CandidateInfo basicInfo = new CandidateInfo();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> education = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "work_experience")
    private List<String> workExperience = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> projects = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> skills = new ArrayList<>();
    @Column(name = "parse_quality_score")
    private double parseQualityScore;
    private String confidence;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> warnings = new ArrayList<>();
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "missing_fields")
    private List<String> missingFields = new ArrayList<>();
    @Column(name = "updated_at")
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getJourneyId() { return journeyId; }
    public void setJourneyId(String journeyId) { this.journeyId = journeyId; }
    public CandidateInfo getBasicInfo() { return basicInfo; }
    public void setBasicInfo(CandidateInfo basicInfo) { this.basicInfo = basicInfo; }
    public List<String> getEducation() { return education; }
    public void setEducation(List<String> education) { this.education = education; }
    public List<String> getWorkExperience() { return workExperience; }
    public void setWorkExperience(List<String> workExperience) { this.workExperience = workExperience; }
    public List<String> getProjects() { return projects; }
    public void setProjects(List<String> projects) { this.projects = projects; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    public double getParseQualityScore() { return parseQualityScore; }
    public void setParseQualityScore(double parseQualityScore) { this.parseQualityScore = parseQualityScore; }
    public String getConfidence() { return confidence; }
    public void setConfidence(String confidence) { this.confidence = confidence; }
    public List<String> getWarnings() { return warnings; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    public List<String> getMissingFields() { return missingFields; }
    public void setMissingFields(List<String> missingFields) { this.missingFields = missingFields; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
