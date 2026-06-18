package com.aitalentagent.api.persistence;

import com.aitalentagent.api.domain.*;
import com.aitalentagent.api.persistence.entity.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
public class StoreMapper {

    public UserRow toUserRow(UserAccount user) {
        UserRow row = new UserRow();
        row.setId(user.getId());
        row.setRole(user.getRole());
        row.setAuthProvider(user.getAuthProvider());
        row.setDeviceId(user.getDeviceId());
        row.setEntitlements(copyList(user.getEntitlements()));
        row.setWechatMpOpenId(user.getWechatMpOpenId());
        row.setWechatOaOpenId(user.getWechatOaOpenId());
        row.setUnionId(user.getUnionId());
        row.setActiveJourneyId(user.getActiveJourneyId());
        row.setCreatedAt(user.getCreatedAt());
        return row;
    }

    public UserAccount toUser(UserRow row) {
        UserAccount user = new UserAccount();
        user.setId(row.getId());
        user.setRole(row.getRole());
        user.setAuthProvider(row.getAuthProvider());
        user.setDeviceId(row.getDeviceId());
        user.setEntitlements(copyList(row.getEntitlements()));
        user.setWechatMpOpenId(row.getWechatMpOpenId());
        user.setWechatOaOpenId(row.getWechatOaOpenId());
        user.setUnionId(row.getUnionId());
        user.setActiveJourneyId(row.getActiveJourneyId());
        user.setCreatedAt(row.getCreatedAt());
        return user;
    }

    public JourneyRow toJourneyRow(Journey journey) {
        JourneyRow row = new JourneyRow();
        row.setId(journey.getId());
        row.setUserId(journey.getUserId());
        row.setStatus(journey.getStatus().name());
        row.setCurrentStep(journey.getCurrentStep().name());
        row.setSteps(journeyStepsToJson(journey.getSteps()));
        row.setResumeFileId(journey.getResumeFileId());
        row.setStructuredResumeId(journey.getStructuredResumeId());
        row.setInterviewSessionId(journey.getInterviewSessionId());
        row.setTalentProfileId(journey.getTalentProfileId());
        row.setCreatedAt(journey.getCreatedAt());
        row.setUpdatedAt(journey.getUpdatedAt());
        return row;
    }

    public Journey toJourney(JourneyRow row) {
        Journey journey = new Journey();
        journey.setId(row.getId());
        journey.setUserId(row.getUserId());
        journey.setStatus(JourneyStatus.valueOf(row.getStatus()));
        journey.setCurrentStep(JourneyStep.valueOf(row.getCurrentStep()));
        journey.setSteps(journeyStepsFromJson(row.getSteps()));
        journey.setResumeFileId(row.getResumeFileId());
        journey.setStructuredResumeId(row.getStructuredResumeId());
        journey.setInterviewSessionId(row.getInterviewSessionId());
        journey.setTalentProfileId(row.getTalentProfileId());
        journey.setCreatedAt(row.getCreatedAt());
        journey.setUpdatedAt(row.getUpdatedAt());
        return journey;
    }

    public ResumeFileRow toResumeFileRow(ResumeFileEntity file) {
        ResumeFileRow row = new ResumeFileRow();
        row.setId(file.getId());
        row.setJourneyId(file.getJourneyId());
        row.setFileName(file.getFileName());
        row.setFileType(file.getFileType());
        row.setFileSize(file.getFileSize());
        row.setStoragePath(file.getStoragePath());
        row.setUploadedAt(file.getUploadedAt());
        return row;
    }

    public ResumeFileEntity toResumeFile(ResumeFileRow row) {
        ResumeFileEntity file = new ResumeFileEntity();
        file.setId(row.getId());
        file.setJourneyId(row.getJourneyId());
        file.setFileName(row.getFileName());
        file.setFileType(row.getFileType());
        file.setFileSize(row.getFileSize());
        file.setStoragePath(row.getStoragePath());
        file.setUploadedAt(row.getUploadedAt());
        return file;
    }

    public StructuredResumeRow toStructuredResumeRow(StructuredResumeEntity resume) {
        StructuredResumeRow row = new StructuredResumeRow();
        row.setId(resume.getId());
        row.setJourneyId(resume.getJourneyId());
        row.setBasicInfo(resume.getBasicInfo());
        row.setEducation(copyList(resume.getEducation()));
        row.setWorkExperience(copyList(resume.getWorkExperience()));
        row.setProjects(copyList(resume.getProjects()));
        row.setSkills(copyList(resume.getSkills()));
        row.setParseQualityScore(resume.getParseQualityScore());
        row.setConfidence(resume.getConfidence());
        row.setWarnings(copyList(resume.getWarnings()));
        row.setMissingFields(copyList(resume.getMissingFields()));
        row.setUpdatedAt(resume.getUpdatedAt());
        return row;
    }

