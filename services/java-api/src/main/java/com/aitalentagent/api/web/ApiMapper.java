package com.aitalentagent.api.web;

import com.aitalentagent.api.domain.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ApiMapper {

    private ApiMapper() {
    }

    public static Map<String, Object> journey(Journey journey) {
        Map<String, Object> steps = new LinkedHashMap<>();
        journey.getSteps().forEach((step, progress) -> {
            Map<String, Object> stepData = new LinkedHashMap<>();
            stepData.put("completed", progress.completed());
            stepData.put("completedAt", progress.completedAt());
            steps.put(stepKey(step), stepData);
        });

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", journey.getId());
        data.put("userId", journey.getUserId());
        data.put("status", journey.getStatus().name().toLowerCase());
        data.put("currentStep", stepKey(journey.getCurrentStep()));
        data.put("steps", steps);
        data.put("resumeFileId", journey.getResumeFileId());
        data.put("structuredResumeId", journey.getStructuredResumeId());
        data.put("interviewSessionId", journey.getInterviewSessionId());
        data.put("talentProfileId", journey.getTalentProfileId());
        data.put("createdAt", journey.getCreatedAt());
        data.put("updatedAt", journey.getUpdatedAt());
        return data;
    }

    public static Map<String, Object> resumeFile(ResumeFileEntity file) {
        return Map.of(
                "fileId", file.getId(),
                "fileName", file.getFileName(),
                "fileType", file.getFileType(),
                "fileSize", file.getFileSize(),
                "uploadedAt", file.getUploadedAt()
        );
    }

    public static Map<String, Object> structuredResume(StructuredResumeEntity resume) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", resume.getId());
        data.put("journeyId", resume.getJourneyId());
        data.put("basicInfo", candidate(resume.getBasicInfo()));
        data.put("education", resume.getEducation());
        data.put("workExperience", resume.getWorkExperience());
        data.put("projects", resume.getProjects());
        data.put("skills", resume.getSkills());
        data.put("parseQualityScore", resume.getParseQualityScore());
        data.put("confidence", resume.getConfidence());
        data.put("warnings", resume.getWarnings());
        data.put("missingFields", resume.getMissingFields());
        data.put("updatedAt", resume.getUpdatedAt());
        return data;
    }

    public static Map<String, Object> interviewSession(InterviewSessionEntity session) {
        double progress = session.getTurns().isEmpty() ? 0 : Math.min(1.0, session.getQuestionIndex() / 3.0);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", session.getId());
        data.put("journeyId", session.getJourneyId());
        data.put("stage", session.getStage());
        data.put("status", session.getStatus());
        data.put("turns", session.getTurns().stream().map(ApiMapper::turn).collect(Collectors.toList()));
        data.put("missingEvidence", session.getMissingEvidence());
        data.put("canGenerateProfile", session.isCanGenerateProfile());
        data.put("progress", progress);
        return data;
    }

    public static Map<String, Object> turn(InterviewTurnEntity turn) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", turn.getId());
        data.put("role", turn.getRole());
        data.put("content", turn.getContent());
        if (turn.getQuestionReason() != null) {
            data.put("questionReason", turn.getQuestionReason());
        }
        if (!turn.getTargetCapabilities().isEmpty()) {
            data.put("targetCapabilities", turn.getTargetCapabilities());
        }
        data.put("createdAt", turn.getCreatedAt());
        return data;
    }

    public static Map<String, Object> talentProfile(TalentProfileEntity profile) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", profile.getId());
        data.put("journeyId", profile.getJourneyId());
        data.put("candidate", candidate(profile.getCandidate()));
        data.put("summary", profile.getSummary());
        data.put("overallScore", profile.getOverallScore());
        data.put("capabilities", profile.getCapabilities().stream().map(ApiMapper::capability).collect(Collectors.toList()));
        data.put("strengths", profile.getStrengths());
        data.put("risks", profile.getRisks());
        data.put("preferences", profile.getPreferences());
        data.put("recommendedRoles", profile.getRecommendedRoles());
        data.put("confidence", profile.getConfidence());
        data.put("evidence", profile.getEvidence().stream().map(ApiMapper::evidence).collect(Collectors.toList()));
        data.put("generatedAt", profile.getGeneratedAt());
        return data;
    }

    public static Map<String, Object> resumeVersion(ResumeVersionEntity version) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", version.getId());
        data.put("journeyId", version.getJourneyId());
        data.put("versionKey", version.getVersionKey());
        data.put("title", version.getTitle());
        data.put("content", version.getContent());
        data.put("contentFormat", version.getContentFormat());
        data.put("confidence", version.getConfidence());
        data.put("warnings", version.getWarnings());
        data.put("usedEvidenceIds", version.getUsedEvidenceIds());
        data.put("generatedAt", version.getGeneratedAt());
        return data;
    }

    public static Map<String, Object> task(AsyncTask task) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", task.getId());
        data.put("journeyId", task.getJourneyId());
        data.put("type", task.getType().name());
        data.put("status", task.getStatus().name().toLowerCase());
        data.put("progress", task.getProgress());
        data.put("result", task.getResult());
        data.put("error", task.getError());
        data.put("createdAt", task.getCreatedAt());
        data.put("updatedAt", task.getUpdatedAt());
        data.put("completedAt", task.getCompletedAt());
        return data;
    }

    public static Map<String, Object> taskAccepted(AsyncTask task) {
        return Map.of("taskId", task.getId(), "status", task.getStatus().name().toLowerCase());
    }

    private static Map<String, Object> candidate(CandidateInfo candidate) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", candidate.getId());
        data.put("name", candidate.getName());
        data.put("title", candidate.getTitle());
        data.put("location", candidate.getLocation());
        data.put("experienceYears", candidate.getExperienceYears());
        data.put("education", candidate.getEducation());
        data.put("phone", candidate.getPhone());
        data.put("email", candidate.getEmail());
        return data;
    }

    private static Map<String, Object> capability(CapabilityScoreEntity capability) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("key", capability.getKey());
        data.put("name", capability.getName());
        data.put("score", capability.getScore());
        data.put("confidence", capability.getConfidence());
        data.put("reason", capability.getReason());
        data.put("evidenceIds", capability.getEvidenceIds());
        return data;
    }

    private static Map<String, Object> evidence(EvidenceEntity evidence) {
        return Map.of(
                "id", evidence.getId(),
                "source", evidence.getSource(),
                "snippet", evidence.getSnippet(),
                "capabilityKeys", evidence.getCapabilityKeys()
        );
    }

    private static String stepKey(JourneyStep step) {
        return switch (step) {
            case UPLOAD -> "upload";
            case REVIEW -> "review";
            case INTERVIEW -> "interview";
            case PROFILE -> "profile";
            case RESUME -> "resume";
        };
    }
}
