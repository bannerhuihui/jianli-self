package com.aitalentagent.api.web;

import com.aitalentagent.api.auth.AuthContext;
import com.aitalentagent.api.common.ApiResponse;
import com.aitalentagent.api.domain.*;
import com.aitalentagent.api.service.JourneyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/journeys")
public class JourneyController {

    private final JourneyService journeyService;

    public JourneyController(JourneyService journeyService) {
        this.journeyService = journeyService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createJourney(HttpServletRequest request) {
        Journey journey = journeyService.createJourney(userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ApiMapper.journey(journey), requestId(request)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Map<String, Object>>> activeJourney(HttpServletRequest request) {
        Journey journey = journeyService.getActiveJourney(userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.journey(journey), requestId(request)));
    }

    @GetMapping("/{journeyId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getJourney(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        Journey journey = journeyService.getJourney(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.journey(journey), requestId(request)));
    }

    @PostMapping("/{journeyId}/resume-files")
    public ResponseEntity<ApiResponse<Map<String, Object>>> uploadResume(
            @PathVariable String journeyId,
            @RequestPart("file") MultipartFile file,
            HttpServletRequest request
    ) {
        ResumeFileEntity entity = journeyService.uploadResumeFile(journeyId, userId(), file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ApiMapper.resumeFile(entity), requestId(request)));
    }

    @PostMapping("/{journeyId}/parse-resume")
    public ResponseEntity<ApiResponse<Map<String, Object>>> parseResume(
            @PathVariable String journeyId,
            @RequestBody(required = false) ParseResumeRequest body,
            HttpServletRequest request
    ) {
        String fileId = body == null ? null : body.fileId();
        AsyncTask task = journeyService.startParseResume(journeyId, userId(), fileId);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.of(ApiMapper.taskAccepted(task), requestId(request)));
    }

