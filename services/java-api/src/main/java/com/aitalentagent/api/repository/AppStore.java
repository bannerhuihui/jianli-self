package com.aitalentagent.api.repository;

import com.aitalentagent.api.domain.*;

import java.util.List;
import java.util.Optional;

public interface AppStore {

    void saveUser(UserAccount user);

    Optional<UserAccount> findUserById(String id);

    Optional<UserAccount> findUserByDeviceId(String deviceId);

    void saveRefreshToken(String token, String userId);

    Optional<String> findUserIdByRefreshToken(String token);

    void saveJourney(Journey journey);

    Optional<Journey> findJourneyById(String id);

    Optional<Journey> findActiveJourneyByUserId(String userId);

    void saveResumeFile(ResumeFileEntity file);

    Optional<ResumeFileEntity> findResumeFileById(String id);

    Optional<ResumeFileEntity> findLatestResumeFileByJourneyId(String journeyId);

    void saveResumeRawText(String journeyId, String fileId, String extractedText, String extractMethod);

    void saveStructuredResume(StructuredResumeEntity resume);

    Optional<StructuredResumeEntity> findStructuredResumeById(String id);

    Optional<StructuredResumeEntity> findStructuredResumeByJourneyId(String journeyId);

    void saveInterviewSession(InterviewSessionEntity session);

    Optional<InterviewSessionEntity> findInterviewSessionById(String id);

    Optional<InterviewSessionEntity> findInterviewSessionByJourneyId(String journeyId);

    void saveTalentProfile(TalentProfileEntity profile);

    Optional<TalentProfileEntity> findTalentProfileByJourneyId(String journeyId);

    void saveResumeVersion(ResumeVersionEntity version);

    List<ResumeVersionEntity> findResumeVersionsByJourneyId(String journeyId);

    Optional<ResumeVersionEntity> findResumeVersionByJourneyIdAndKey(String journeyId, String versionKey);

    void saveTask(AsyncTask task);

    Optional<AsyncTask> findTaskById(String id);
}
