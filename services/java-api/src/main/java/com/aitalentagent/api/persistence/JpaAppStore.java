package com.aitalentagent.api.persistence;

import com.aitalentagent.api.common.Ids;
import com.aitalentagent.api.domain.*;
import com.aitalentagent.api.persistence.entity.JourneyRow;
import com.aitalentagent.api.persistence.entity.RefreshTokenRow;
import com.aitalentagent.api.persistence.entity.ResumeRawTextRow;
import com.aitalentagent.api.persistence.repo.*;
import com.aitalentagent.api.repository.AppStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnBean(DataSource.class)
public class JpaAppStore implements AppStore {

    private final UserJpaRepository userRepo;
    private final RefreshTokenJpaRepository refreshTokenRepo;
    private final JourneyJpaRepository journeyRepo;
    private final ResumeFileJpaRepository resumeFileRepo;
    private final ResumeRawTextJpaRepository resumeRawTextRepo;
    private final StructuredResumeJpaRepository structuredResumeRepo;
    private final InterviewSessionJpaRepository interviewSessionRepo;
    private final InterviewTurnJpaRepository interviewTurnRepo;
    private final TalentProfileJpaRepository talentProfileRepo;
    private final ResumeVersionJpaRepository resumeVersionRepo;
    private final AsyncTaskJpaRepository asyncTaskRepo;
    private final StoreMapper mapper;

    public JpaAppStore(
            UserJpaRepository userRepo,
            RefreshTokenJpaRepository refreshTokenRepo,
            JourneyJpaRepository journeyRepo,
            ResumeFileJpaRepository resumeFileRepo,
            ResumeRawTextJpaRepository resumeRawTextRepo,
            StructuredResumeJpaRepository structuredResumeRepo,
            InterviewSessionJpaRepository interviewSessionRepo,
            InterviewTurnJpaRepository interviewTurnRepo,
            TalentProfileJpaRepository talentProfileRepo,
            ResumeVersionJpaRepository resumeVersionRepo,
            AsyncTaskJpaRepository asyncTaskRepo,
            StoreMapper mapper
    ) {
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.journeyRepo = journeyRepo;
        this.resumeFileRepo = resumeFileRepo;
        this.resumeRawTextRepo = resumeRawTextRepo;
        this.structuredResumeRepo = structuredResumeRepo;
        this.interviewSessionRepo = interviewSessionRepo;
        this.interviewTurnRepo = interviewTurnRepo;
        this.talentProfileRepo = talentProfileRepo;
        this.resumeVersionRepo = resumeVersionRepo;
        this.asyncTaskRepo = asyncTaskRepo;
        this.mapper = mapper;
    }

    @Override
    public void saveUser(UserAccount user) {
        userRepo.save(mapper.toUserRow(user));
    }

    @Override
    public Optional<UserAccount> findUserById(String id) {
        return userRepo.findById(id).map(mapper::toUser);
    }

    @Override
    public Optional<UserAccount> findUserByDeviceId(String deviceId) {
        return userRepo.findByDeviceId(deviceId).map(mapper::toUser);
    }

    @Override
    public void saveRefreshToken(String token, String userId) {
        RefreshTokenRow row = new RefreshTokenRow();
        row.setToken(token);
        row.setUserId(userId);
        row.setCreatedAt(Instant.now());
        refreshTokenRepo.save(row);
    }

    @Override
    public Optional<String> findUserIdByRefreshToken(String token) {
        return refreshTokenRepo.findById(token).map(RefreshTokenRow::getUserId);
    }

    @Override
    public void saveJourney(Journey journey) {
        journeyRepo.save(mapper.toJourneyRow(journey));
    }

    @Override
    public Optional<Journey> findJourneyById(String id) {
        return journeyRepo.findById(id).map(mapper::toJourney);
    }

    @Override
    public Optional<Journey> findActiveJourneyByUserId(String userId) {
        return journeyRepo.findByUserIdOrderByUpdatedAtDesc(userId).stream()
                .filter(row -> !JourneyStatus.COMPLETED.name().equals(row.getStatus()))
                .max(Comparator.comparing(JourneyRow::getUpdatedAt))
                .map(mapper::toJourney);
    }