    public StructuredResumeEntity toStructuredResume(StructuredResumeRow row) {
        StructuredResumeEntity resume = new StructuredResumeEntity();
        resume.setId(row.getId());
        resume.setJourneyId(row.getJourneyId());
        resume.setBasicInfo(row.getBasicInfo());
        resume.setEducation(copyList(row.getEducation()));
        resume.setWorkExperience(copyList(row.getWorkExperience()));
        resume.setProjects(copyList(row.getProjects()));
        resume.setSkills(copyList(row.getSkills()));
        resume.setParseQualityScore(row.getParseQualityScore());
        resume.setConfidence(row.getConfidence());
        resume.setWarnings(copyList(row.getWarnings()));
        resume.setMissingFields(copyList(row.getMissingFields()));
        resume.setUpdatedAt(row.getUpdatedAt());
        return resume;
    }

    public InterviewSessionRow toInterviewSessionRow(InterviewSessionEntity session) {
        InterviewSessionRow row = new InterviewSessionRow();
        row.setId(session.getId());
        row.setJourneyId(session.getJourneyId());
        row.setStage(session.getStage());
        row.setStatus(session.getStatus());
        row.setMissingEvidence(copyList(session.getMissingEvidence()));
        row.setQuestionIndex(session.getQuestionIndex());
        row.setCanGenerateProfile(session.isCanGenerateProfile());
        return row;
    }

    public InterviewSessionEntity toInterviewSession(InterviewSessionRow row, List<InterviewTurnRow> turns) {
        InterviewSessionEntity session = new InterviewSessionEntity();
        session.setId(row.getId());
        session.setJourneyId(row.getJourneyId());
        session.setStage(row.getStage());
        session.setStatus(row.getStatus());
        session.setMissingEvidence(copyList(row.getMissingEvidence()));
        session.setQuestionIndex(row.getQuestionIndex());
        session.setCanGenerateProfile(row.isCanGenerateProfile());
        session.setTurns(turns.stream().map(this::toInterviewTurn).toList());
        return session;
    }

    public InterviewTurnRow toInterviewTurnRow(String sessionId, InterviewTurnEntity turn) {
        InterviewTurnRow row = new InterviewTurnRow();
        row.setId(turn.getId());
        row.setSessionId(sessionId);
        row.setRole(turn.getRole());
        row.setContent(turn.getContent());
        row.setQuestionReason(turn.getQuestionReason());
        row.setTargetCapabilities(copyList(turn.getTargetCapabilities()));
        row.setCreatedAt(turn.getCreatedAt());
        return row;
    }

    public InterviewTurnEntity toInterviewTurn(InterviewTurnRow row) {
        InterviewTurnEntity turn = new InterviewTurnEntity();
        turn.setId(row.getId());
        turn.setRole(row.getRole());
        turn.setContent(row.getContent());
        turn.setQuestionReason(row.getQuestionReason());
        turn.setTargetCapabilities(copyList(row.getTargetCapabilities()));
        turn.setCreatedAt(row.getCreatedAt());
        return turn;
    }

    public TalentProfileRow toTalentProfileRow(TalentProfileEntity profile) {
        TalentProfileRow row = new TalentProfileRow();
        row.setId(profile.getId());
        row.setJourneyId(profile.getJourneyId());
        row.setCandidate(profile.getCandidate());
        row.setSummary(profile.getSummary());
        row.setOverallScore(profile.getOverallScore());
        row.setCapabilities(copyList(profile.getCapabilities()));
        row.setStrengths(copyList(profile.getStrengths()));
        row.setRisks(copyList(profile.getRisks()));
        row.setPreferences(copyList(profile.getPreferences()));
        row.setRecommendedRoles(copyList(profile.getRecommendedRoles()));
        row.setConfidence(profile.getConfidence());
        row.setEvidence(copyList(profile.getEvidence()));
        row.setGeneratedAt(profile.getGeneratedAt());
        return row;
    }

    public TalentProfileEntity toTalentProfile(TalentProfileRow row) {
        TalentProfileEntity profile = new TalentProfileEntity();
        profile.setId(row.getId());
        profile.setJourneyId(row.getJourneyId());
        profile.setCandidate(row.getCandidate());
        profile.setSummary(row.getSummary());
        profile.setOverallScore(row.getOverallScore());
        profile.setCapabilities(copyList(row.getCapabilities()));
        profile.setStrengths(copyList(row.getStrengths()));
        profile.setRisks(copyList(row.getRisks()));
        profile.setPreferences(copyList(row.getPreferences()));
        profile.setRecommendedRoles(copyList(row.getRecommendedRoles()));
        profile.setConfidence(row.getConfidence());
        profile.setEvidence(copyList(row.getEvidence()));
        profile.setGeneratedAt(row.getGeneratedAt());
        return profile;
    }

