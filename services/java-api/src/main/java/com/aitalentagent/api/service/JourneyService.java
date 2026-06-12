package com.aitalentagent.api.service;

import com.aitalentagent.api.agent.MockDataFactory;
import com.aitalentagent.api.common.ApiException;
import com.aitalentagent.api.common.Ids;
import com.aitalentagent.api.domain.*;
import com.aitalentagent.api.repository.InMemoryStore;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

@Service
public class JourneyService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "doc", "docx");
    private static final Set<String> ALLOWED_VERSION_KEYS = Set.of("ats", "hr", "platform", "email");
    private static final long MAX_FILE_SIZE = 20L * 1024 * 1024;

    private final InMemoryStore store;
    private final TaskService taskService;
    private final Path uploadDir;

    public JourneyService(InMemoryStore store, TaskService taskService) {
        this.store = store;
        this.taskService = taskService;
        this.uploadDir = Path.of(System.getProperty("java.io.tmpdir"), "ai-talent-agent-uploads");
    }

    public Journey createJourney(String userId) {
        Optional<Journey> active = store.findActiveJourneyByUserId(userId);
        if (active.isPresent()) {
            return active.get();
        }

        Journey journey = new Journey();
        journey.setId(Ids.next("journey"));
        journey.setUserId(userId);
        journey.setStatus(JourneyStatus.CREATED);
        journey.setCurrentStep(JourneyStep.UPLOAD);
        store.saveJourney(journey);

        UserAccount user = store.findUserById(userId)
                .orElseThrow(() -> new ApiException("AUTH_ACCESS_DENIED", "用户不存在", HttpStatus.FORBIDDEN));
        user.setActiveJourneyId(journey.getId());
        store.saveUser(user);
        return journey;
    }

    public Journey getActiveJourney(String userId) {
        return store.findActiveJourneyByUserId(userId)
                .orElseThrow(() -> new ApiException("JOURNEY_NOT_FOUND", "暂无进行中的旅程", HttpStatus.NOT_FOUND));
    }

    public Journey getJourney(String journeyId, String userId) {
        return requireOwnedJourney(journeyId, userId);
    }

    public ResumeFileEntity uploadResumeFile(String journeyId, String userId, MultipartFile file) {
        Journey journey = requireOwnedJourney(journeyId, userId);
        validateUploadFile(file);

        String extension = extension(file.getOriginalFilename());
        ResumeFileEntity entity = new ResumeFileEntity();
        entity.setId(Ids.next("file"));
        entity.setJourneyId(journeyId);
        entity.setFileName(file.getOriginalFilename());
        entity.setFileType(extension);
        entity.setFileSize(file.getSize());
        entity.setStoragePath(persistFile(entity.getId(), extension, file));
        store.saveResumeFile(entity);

        journey.setResumeFileId(entity.getId());
        journey.setStatus(JourneyStatus.RESUME_UPLOADED);
        journey.setUpdatedAt(Instant.now());
        store.saveJourney(journey);
        return entity;
    }

    public AsyncTask startParseResume(String journeyId, String userId, String fileId) {
        Journey journey = requireOwnedJourney(journeyId, userId);
        if (journey.getResumeFileId() == null && fileId == null) {
            throw new ApiException("JOURNEY_STATE_INVALID", "请先上传简历文件", HttpStatus.CONFLICT);
        }

        journey.setStatus(JourneyStatus.RESUME_PARSING);
        journey.setUpdatedAt(Instant.now());
        store.saveJourney(journey);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("fileId", fileId == null ? journey.getResumeFileId() : fileId);
        AsyncTask task = taskService.createTask(journeyId, TaskType.PARSE_RESUME, metadata);
        taskService.runParseResumeTask(task.getId());
        return task;
    }

    public StructuredResumeEntity createManualStructuredResume(String journeyId, String userId) {
        Journey journey = requireOwnedJourney(journeyId, userId);
        StructuredResumeEntity resume = MockDataFactory.createEmptyStructuredResume(journeyId);
        store.saveStructuredResume(resume);

        journey.setStructuredResumeId(resume.getId());
        journey.setStatus(JourneyStatus.RESUME_REVIEW);
        journey.setCurrentStep(JourneyStep.REVIEW);
        journey.setUpdatedAt(Instant.now());
        store.saveJourney(journey);
        return resume;
    }

    public StructuredResumeEntity getStructuredResume(String journeyId, String userId) {
        requireOwnedJourney(journeyId, userId);
        return store.findStructuredResumeByJourneyId(journeyId)
                .orElseThrow(() -> new ApiException("JOURNEY_STATE_INVALID", "结构化简历尚未生成", HttpStatus.CONFLICT));
    }

    public StructuredResumeEntity patchStructuredResume(String journeyId, String userId, StructuredResumePatch patch) {
        StructuredResumeEntity resume = getStructuredResume(journeyId, userId);
        if (patch.basicInfo() != null) {
            mergeBasicInfo(resume.getBasicInfo(), patch.basicInfo());
        }
        if (patch.education() != null) {
            resume.setEducation(patch.education());
        }
        if (patch.workExperience() != null) {
            resume.setWorkExperience(patch.workExperience());
        }
        if (patch.projects() != null) {
            resume.setProjects(patch.projects());
        }
        if (patch.skills() != null) {
            resume.setSkills(patch.skills());
        }
        resume.setUpdatedAt(Instant.now());
        store.saveStructuredResume(resume);
        return resume;
    }

    public InterviewSessionEntity confirmStructuredResume(String journeyId, String userId) {
        Journey journey = requireOwnedJourney(journeyId, userId);
        StructuredResumeEntity resume = getStructuredResume(journeyId, userId);
        validateResumeForInterview(resume);

        InterviewSessionEntity session = store.findInterviewSessionByJourneyId(journeyId).orElseGet(() -> {
            InterviewSessionEntity created = new InterviewSessionEntity();
            created.setId(Ids.next("iv"));
            created.setJourneyId(journeyId);
            created.setMissingEvidence(List.of("communicationAbility", "leadershipAbility"));
            return created;
        });

        if (session.getTurns().isEmpty()) {
            appendFirstQuestion(session);
        }
        store.saveInterviewSession(session);

        journey.setInterviewSessionId(session.getId());
        journey.setStatus(JourneyStatus.INTERVIEW_ACTIVE);
        journey.setCurrentStep(JourneyStep.INTERVIEW);
        journey.getSteps().put(JourneyStep.REVIEW, new Journey.StepProgress(true, Instant.now()));
        journey.setUpdatedAt(Instant.now());
        store.saveJourney(journey);
        return session;
    }

    public InterviewSessionEntity getInterview(String journeyId, String userId) {
        requireOwnedJourney(journeyId, userId);
        return store.findInterviewSessionByJourneyId(journeyId)
                .orElseThrow(() -> new ApiException("INTERVIEW_NOT_STARTED", "访谈尚未开始", HttpStatus.CONFLICT));
    }

    public InterviewTurnResponse submitInterviewTurn(String journeyId, String userId, String content) {
        Journey journey = requireOwnedJourney(journeyId, userId);
        if (journey.getStatus() != JourneyStatus.INTERVIEW_ACTIVE) {
            throw new ApiException("JOURNEY_STATE_INVALID", "当前不在访谈阶段", HttpStatus.CONFLICT);
        }

        InterviewSessionEntity session = getInterview(journeyId, userId);
        InterviewTurnEntity userTurn = createTurn("user", content, null, null);
        session.getTurns().add(userTurn);

        List<MockDataFactory.MockInterviewQuestion> questions = MockDataFactory.interviewQuestions();
        int nextIndex = session.getQuestionIndex() + 1;
        InterviewTurnEntity agentTurn;

        if (nextIndex < questions.size()) {
            MockDataFactory.MockInterviewQuestion question = questions.get(nextIndex);
            agentTurn = createTurn(
                    "agent",
                    question.question(),
                    question.questionReason(),
                    question.targetCapabilities()
            );
            session.setQuestionIndex(nextIndex);
            session.setStage(question.stage());
            session.setCanGenerateProfile(nextIndex >= 1);
        } else {
            agentTurn = createTurn(
                    "agent",
                    "感谢补充。如果您暂无更多信息，可以结束访谈并生成人才画像。",
                    "访谈信息已较充分",
                    List.of()
            );
            session.setCanGenerateProfile(true);
            session.setStage("wrap_up");
        }

        session.getTurns().add(agentTurn);
        store.saveInterviewSession(session);
        return new InterviewTurnResponse(userTurn, agentTurn, session);
    }

    public InterviewSessionEntity skipInterviewQuestion(String journeyId, String userId) {
        InterviewSessionEntity session = getInterview(journeyId, userId);
        List<MockDataFactory.MockInterviewQuestion> questions = MockDataFactory.interviewQuestions();
        int nextIndex = Math.min(session.getQuestionIndex() + 1, questions.size() - 1);
        MockDataFactory.MockInterviewQuestion question = questions.get(nextIndex);

        InterviewTurnEntity agentTurn = createTurn(
                "agent",
                question.question(),
                question.questionReason(),
                question.targetCapabilities()
        );
        session.getTurns().add(agentTurn);
        session.setQuestionIndex(nextIndex);
        session.setCanGenerateProfile(true);
        store.saveInterviewSession(session);
        return session;
    }

    public Journey completeInterview(String journeyId, String userId) {
        Journey journey = requireOwnedJourney(journeyId, userId);
        InterviewSessionEntity session = getInterview(journeyId, userId);
        session.setStatus("completed");
        store.saveInterviewSession(session);

        journey.setStatus(JourneyStatus.INTERVIEW_COMPLETED);
        journey.setUpdatedAt(Instant.now());
        store.saveJourney(journey);
        return journey;
    }

    public AsyncTask startProfileGeneration(String journeyId, String userId) {
        Journey journey = requireOwnedJourney(journeyId, userId);
        if (journey.getStatus() != JourneyStatus.INTERVIEW_COMPLETED
                && journey.getStatus() != JourneyStatus.INTERVIEW_ACTIVE) {
            throw new ApiException("JOURNEY_STATE_INVALID", "请先完成访谈", HttpStatus.CONFLICT);
        }

        journey.setStatus(JourneyStatus.PROFILE_GENERATING);
        journey.setUpdatedAt(Instant.now());
        store.saveJourney(journey);

        AsyncTask task = taskService.createTask(journeyId, TaskType.GENERATE_PROFILE, Map.of());
        taskService.runGenerateProfileTask(task.getId());
        return task;
    }

    public TalentProfileEntity getProfile(String journeyId, String userId) {
        requireOwnedJourney(journeyId, userId);
        return store.findTalentProfileByJourneyId(journeyId)
                .orElseThrow(() -> new ApiException("PROFILE_NOT_READY", "人才画像尚未生成", HttpStatus.CONFLICT));
    }

    public List<ResumeVersionEntity> listResumeVersions(String journeyId, String userId) {
        requireOwnedJourney(journeyId, userId);
        return store.findResumeVersionsByJourneyId(journeyId);
    }

    public AsyncTask startResumeVersionGeneration(String journeyId, String userId, String versionKey) {
        if (!ALLOWED_VERSION_KEYS.contains(versionKey)) {
            throw new ApiException("BAD_REQUEST", "不支持的简历版本: " + versionKey, HttpStatus.BAD_REQUEST);
        }
        Journey journey = requireOwnedJourney(journeyId, userId);
        store.findTalentProfileByJourneyId(journeyId)
                .orElseThrow(() -> new ApiException("PROFILE_NOT_READY", "请先生成人才画像", HttpStatus.UNPROCESSABLE_ENTITY));

        journey.setStatus(JourneyStatus.RESUME_GENERATING);
        journey.setUpdatedAt(Instant.now());
        store.saveJourney(journey);

        AsyncTask task = taskService.createTask(
                journeyId,
                TaskType.GENERATE_RESUME_VERSION,
                Map.of("versionKey", versionKey)
        );
        taskService.runGenerateResumeVersionTask(task.getId(), versionKey);
        return task;
    }

    public ResumeVersionEntity getResumeVersion(String journeyId, String userId, String versionKey) {
        requireOwnedJourney(journeyId, userId);
        return store.findResumeVersionByJourneyIdAndKey(journeyId, versionKey)
                .orElseThrow(() -> new ApiException("RESUME_VERSION_NOT_FOUND", "简历版本不存在", HttpStatus.NOT_FOUND));
    }

    public AsyncTask startResumeExport(String journeyId, String userId, String versionKey, String format) {
        if (!Set.of("pdf", "docx").contains(format)) {
            throw new ApiException("BAD_REQUEST", "不支持的导出格式: " + format, HttpStatus.BAD_REQUEST);
        }
        requireOwnedJourney(journeyId, userId);
        store.findResumeVersionByJourneyIdAndKey(journeyId, versionKey)
                .orElseThrow(() -> new ApiException("RESUME_VERSION_NOT_FOUND", "请先生成该简历版本", HttpStatus.NOT_FOUND));

        AsyncTask task = taskService.createTask(
                journeyId,
                TaskType.EXPORT_RESUME,
                Map.of("versionKey", versionKey, "format", format)
        );
        taskService.runExportResumeTask(task.getId(), versionKey, format);
        return task;
    }

    private Journey requireOwnedJourney(String journeyId, String userId) {
        Journey journey = store.findJourneyById(journeyId)
                .orElseThrow(() -> new ApiException("JOURNEY_NOT_FOUND", "旅程不存在", HttpStatus.NOT_FOUND));
        if (!journey.getUserId().equals(userId)) {
            throw new ApiException("AUTH_ACCESS_DENIED", "无权访问该旅程", HttpStatus.FORBIDDEN);
        }
        return journey;
    }

    private void validateUploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("BAD_REQUEST", "请选择文件", HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ApiException("RESUME_FILE_TOO_LARGE", "文件超过 20MB", HttpStatus.PAYLOAD_TOO_LARGE);
        }
        String extension = extension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ApiException("RESUME_FILE_TYPE_UNSUPPORTED", "仅支持 PDF、Word、Docx", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    private String persistFile(String fileId, String extension, MultipartFile file) {
        try {
            Files.createDirectories(uploadDir);
            Path target = uploadDir.resolve(fileId + "." + extension);
            file.transferTo(target);
            return target.toString();
        } catch (IOException ex) {
            throw new ApiException("INTERNAL_ERROR", "文件保存失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(Locale.ROOT);
    }

    private void mergeBasicInfo(CandidateInfo target, CandidateInfo patch) {
        if (patch.getName() != null) {
            target.setName(patch.getName());
        }
        if (patch.getTitle() != null) {
            target.setTitle(patch.getTitle());
        }
        if (patch.getLocation() != null) {
            target.setLocation(patch.getLocation());
        }
        if (patch.getExperienceYears() > 0) {
            target.setExperienceYears(patch.getExperienceYears());
        }
        if (patch.getEducation() != null) {
            target.setEducation(patch.getEducation());
        }
        if (patch.getPhone() != null) {
            target.setPhone(patch.getPhone());
        }
        if (patch.getEmail() != null) {
            target.setEmail(patch.getEmail());
        }
    }

    private void validateResumeForInterview(StructuredResumeEntity resume) {
        if (resume.getBasicInfo().getName() == null || resume.getBasicInfo().getName().isBlank()) {
            throw new ApiException("JOURNEY_STATE_INVALID", "请先填写姓名", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private void appendFirstQuestion(InterviewSessionEntity session) {
        MockDataFactory.MockInterviewQuestion question = MockDataFactory.interviewQuestions().get(0);
        InterviewTurnEntity agentTurn = createTurn(
                "agent",
                question.question(),
                question.questionReason(),
                question.targetCapabilities()
        );
        session.getTurns().add(agentTurn);
        session.setQuestionIndex(0);
        session.setStage(question.stage());
    }

    private InterviewTurnEntity createTurn(
            String role,
            String content,
            String questionReason,
            List<String> targetCapabilities
    ) {
        InterviewTurnEntity turn = new InterviewTurnEntity();
        turn.setId(Ids.next("turn"));
        turn.setRole(role);
        turn.setContent(content);
        turn.setQuestionReason(questionReason);
        if (targetCapabilities != null) {
            turn.setTargetCapabilities(targetCapabilities);
        }
        return turn;
    }

    public record StructuredResumePatch(
            CandidateInfo basicInfo,
            List<String> education,
            List<String> workExperience,
            List<String> projects,
            List<String> skills
    ) {
    }

    public record InterviewTurnResponse(
            InterviewTurnEntity userTurn,
            InterviewTurnEntity agentTurn,
            InterviewSessionEntity session
    ) {
    }
}