    @PostMapping("/{journeyId}/structured-resume/manual")
    public ResponseEntity<ApiResponse<Map<String, Object>>> manualStructuredResume(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        StructuredResumeEntity resume = journeyService.createManualStructuredResume(journeyId, userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ApiMapper.structuredResume(resume), requestId(request)));
    }

    @GetMapping("/{journeyId}/structured-resume")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStructuredResume(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        StructuredResumeEntity resume = journeyService.getStructuredResume(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.structuredResume(resume), requestId(request)));
    }

    @GetMapping("/{journeyId}/resume-file")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getResumeFile(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        ResumeFileEntity file = journeyService.getResumeFile(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.resumeFile(file), requestId(request)));
    }

    @PatchMapping("/{journeyId}/structured-resume")
    public ResponseEntity<ApiResponse<Map<String, Object>>> patchStructuredResume(
            @PathVariable String journeyId,
            @RequestBody StructuredResumePatchRequest body,
            HttpServletRequest request
    ) {
        StructuredResumeEntity resume = journeyService.patchStructuredResume(
                journeyId,
                userId(),
                new JourneyService.StructuredResumePatch(
                        body.basicInfo(),
                        body.education(),
                        body.workExperience(),
                        body.projects(),
                        body.skills()
                )
        );
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.structuredResume(resume), requestId(request)));
    }

    @PostMapping("/{journeyId}/structured-resume/confirm")
    public ResponseEntity<ApiResponse<Map<String, Object>>> confirmStructuredResume(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        InterviewSessionEntity session = journeyService.confirmStructuredResume(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(Map.of(
                "interviewSessionId", session.getId(),
                "stage", session.getStage(),
                "journeyStatus", JourneyStatus.INTERVIEW_ACTIVE.name().toLowerCase()
        ), requestId(request)));
    }

    @GetMapping("/{journeyId}/interview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getInterview(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        InterviewSessionEntity session = journeyService.getInterview(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.interviewSession(session), requestId(request)));
    }

    @PostMapping("/{journeyId}/interview/turns")
    public ResponseEntity<ApiResponse<Map<String, Object>>> submitInterviewTurn(
            @PathVariable String journeyId,
            @Valid @RequestBody InterviewTurnRequest body,
            HttpServletRequest request
    ) {
        JourneyService.InterviewTurnResponse response = journeyService.submitInterviewTurn(
                journeyId, userId(), body.content()
        );
        return ResponseEntity.ok(ApiResponse.of(Map.of(
                "userTurn", ApiMapper.turn(response.userTurn()),
                "agentTurn", ApiMapper.turn(response.agentTurn()),
                "missingEvidence", response.session().getMissingEvidence(),
                "canGenerateProfile", response.session().isCanGenerateProfile(),
                "stage", response.session().getStage()
        ), requestId(request)));
    }

    @PostMapping("/{journeyId}/interview/skip")
    public ResponseEntity<ApiResponse<Map<String, Object>>> skipInterview(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        InterviewSessionEntity session = journeyService.skipInterviewQuestion(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.interviewSession(session), requestId(request)));
    }

    @PostMapping("/{journeyId}/interview/complete")
    public ResponseEntity<ApiResponse<Map<String, Object>>> completeInterview(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        Journey journey = journeyService.completeInterview(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(Map.of(
                "journeyId", journey.getId(),
                "status", journey.getStatus().name().toLowerCase()
        ), requestId(request)));
    }

    @PostMapping("/{journeyId}/profile/generate")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateProfile(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        AsyncTask task = journeyService.startProfileGeneration(journeyId, userId());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.of(ApiMapper.taskAccepted(task), requestId(request)));
    }

    @GetMapping("/{journeyId}/profile")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProfile(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        TalentProfileEntity profile = journeyService.getProfile(journeyId, userId());
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.talentProfile(profile), requestId(request)));
    }

    @GetMapping("/{journeyId}/resume-versions")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> listResumeVersions(
            @PathVariable String journeyId,
            HttpServletRequest request
    ) {
        List<Map<String, Object>> versions = journeyService.listResumeVersions(journeyId, userId()).stream()
                .map(ApiMapper::resumeVersion)
                .toList();
        return ResponseEntity.ok(ApiResponse.of(versions, requestId(request)));
    }

    @PostMapping("/{journeyId}/resume-versions")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateResumeVersion(
            @PathVariable String journeyId,
            @Valid @RequestBody ResumeVersionRequest body,
            HttpServletRequest request
    ) {
        AsyncTask task = journeyService.startResumeVersionGeneration(journeyId, userId(), body.versionKey());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.of(ApiMapper.taskAccepted(task), requestId(request)));
    }

    @GetMapping("/{journeyId}/resume-versions/{versionKey}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getResumeVersion(
            @PathVariable String journeyId,
            @PathVariable String versionKey,
            HttpServletRequest request
    ) {
        ResumeVersionEntity version = journeyService.getResumeVersion(journeyId, userId(), versionKey);
        return ResponseEntity.ok(ApiResponse.of(ApiMapper.resumeVersion(version), requestId(request)));
    }

    @PostMapping("/{journeyId}/resume-versions/{versionKey}/export")
    public ResponseEntity<ApiResponse<Map<String, Object>>> exportResumeVersion(
            @PathVariable String journeyId,
            @PathVariable String versionKey,
            @Valid @RequestBody ExportResumeRequest body,
            HttpServletRequest request
    ) {
        AsyncTask task = journeyService.startResumeExport(journeyId, userId(), versionKey, body.format());
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.of(ApiMapper.taskAccepted(task), requestId(request)));
    }

    private String userId() {
        return AuthContext.requireUser().userId();
    }

    private String requestId(HttpServletRequest request) {
        return request.getAttribute("requestId").toString();
    }

    public record ParseResumeRequest(String fileId) {
    }

    public record StructuredResumePatchRequest(
            CandidateInfo basicInfo,
            List<String> education,
            List<String> workExperience,
            List<String> projects,
            List<String> skills
    ) {
    }

    public record InterviewTurnRequest(@NotBlank String content) {
    }

    public record ResumeVersionRequest(@NotBlank String versionKey) {
    }

    public record ExportResumeRequest(@NotBlank String format) {
    }
}
