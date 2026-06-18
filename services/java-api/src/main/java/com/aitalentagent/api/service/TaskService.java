package com.aitalentagent.api.service;

import com.aitalentagent.api.agent.AgentOrchestrator;
import com.aitalentagent.api.common.ApiException;
import com.aitalentagent.api.common.Ids;
import com.aitalentagent.api.config.AppProperties;
import com.aitalentagent.api.domain.*;
import com.aitalentagent.api.repository.AppStore;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskService {

    private final AppStore store;
    private final AppProperties appProperties;
    private final AgentOrchestrator agentOrchestrator;

    public TaskService(
            AppStore store,
            AppProperties appProperties,
            AgentOrchestrator agentOrchestrator
    ) {
        this.store = store;
        this.appProperties = appProperties;
        this.agentOrchestrator = agentOrchestrator;
    }

    public AsyncTask createTask(String journeyId, TaskType type, Map<String, Object> metadata) {
        AsyncTask task = new AsyncTask();
        task.setId(Ids.next("task"));
        task.setJourneyId(journeyId);
        task.setType(type);
        task.setStatus(TaskStatus.PENDING);
        task.setMetadata(metadata == null ? Map.of() : metadata);
        store.saveTask(task);
        return task;
    }

    public AsyncTask getTask(String taskId, String userId) {
        AsyncTask task = store.findTaskById(taskId)
                .orElseThrow(() -> new ApiException("TASK_NOT_FOUND", "任务不存在", HttpStatus.NOT_FOUND));
        Journey journey = store.findJourneyById(task.getJourneyId())
                .orElseThrow(() -> new ApiException("JOURNEY_NOT_FOUND", "旅程不存在", HttpStatus.NOT_FOUND));
        if (!journey.getUserId().equals(userId)) {
            throw new ApiException("AUTH_ACCESS_DENIED", "无权访问该任务", HttpStatus.FORBIDDEN);
        }
        return task;
    }

    @Async("taskExecutor")
    public void runParseResumeTask(String taskId) {
        executeTask(taskId, () -> {
            AsyncTask task = requireTask(taskId);
            Journey journey = requireJourney(task.getJourneyId());
            String fileId = task.getMetadata() == null ? null : String.valueOf(task.getMetadata().getOrDefault("fileId", ""));
            if (fileId == null || fileId.isBlank() || "null".equals(fileId)) {
                fileId = journey.getResumeFileId();
            }

            StructuredResumeEntity resume = agentOrchestrator.parseResume(journey.getId(), fileId);
            store.saveStructuredResume(resume);

            journey.setStructuredResumeId(resume.getId());
            journey.setStatus(JourneyStatus.RESUME_REVIEW);
            journey.setCurrentStep(JourneyStep.REVIEW);
            journey.getSteps().put(JourneyStep.UPLOAD, new Journey.StepProgress(true, Instant.now()));
            journey.setUpdatedAt(Instant.now());
            store.saveJourney(journey);

            Map<String, Object> result = new HashMap<>();
            result.put("structuredResumeId", resume.getId());
            result.put("parseQualityScore", resume.getParseQualityScore());
            result.put("confidence", resume.getConfidence());
            result.put("warnings", resume.getWarnings());
            result.put("missingFields", resume.getMissingFields());
            return result;
        });
    }

    @Async("taskExecutor")
    public void runGenerateProfileTask(String taskId) {
        executeTask(taskId, () -> {
            AsyncTask task = requireTask(taskId);
            Journey journey = requireJourney(task.getJourneyId());
            StructuredResumeEntity resume = store.findStructuredResumeByJourneyId(journey.getId())
                    .orElseThrow(() -> new ApiException("JOURNEY_STATE_INVALID", "请先完成简历校对", HttpStatus.CONFLICT));

            TalentProfileEntity profile = agentOrchestrator.generateProfile(journey.getId(), resume);
            store.saveTalentProfile(profile);

            journey.setTalentProfileId(profile.getId());
            journey.setStatus(JourneyStatus.PROFILE_READY);
            journey.setCurrentStep(JourneyStep.PROFILE);
            journey.getSteps().put(JourneyStep.INTERVIEW, new Journey.StepProgress(true, Instant.now()));
            journey.getSteps().put(JourneyStep.PROFILE, new Journey.StepProgress(true, Instant.now()));
            journey.setUpdatedAt(Instant.now());
            store.saveJourney(journey);

            return Map.of("talentProfileId", profile.getId());
        });
    }

    @Async("taskExecutor")
    public void runGenerateResumeVersionTask(String taskId, String versionKey) {
        executeTask(taskId, () -> {
            AsyncTask task = requireTask(taskId);
            Journey journey = requireJourney(task.getJourneyId());
            TalentProfileEntity profile = store.findTalentProfileByJourneyId(journey.getId())
                    .orElseThrow(() -> new ApiException("PROFILE_NOT_READY", "请先生成人才画像", HttpStatus.UNPROCESSABLE_ENTITY));

            ResumeVersionEntity version = agentOrchestrator.generateResumeVersion(
                    journey.getId(), versionKey, profile
            );
            store.saveResumeVersion(version);

            journey.setStatus(JourneyStatus.COMPLETED);
            journey.setCurrentStep(JourneyStep.RESUME);
            journey.getSteps().put(JourneyStep.RESUME, new Journey.StepProgress(true, Instant.now()));
            journey.setUpdatedAt(Instant.now());
            store.saveJourney(journey);

            return Map.of(
                    "resumeVersionId", version.getId(),
                    "versionKey", version.getVersionKey()
            );
        });
    }

    @Async("taskExecutor")
    public void runExportResumeTask(String taskId, String versionKey, String format) {
        executeTask(taskId, () -> {
            AsyncTask task = requireTask(taskId);
            Journey journey = requireJourney(task.getJourneyId());
            ResumeVersionEntity version = store.findResumeVersionByJourneyIdAndKey(journey.getId(), versionKey)
                    .orElseThrow(() -> new ApiException("RESUME_VERSION_NOT_FOUND", "简历版本不存在", HttpStatus.NOT_FOUND));

            String fileName = "resume-" + versionKey + "-" + version.getTitle().replace(" ", "") + "." + format;
            Map<String, Object> result = new HashMap<>();
            result.put("downloadUrl", "https://mock.local/exports/" + version.getId() + "." + format);
            result.put("expiresAt", Instant.now().plusSeconds(3600).toString());
            result.put("fileName", fileName);
            return result;
        });
    }

    private void executeTask(String taskId, TaskRunner runner) {
        AsyncTask task = requireTask(taskId);
        task.setStatus(TaskStatus.RUNNING);
        task.setProgress(10);
        task.setUpdatedAt(Instant.now());
        store.saveTask(task);

        try {
            if (!agentOrchestrator.isLlmMode()) {
                Thread.sleep(appProperties.getAgents().getMockDelayMs());
            }
            task.setProgress(80);
            task.setUpdatedAt(Instant.now());
            store.saveTask(task);

            Map<String, Object> result = runner.run();
            task.setStatus(TaskStatus.SUCCEEDED);
            task.setProgress(100);
            task.setResult(result);
            task.setCompletedAt(Instant.now());
            task.setUpdatedAt(Instant.now());
            store.saveTask(task);
        } catch (ApiException ex) {
            failTask(task, ex.getCode(), ex.getMessage());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            failTask(task, "TASK_FAILED", "任务被中断");
        } catch (Exception ex) {
            failTask(task, "TASK_FAILED", "任务执行失败: " + ex.getMessage());
        }
    }

    private void failTask(AsyncTask task, String code, String message) {
        task.setStatus(TaskStatus.FAILED);
        task.setError(Map.of("code", code, "message", message));
        task.setCompletedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        store.saveTask(task);
    }

    private AsyncTask requireTask(String taskId) {
        return store.findTaskById(taskId)
                .orElseThrow(() -> new ApiException("TASK_NOT_FOUND", "任务不存在", HttpStatus.NOT_FOUND));
    }

    private Journey requireJourney(String journeyId) {
        return store.findJourneyById(journeyId)
                .orElseThrow(() -> new ApiException("JOURNEY_NOT_FOUND", "旅程不存在", HttpStatus.NOT_FOUND));
    }

    @FunctionalInterface
    private interface TaskRunner {
        Map<String, Object> run();
    }
}
