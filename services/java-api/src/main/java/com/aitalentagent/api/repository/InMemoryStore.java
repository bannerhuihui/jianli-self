package com.aitalentagent.api.repository;

import com.aitalentagent.api.domain.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnMissingBean(DataSource.class)
public class InMemoryStore implements AppStore {

    private final Map<String, UserAccount> users = new ConcurrentHashMap<>();
    private final Map<String, String> refreshTokens = new ConcurrentHashMap<>();
    private final Map<String, Journey> journeys = new ConcurrentHashMap<>();
    private final Map<String, ResumeFileEntity> resumeFiles = new ConcurrentHashMap<>();
    private final Map<String, StructuredResumeEntity> structuredResumes = new ConcurrentHashMap<>();
    private final Map<String, InterviewSessionEntity> interviewSessions = new ConcurrentHashMap<>();
    private final Map<String, TalentProfileEntity> talentProfiles = new ConcurrentHashMap<>();
    private final Map<String, ResumeVersionEntity> resumeVersions = new ConcurrentHashMap<>();
    private final Map<String, AsyncTask> tasks = new ConcurrentHashMap<>();

    @Override
    public void saveUser(UserAccount user) {
        users.put(user.getId(), user);
    }

    @Override
    public Optional<UserAccount> findUserById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<UserAccount> findUserByDeviceId(String deviceId) {
        return users.values().stream()
                .filter(user -> deviceId.equals(user.getDeviceId()))
                .findFirst();
    }

    @Override
    public void saveRefreshToken(String token, String userId) {
        refreshTokens.put(token, userId);
    }

    @Override
    public Optional<String> findUserIdByRefreshToken(String token) {
        return Optional.ofNullable(refreshTokens.get(token));
    }

    @Override
    public void saveJourney(Journey journey) {
        journeys.put(journey.getId(), journey);
    }

    @Override
    public Optional<Journey> findJourneyById(String id) {
        return Optional.ofNullable(journeys.get(id));
    }

    @Override
    public Optional<Journey> findActiveJourneyByUserId(String userId) {
        return journeys.values().stream()
                .filter(journey -> userId.equals(journey.getUserId()))
                .filter(journey -> journey.getStatus() != JourneyStatus.COMPLETED)
                .max(Comparator.comparing(Journey::getUpdatedAt));
    }

    @Override
    public void saveResumeFile(ResumeFileEntity file) {
        resumeFiles.put(file.getId(), file);
    }

    @Override
    public Optional<ResumeFileEntity> findResumeFileById(String id) {
        return Optional.ofNullable(resumeFiles.get(id));
    }

    @Override
    public Optional<ResumeFileEntity> findLatestResumeFileByJourneyId(String journeyId) {
        return resumeFiles.values().stream()
                .filter(file -> journeyId.equals(file.getJourneyId()))
                .max(Comparator.comparing(ResumeFileEntity::getUploadedAt));
    }

    @Override
    public void saveResumeRawText(String journeyId, String fileId, String extractedText, String extractMethod) {
        // 内存模式不持久化原始文本
    }

    @Override
    public void saveStructuredResume(StructuredResumeEntity resume) {
        structuredResumes.put(resume.getId(), resume);
    }

    @Override
    public Optional<StructuredResumeEntity> findStructuredResumeById(String id) {
        return Optional.ofNullable(structuredResumes.get(id));
    }

    @Override
    public Optional<StructuredResumeEntity> findStructuredResumeByJourneyId(String journeyId) {
        return structuredResumes.values().stream()
                .filter(resume -> journeyId.equals(resume.getJourneyId()))
                .findFirst();
    }

    @Override
    public void saveInterviewSession(InterviewSessionEntity session) {
        interviewSessions.put(session.getId(), session);
    }

    @Override
    public Optional<InterviewSessionEntity> findInterviewSessionById(String id) {
        return Optional.ofNullable(interviewSessions.get(id));
    }

    @Override
    public Optional<InterviewSessionEntity> findInterviewSessionByJourneyId(String journeyId) {
        return interviewSessions.values().stream()
                .filter(session -> journeyId.equals(session.getJourneyId()))
                .findFirst();
    }

    @Override
    public void saveTalentProfile(TalentProfileEntity profile) {
        talentProfiles.put(profile.getId(), profile);
    }

    @Override
    public Optional<TalentProfileEntity> findTalentProfileByJourneyId(String journeyId) {
        return talentProfiles.values().stream()
                .filter(profile -> journeyId.equals(profile.getJourneyId()))
                .findFirst();
    }

    @Override
    public void saveResumeVersion(ResumeVersionEntity version) {
        resumeVersions.put(version.getId(), version);
    }

    @Override
    public List<ResumeVersionEntity> findResumeVersionsByJourneyId(String journeyId) {
        return resumeVersions.values().stream()
                .filter(version -> journeyId.equals(version.getJourneyId()))
                .sorted(Comparator.comparing(ResumeVersionEntity::getGeneratedAt).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ResumeVersionEntity> findResumeVersionByJourneyIdAndKey(String journeyId, String versionKey) {
        return resumeVersions.values().stream()
                .filter(version -> journeyId.equals(version.getJourneyId()) && versionKey.equals(version.getVersionKey()))
                .max(Comparator.comparing(ResumeVersionEntity::getGeneratedAt));
    }

    @Override
    public void saveTask(AsyncTask task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public Optional<AsyncTask> findTaskById(String id) {
        return Optional.ofNullable(tasks.get(id));
    }
}