    public ResumeVersionRow toResumeVersionRow(ResumeVersionEntity version) {
        ResumeVersionRow row = new ResumeVersionRow();
        row.setId(version.getId());
        row.setJourneyId(version.getJourneyId());
        row.setVersionKey(version.getVersionKey());
        row.setTitle(version.getTitle());
        row.setContent(version.getContent());
        row.setContentFormat(version.getContentFormat());
        row.setConfidence(version.getConfidence());
        row.setWarnings(copyList(version.getWarnings()));
        row.setUsedEvidenceIds(copyList(version.getUsedEvidenceIds()));
        row.setGeneratedAt(version.getGeneratedAt());
        return row;
    }

    public ResumeVersionEntity toResumeVersion(ResumeVersionRow row) {
        ResumeVersionEntity version = new ResumeVersionEntity();
        version.setId(row.getId());
        version.setJourneyId(row.getJourneyId());
        version.setVersionKey(row.getVersionKey());
        version.setTitle(row.getTitle());
        version.setContent(row.getContent());
        version.setContentFormat(row.getContentFormat());
        version.setConfidence(row.getConfidence());
        version.setWarnings(copyList(row.getWarnings()));
        version.setUsedEvidenceIds(copyList(row.getUsedEvidenceIds()));
        version.setGeneratedAt(row.getGeneratedAt());
        return version;
    }

    public AsyncTaskRow toAsyncTaskRow(AsyncTask task) {
        AsyncTaskRow row = new AsyncTaskRow();
        row.setId(task.getId());
        row.setJourneyId(task.getJourneyId());
        row.setType(task.getType().name());
        row.setStatus(task.getStatus().name());
        row.setProgress(task.getProgress());
        row.setResult(task.getResult());
        row.setError(task.getError());
        row.setMetadata(task.getMetadata() != null ? new HashMap<>(task.getMetadata()) : new HashMap<>());
        row.setCreatedAt(task.getCreatedAt());
        row.setUpdatedAt(task.getUpdatedAt());
        row.setCompletedAt(task.getCompletedAt());
        return row;
    }

    public AsyncTask toAsyncTask(AsyncTaskRow row) {
        AsyncTask task = new AsyncTask();
        task.setId(row.getId());
        task.setJourneyId(row.getJourneyId());
        task.setType(TaskType.valueOf(row.getType()));
        task.setStatus(TaskStatus.valueOf(row.getStatus()));
        task.setProgress(row.getProgress());
        task.setResult(row.getResult());
        task.setError(row.getError());
        task.setMetadata(row.getMetadata() != null ? new HashMap<>(row.getMetadata()) : new HashMap<>());
        task.setCreatedAt(row.getCreatedAt());
        task.setUpdatedAt(row.getUpdatedAt());
        task.setCompletedAt(row.getCompletedAt());
        return task;
    }

    private Map<String, Object> journeyStepsToJson(Map<JourneyStep, Journey.StepProgress> steps) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (JourneyStep step : JourneyStep.values()) {
            Journey.StepProgress progress = steps.getOrDefault(step, new Journey.StepProgress(false, null));
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("completed", progress.completed());
            value.put("completedAt", progress.completedAt() != null ? progress.completedAt().toString() : null);
            result.put(step.name(), value);
        }
        return result;
    }

    private Map<JourneyStep, Journey.StepProgress> journeyStepsFromJson(Map<String, Object> steps) {
        Map<JourneyStep, Journey.StepProgress> result = new EnumMap<>(JourneyStep.class);
        for (JourneyStep step : JourneyStep.values()) {
            Object raw = steps != null ? steps.get(step.name()) : null;
            if (raw instanceof Map<?, ?> map) {
                boolean completed = Boolean.TRUE.equals(map.get("completed"));
                Instant completedAt = null;
                Object at = map.get("completedAt");
                if (at instanceof String s && !s.isBlank()) {
                    completedAt = Instant.parse(s);
                }
                result.put(step, new Journey.StepProgress(completed, completedAt));
            } else {
                result.put(step, new Journey.StepProgress(false, null));
            }
        }
        return result;
    }

    private <T> List<T> copyList(List<T> source) {
        return source != null ? new ArrayList<>(source) : new ArrayList<>();
    }
}