    @Override
    public void saveResumeFile(ResumeFileEntity file) {
        resumeFileRepo.save(mapper.toResumeFileRow(file));
    }

    @Override
    public Optional<ResumeFileEntity> findResumeFileById(String id) {
        return resumeFileRepo.findById(id).map(mapper::toResumeFile);
    }

    @Override
    public Optional<ResumeFileEntity> findLatestResumeFileByJourneyId(String journeyId) {
        return resumeFileRepo.findFirstByJourneyIdOrderByUploadedAtDesc(journeyId).map(mapper::toResumeFile);
    }

    @Override
    public void saveResumeRawText(String journeyId, String fileId, String extractedText, String extractMethod) {
        ResumeRawTextRow row = resumeRawTextRepo.findByJourneyId(journeyId).orElseGet(ResumeRawTextRow::new);
        if (row.getId() == null) {
            row.setId(Ids.next("raw"));
        }
        row.setJourneyId(journeyId);
        row.setResumeFileId(fileId);
        row.setExtractedText(extractedText);
        row.setExtractMethod(extractMethod);
        row.setExtractedAt(Instant.now());
        resumeRawTextRepo.save(row);
    }

    @Override
    public void saveStructuredResume(StructuredResumeEntity resume) {
        structuredResumeRepo.save(mapper.toStructuredResumeRow(resume));
    }

    @Override
    public Optional<StructuredResumeEntity> findStructuredResumeById(String id) {
        return structuredResumeRepo.findById(id).map(mapper::toStructuredResume);
    }

    @Override
    public Optional<StructuredResumeEntity> findStructuredResumeByJourneyId(String journeyId) {
        return structuredResumeRepo.findByJourneyId(journeyId).map(mapper::toStructuredResume);
    }

    @Override
    @Transactional
    public void saveInterviewSession(InterviewSessionEntity session) {
        interviewSessionRepo.save(mapper.toInterviewSessionRow(session));
        interviewTurnRepo.deleteBySessionId(session.getId());
        for (InterviewTurnEntity turn : session.getTurns()) {
            interviewTurnRepo.save(mapper.toInterviewTurnRow(session.getId(), turn));
        }
    }

    @Override
    public Optional<InterviewSessionEntity> findInterviewSessionById(String id) {
        return interviewSessionRepo.findById(id)
                .map(row -> mapper.toInterviewSession(row, interviewTurnRepo.findBySessionIdOrderByCreatedAtAsc(id)));
    }

    @Override
    public Optional<InterviewSessionEntity> findInterviewSessionByJourneyId(String journeyId) {
        return interviewSessionRepo.findByJourneyId(journeyId)
                .map(row -> mapper.toInterviewSession(
                        row,
                        interviewTurnRepo.findBySessionIdOrderByCreatedAtAsc(row.getId())
                ));
    }

    @Override
    public void saveTalentProfile(TalentProfileEntity profile) {
        talentProfileRepo.save(mapper.toTalentProfileRow(profile));
    }

    @Override
    public Optional<TalentProfileEntity> findTalentProfileByJourneyId(String journeyId) {
        return talentProfileRepo.findByJourneyId(journeyId).map(mapper::toTalentProfile);
    }

    @Override
    public void saveResumeVersion(ResumeVersionEntity version) {
        resumeVersionRepo.save(mapper.toResumeVersionRow(version));
    }

    @Override
    public List<ResumeVersionEntity> findResumeVersionsByJourneyId(String journeyId) {
        return resumeVersionRepo.findByJourneyIdOrderByGeneratedAtDesc(journeyId).stream()
                .map(mapper::toResumeVersion)
                .toList();
    }

    @Override
    public Optional<ResumeVersionEntity> findResumeVersionByJourneyIdAndKey(String journeyId, String versionKey) {
        return resumeVersionRepo.findFirstByJourneyIdAndVersionKeyOrderByGeneratedAtDesc(journeyId, versionKey)
                .map(mapper::toResumeVersion);
    }

    @Override
    public void saveTask(AsyncTask task) {
        asyncTaskRepo.save(mapper.toAsyncTaskRow(task));
    }

    @Override
    public Optional<AsyncTask> findTaskById(String id) {
        return asyncTaskRepo.findById(id).map(mapper::toAsyncTask);
    }
}
