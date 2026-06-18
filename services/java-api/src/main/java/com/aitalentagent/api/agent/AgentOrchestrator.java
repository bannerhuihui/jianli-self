package com.aitalentagent.api.agent;

import com.aitalentagent.api.common.ApiException;
import com.aitalentagent.api.common.Ids;
import com.aitalentagent.api.config.AppProperties;
import com.aitalentagent.api.domain.*;
import com.aitalentagent.api.llm.*;
import com.aitalentagent.api.parser.ResumeTextExtractor;
import com.aitalentagent.api.repository.AppStore;
import com.aitalentagent.api.service.JourneyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(AgentOrchestrator.class);

    private final AppProperties appProperties;
    private final AppStore store;
    private final LlmClient llmClient;
    private final PromptLoader promptLoader;
    private final AgentJsonParser jsonParser;
    private final ResumeTextExtractor resumeTextExtractor;
    private final ObjectMapper objectMapper;

    public AgentOrchestrator(
            AppProperties appProperties,
            AppStore store,
            LlmClient llmClient,
            PromptLoader promptLoader,
            AgentJsonParser jsonParser,
            ResumeTextExtractor resumeTextExtractor,
            ObjectMapper objectMapper
    ) {
        this.appProperties = appProperties;
        this.store = store;
        this.llmClient = llmClient;
        this.promptLoader = promptLoader;
        this.jsonParser = jsonParser;
        this.resumeTextExtractor = resumeTextExtractor;
        this.objectMapper = objectMapper;
    }

    public boolean isLlmMode() {
        return "llm".equalsIgnoreCase(appProperties.getAgents().getMode());
    }

    public StructuredResumeEntity parseResume(String journeyId, String fileId) {
        if (!isLlmMode()) {
            return MockDataFactory.createStructuredResume(journeyId);
        }

        ResumeFileEntity file = store.findResumeFileById(fileId)
                .orElseGet(() -> store.findLatestResumeFileByJourneyId(journeyId)
                        .orElseThrow(() -> new ApiException("JOURNEY_STATE_INVALID", "请先上传简历文件", HttpStatus.CONFLICT)));

        String resumeText = resumeTextExtractor.extract(file.getStoragePath(), file.getFileType());
        if (resumeText.isBlank()) {
            throw new ApiException(
                    "RESUME_PARSE_FAILED",
                    "未能从文件中提取文本，请上传可选中文字的 PDF 或 Word",
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        store.saveResumeRawText(journeyId, file.getId(), resumeText, file.getFileType());

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("fileName", file.getFileName());
        input.put("fileType", file.getFileType());
        input.put("resumeText", resumeText);

        JsonNode output = callAgent(
                "resume",
                appProperties.getLlm().getVolcengine().getEndpoints().getResume(),
                appProperties.getLlm().getVolcengine().getAgentOptions().getResumeTemperature(),
                promptLoader.load("resume_agent_v1.md"),
                input
        );
        return mapStructuredResume(journeyId, output);
    }

    public JourneyService.InterviewTurnResponse processInterviewTurn(
            String journeyId,
            InterviewSessionEntity session,
            String userContent,
            StructuredResumeEntity resume
    ) {
        if (!isLlmMode()) {
            return mockInterviewTurn(session, userContent);
        }

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("structuredResume", resume);
        input.put("previousTurns", session.getTurns());
        input.put("missingEvidence", session.getMissingEvidence());
        input.put("currentStage", session.getStage());
        input.put("latestUserAnswer", userContent);

        JsonNode output = callAgent(
                "interview",
                appProperties.getLlm().getVolcengine().getEndpoints().getInterview(),
                appProperties.getLlm().getVolcengine().getAgentOptions().getInterviewTemperature(),
                promptLoader.load("interview_agent_v1.md"),
                input
        );

        InterviewTurnEntity userTurn = createTurn("user", userContent, null, null);
        InterviewTurnEntity agentTurn = createTurn(
                "agent",
                text(output, "question"),
                text(output, "questionReason"),
                readStringList(output.path("targetCapabilities"))
        );

        session.getTurns().add(userTurn);
        session.getTurns().add(agentTurn);
        session.setQuestionIndex(session.getQuestionIndex() + 1);
        session.setStage(text(output, "stage", session.getStage()));
        session.setCanGenerateProfile(output.path("canGenerateProfile").asBoolean(session.isCanGenerateProfile()));
        if (output.has("missingEvidence")) {
            session.setMissingEvidence(readStringList(output.path("missingEvidence")));
        }

        return new JourneyService.InterviewTurnResponse(userTurn, agentTurn, session);
    }

    public InterviewSessionEntity startInterview(String journeyId) {
        if (!isLlmMode()) {
            InterviewSessionEntity session = new InterviewSessionEntity();
            session.setId(Ids.next("iv"));
            session.setJourneyId(journeyId);
            session.setMissingEvidence(List.of("communicationAbility", "leadershipAbility"));
            MockDataFactory.MockInterviewQuestion question = MockDataFactory.interviewQuestions().get(0);
            session.getTurns().add(createTurn(
                    "agent",
                    question.question(),
                    question.questionReason(),
                    question.targetCapabilities()
            ));
            session.setQuestionIndex(0);
            session.setStage(question.stage());
            return session;
        }

        StructuredResumeEntity resume = store.findStructuredResumeByJourneyId(journeyId)
                .orElseThrow(() -> new ApiException("JOURNEY_STATE_INVALID", "结构化简历尚未生成", HttpStatus.CONFLICT));

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("structuredResume", resume);
        input.put("previousTurns", List.of());
        input.put("missingEvidence", List.of("communicationAbility", "executionAbility", "leadershipAbility"));
        input.put("currentStage", "experience_exploration");
        input.put("latestUserAnswer", "");

        JsonNode output = callAgent(
                "interview",
                appProperties.getLlm().getVolcengine().getEndpoints().getInterview(),
                appProperties.getLlm().getVolcengine().getAgentOptions().getInterviewTemperature(),
                promptLoader.load("interview_agent_v1.md"),
                input
        );

        InterviewSessionEntity session = new InterviewSessionEntity();
        session.setId(Ids.next("iv"));
        session.setJourneyId(journeyId);
        session.setStage(text(output, "stage", "experience_exploration"));
        session.setMissingEvidence(readStringList(output.path("missingEvidence")));
        session.setCanGenerateProfile(output.path("canGenerateProfile").asBoolean(false));
        session.getTurns().add(createTurn(
                "agent",
                text(output, "question"),
                text(output, "questionReason"),
                readStringList(output.path("targetCapabilities"))
        ));
        session.setQuestionIndex(0);
        return session;
    }

    public TalentProfileEntity generateProfile(String journeyId, StructuredResumeEntity resume) {
        if (!isLlmMode()) {
            return MockDataFactory.createTalentProfile(journeyId, resume.getBasicInfo());
        }

        InterviewSessionEntity interview = store.findInterviewSessionByJourneyId(journeyId).orElse(null);
        List<String> interviewFacts = new ArrayList<>();
        if (interview != null) {
            interview.getTurns().stream()
                    .filter(turn -> "user".equals(turn.getRole()))
                    .map(InterviewTurnEntity::getContent)
                    .forEach(interviewFacts::add);
        }

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("structuredResume", resume);
        input.put("interviewFacts", interviewFacts);

        JsonNode output = callAgent(
                "profile",
                appProperties.getLlm().getVolcengine().getEndpoints().getProfile(),
                appProperties.getLlm().getVolcengine().getAgentOptions().getProfileTemperature(),
                promptLoader.load("profile_agent_v1.md"),
                input
        );
        return mapTalentProfile(journeyId, resume.getBasicInfo(), output);
    }

    public ResumeVersionEntity generateResumeVersion(String journeyId, String versionKey, TalentProfileEntity profile) {
        if (!isLlmMode()) {
            return MockDataFactory.createResumeVersion(journeyId, versionKey, profile.getCandidate());
        }

        StructuredResumeEntity resume = store.findStructuredResumeByJourneyId(journeyId)
                .orElseThrow(() -> new ApiException("JOURNEY_STATE_INVALID", "结构化简历尚未生成", HttpStatus.CONFLICT));

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("versionKey", versionKey);
        input.put("structuredResume", resume);
        input.put("talentProfile", profile);

        JsonNode output = callAgent(
                "resume-builder",
                appProperties.getLlm().getVolcengine().getEndpoints().getResumeBuilder(),
                appProperties.getLlm().getVolcengine().getAgentOptions().getResumeBuilderTemperature(),
                promptLoader.load("resume_builder_agent_v1.md"),
                input
        );
        return mapResumeVersion(journeyId, versionKey, output);
    }

    private JsonNode callAgent(
            String agentName,
            String endpointId,
            double temperature,
            String systemPrompt,
            Map<String, Object> input
    ) {
        try {
            String userContent = objectMapper.writeValueAsString(input);
            LlmCompletionResult result = llmClient.complete(
                    endpointId,
                    List.of(
                            new LlmMessage("system", systemPrompt),
                            new LlmMessage("user", userContent)
                    ),
                    new LlmCompletionOptions(temperature, true)
            );
            log.info("Agent {} model={} latencyMs={}", agentName, result.model(), result.latencyMs());
            return jsonParser.parse(result.content());
        } catch (JsonProcessingException ex) {
            throw new ApiException("LLM_PARSE_FAILED", "序列化 Agent 输入失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private StructuredResumeEntity mapStructuredResume(String journeyId, JsonNode output) {
        StructuredResumeEntity resume = new StructuredResumeEntity();
        resume.setId(Ids.next("sr"));
        resume.setJourneyId(journeyId);
        resume.setParseQualityScore(output.path("parseQualityScore").asDouble(0.7));
        resume.setConfidence(text(output, "confidence", "medium"));
        resume.setWarnings(readStringList(output.path("warnings")));
        resume.setMissingFields(readStringList(output.path("missingFields")));
        resume.setEducation(readResumeStringList(output.path("education")));
        resume.setWorkExperience(readResumeStringList(output.path("workExperience")));
        resume.setProjects(readResumeStringList(output.path("projects")));
        resume.setSkills(readResumeStringList(output.path("skills")));

        JsonNode basicInfo = output.path("basicInfo");
        CandidateInfo candidate = new CandidateInfo();
        candidate.setId(Ids.next("candidate"));
        candidate.setName(text(basicInfo, "name"));
        candidate.setTitle(text(basicInfo, "title"));
        candidate.setLocation(text(basicInfo, "location"));
        candidate.setExperienceYears(basicInfo.path("experienceYears").asInt(0));
        candidate.setEducation(text(basicInfo, "education"));
        candidate.setPhone(text(basicInfo, "phone"));
        candidate.setEmail(text(basicInfo, "email"));
        resume.setBasicInfo(candidate);

        if (resume.getEducation().isEmpty() && !candidate.getEducation().isBlank()) {
            resume.setEducation(List.of(candidate.getEducation()));
        }
        return resume;
    }

    private TalentProfileEntity mapTalentProfile(String journeyId, CandidateInfo sourceCandidate, JsonNode output) {
        TalentProfileEntity profile = new TalentProfileEntity();
        profile.setId(Ids.next("tp"));
        profile.setJourneyId(journeyId);
        profile.setCandidate(copyCandidate(sourceCandidate));
        profile.setSummary(text(output, "summary"));
        profile.setOverallScore(output.path("overallScore").asInt(80));
        profile.setStrengths(readStringList(output.path("strengths")));
        profile.setRisks(readStringList(output.path("risks")));
        profile.setPreferences(readStringList(output.path("preferences")));
        profile.setRecommendedRoles(readStringList(output.path("recommendedRoles")));
        profile.setConfidence(text(output, "confidence", "medium"));

        List<CapabilityScoreEntity> capabilities = new ArrayList<>();
        if (output.path("capabilities").isArray()) {
            for (JsonNode item : output.path("capabilities")) {
                CapabilityScoreEntity capability = new CapabilityScoreEntity();
                capability.setKey(text(item, "key"));
                capability.setName(text(item, "name"));
                capability.setScore(item.path("score").asDouble(3));
                capability.setConfidence(text(item, "confidence", "medium"));
                capability.setReason(text(item, "reason"));
                capability.setEvidenceIds(readStringList(item.path("evidenceIds")));
                capabilities.add(capability);
            }
        }
        profile.setCapabilities(capabilities);

        List<EvidenceEntity> evidenceList = new ArrayList<>();
        if (output.path("evidence").isArray()) {
            for (JsonNode item : output.path("evidence")) {
                EvidenceEntity evidence = new EvidenceEntity();
                evidence.setId(text(item, "id", Ids.next("ev")));
                evidence.setSource(text(item, "source", "resume"));
                evidence.setSnippet(text(item, "snippet"));
                evidence.setCapabilityKeys(readStringList(item.path("capabilityKeys")));
                evidenceList.add(evidence);
            }
        }
        profile.setEvidence(evidenceList);
        return profile;
    }

    private ResumeVersionEntity mapResumeVersion(String journeyId, String versionKey, JsonNode output) {
        ResumeVersionEntity version = new ResumeVersionEntity();
        version.setId(Ids.next("rv"));
        version.setJourneyId(journeyId);
        version.setVersionKey(text(output, "versionKey", versionKey));
        version.setTitle(text(output, "title"));
        version.setContent(text(output, "content"));
        version.setConfidence(text(output, "confidence", "medium"));
        version.setWarnings(readStringList(output.path("warnings")));
        version.setUsedEvidenceIds(readStringList(output.path("usedEvidenceIds")));
        return version;
    }

    private JourneyService.InterviewTurnResponse mockInterviewTurn(InterviewSessionEntity session, String userContent) {
        InterviewTurnEntity userTurn = createTurn("user", userContent, null, null);
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
        return new JourneyService.InterviewTurnResponse(userTurn, agentTurn, session);
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

    private CandidateInfo copyCandidate(CandidateInfo source) {
        CandidateInfo copy = new CandidateInfo();
        copy.setId(source.getId());
        copy.setName(source.getName());
        copy.setTitle(source.getTitle());
        copy.setLocation(source.getLocation());
        copy.setExperienceYears(source.getExperienceYears());
        copy.setEducation(source.getEducation());
        copy.setPhone(source.getPhone());
        copy.setEmail(source.getEmail());
        return copy;
    }

    private List<String> readStringList(JsonNode node) {
        List<String> values = new ArrayList<>();
        if (node != null && node.isArray()) {
            node.forEach(item -> {
                if (!item.isNull()) {
                    values.add(item.asText());
                }
            });
        }
        return values;
    }

    private List<String> readResumeStringList(JsonNode node) {
        List<String> values = new ArrayList<>();
        if (node == null || !node.isArray()) {
            return values;
        }
        node.forEach(item -> {
            String line = formatResumeLine(item);
            if (!line.isBlank()) {
                values.add(line);
            }
        });
        return values;
    }

    private String formatResumeLine(JsonNode item) {
        if (item == null || item.isNull()) {
            return "";
        }
        if (item.isTextual() || item.isNumber()) {
            return item.asText("").trim();
        }
        if (!item.isObject()) {
            return item.asText("").trim();
        }

        for (String key : List.of("line", "text", "summary", "description", "content")) {
            String value = text(item, key);
            if (!value.isBlank()) {
                return value;
            }
        }

        List<String> parts = new ArrayList<>();
        appendIfPresent(parts, item, "company", "organization", "employer", "school", "institution", "university");
        appendIfPresent(parts, item, "title", "position", "role", "major", "degree", "name", "project");
        appendIfPresent(parts, item, "period", "time", "duration", "startDate", "endDate", "start", "end");
        if (!parts.isEmpty()) {
            return String.join(" · ", parts);
        }

        item.fields().forEachRemaining(entry -> {
            JsonNode value = entry.getValue();
            if (value != null && value.isValueNode() && !value.isNull()) {
                String text = value.asText("").trim();
                if (!text.isBlank()) {
                    parts.add(text);
                }
            }
        });
        return String.join(" · ", parts);
    }

    private void appendIfPresent(List<String> parts, JsonNode node, String... fields) {
        for (String field : fields) {
            String value = text(node, field);
            if (!value.isBlank() && !parts.contains(value)) {
                parts.add(value);
            }
        }
    }

    private String text(JsonNode node, String field) {
        return text(node, field, "");
    }

    private String text(JsonNode node, String field, String defaultValue) {
        if (node == null || node.isMissingNode()) {
            return defaultValue;
        }
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? defaultValue : value.asText(defaultValue);
    }
}
