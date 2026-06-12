package com.aitalentagent.api.agent;

import com.aitalentagent.api.common.Ids;
import com.aitalentagent.api.domain.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class MockDataFactory {

    private MockDataFactory() {
    }

    public static StructuredResumeEntity createStructuredResume(String journeyId) {
        StructuredResumeEntity resume = new StructuredResumeEntity();
        resume.setId(Ids.next("sr"));
        resume.setJourneyId(journeyId);
        resume.setParseQualityScore(0.82);
        resume.setConfidence("medium");
        resume.setWarnings(List.of("复杂表格可能影响工作经历提取"));
        resume.setMissingFields(List.of("certifications"));

        CandidateInfo basicInfo = new CandidateInfo();
        basicInfo.setId(Ids.next("candidate"));
        basicInfo.setName("张伟");
        basicInfo.setTitle("高级软件工程师");
        basicInfo.setLocation("上海");
        basicInfo.setExperienceYears(8);
        basicInfo.setEducation("清华大学 · 计算机科学与技术");
        basicInfo.setPhone("138-0000-0000");
        basicInfo.setEmail("zhangwei@example.com");
        resume.setBasicInfo(basicInfo);

        resume.setEducation(List.of("清华大学 · 计算机科学与技术 · 学士 · 2016-2020"));
        resume.setWorkExperience(List.of(
                "阿里巴巴 · 高级软件工程师 · 负责微服务架构升级，支撑千万级 QPS。",
                "主导自动化测试框架，减少人工回归测试成本。"
        ));
        resume.setProjects(List.of("AI 智能人才评估系统", "云服务分布式架构重构"));
        resume.setSkills(List.of("TypeScript", "Node.js", "React", "Kubernetes", "MySQL", "Redis"));
        return resume;
    }

    public static StructuredResumeEntity createEmptyStructuredResume(String journeyId) {
        StructuredResumeEntity resume = new StructuredResumeEntity();
        resume.setId(Ids.next("sr"));
        resume.setJourneyId(journeyId);
        resume.setParseQualityScore(0);
        resume.setConfidence("low");
        resume.setMissingFields(List.of("name", "workExperience", "education", "skills"));

        CandidateInfo basicInfo = new CandidateInfo();
        basicInfo.setId(Ids.next("candidate"));
        resume.setBasicInfo(basicInfo);
        return resume;
    }

    public static TalentProfileEntity createTalentProfile(String journeyId, CandidateInfo candidate) {
        TalentProfileEntity profile = new TalentProfileEntity();
        profile.setId(Ids.next("tp"));
        profile.setJourneyId(journeyId);
        profile.setCandidate(copyCandidate(candidate));
        profile.setSummary("该候选人具备较强的分布式架构设计能力，在复杂系统建设和技术推进方面有明确证据。适合技术专家或架构方向岗位。");
        profile.setOverallScore(94);
        profile.setStrengths(List.of("复杂系统架构经验丰富", "技术深度较强", "执行落地能力突出"));
        profile.setRisks(List.of("职业稳定性待验证", "管理经验需要进一步确认"));
        profile.setPreferences(List.of("技术深耕", "架构方向", "开放协作"));
        profile.setRecommendedRoles(List.of("资深架构专家", "技术中台负责人"));
        profile.setConfidence("medium");

        profile.setCapabilities(List.of(
                capability("learningAbility", "学习能力", 4.8, "high", "能快速掌握并应用新技术到复杂项目中。", "ev_001"),
                capability("logicAbility", "逻辑能力", 5.0, "high", "能清晰拆解复杂系统问题并给出方案。", "ev_002"),
                capability("communicationAbility", "沟通能力", 4.0, "medium", "有跨团队推进经验，但证据仍可继续补充。", "ev_003"),
                capability("executionAbility", "执行能力", 4.5, "high", "多次完成复杂项目交付。", "ev_001"),
                capability("innovationAbility", "创新能力", 4.9, "high", "提出自动化方案并产生业务价值。", "ev_002"),
                capability("leadershipAbility", "领导能力", 3.5, "medium", "有项目推进经验，但带团队证据不足。", "ev_003"),
                capability("stressTolerance", "抗压能力", 4.2, "high", "在高压系统升级中保持稳定交付。", "ev_001"),
                capability("careerStability", "职业稳定性", 2.0, "low", "近年变动较多，需要进一步确认动机。", "ev_004")
        ));

        EvidenceEntity evidence1 = new EvidenceEntity();
        evidence1.setId("ev_001");
        evidence1.setSource("resume");
        evidence1.setSnippet("主导微服务架构升级，支撑千万级 QPS。");
        evidence1.setCapabilityKeys(List.of("executionAbility", "stressTolerance"));

        EvidenceEntity evidence2 = new EvidenceEntity();
        evidence2.setId("ev_002");
        evidence2.setSource("resume");
        evidence2.setSnippet("云服务分布式架构重构项目。");
        evidence2.setCapabilityKeys(List.of("logicAbility", "innovationAbility"));

        profile.setEvidence(List.of(evidence1, evidence2));
        return profile;
    }

    public static ResumeVersionEntity createResumeVersion(String journeyId, String versionKey, CandidateInfo candidate) {
        ResumeVersionEntity version = new ResumeVersionEntity();
        version.setId(Ids.next("rv"));
        version.setJourneyId(journeyId);
        version.setVersionKey(versionKey);
        version.setTitle(candidate.getName() + " - " + candidate.getTitle());
        version.setContent(resumeVersionTexts().get(versionKey));
        version.setConfidence("medium");
        version.setWarnings(List.of("部分量化数据来自访谈，建议核对"));
        version.setUsedEvidenceIds(List.of("ev_001", "ev_002"));
        return version;
    }

    public static List<MockInterviewQuestion> interviewQuestions() {
        return List.of(
                new MockInterviewQuestion(
                        "你在最近一个项目中具体负责哪一部分？团队规模如何？",
                        "需要补充执行能力和协作能力证据",
                        List.of("executionAbility", "communicationAbility"),
                        "deep_dive"
                ),
                new MockInterviewQuestion(
                        "遇到技术方案分歧时，你通常如何推动决策？",
                        "需要补充沟通与逻辑能力证据",
                        List.of("communicationAbility", "logicAbility"),
                        "deep_dive"
                ),
                new MockInterviewQuestion(
                        "你希望下一份工作在哪些方面获得成长？",
                        "需要补充职业偏好与稳定性信号",
                        List.of("careerStability"),
                        "preference_exploration"
                )
        );
    }

    private static CapabilityScoreEntity capability(
            String key, String name, double score, String confidence, String reason, String evidenceId
    ) {
        CapabilityScoreEntity entity = new CapabilityScoreEntity();
        entity.setKey(key);
        entity.setName(name);
        entity.setScore(score);
        entity.setConfidence(confidence);
        entity.setReason(reason);
        entity.setEvidenceIds(new ArrayList<>(List.of(evidenceId)));
        return entity;
    }

    private static CandidateInfo copyCandidate(CandidateInfo source) {
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

    private static Map<String, String> resumeVersionTexts() {
        Map<String, String> texts = new LinkedHashMap<>();
        texts.put("ats", String.join("\n", List.of(
                "张伟 (Felix)",
                "高级全栈工程师 | 北京, 中国 | felix.zhang@example.com",
                "",
                "核心总结",
                "拥有 8 年以上构建可扩展 SaaS 架构的经验。精通 React、Node.js 和分布式系统。",
                "",
                "工作经历",
                "科技巨头解决方案 - 资深负责人 | 2020 - 至今",
                "• 使用 Golang 构建了每分钟处理 100 万次以上请求的微服务架构。",
                "",
                "专业技能",
                "JavaScript, TypeScript, Python, Go, Kubernetes, AWS, SQL, NoSQL."
        )));
        texts.put("hr", String.join("\n", List.of(
                "张伟 · 高级全栈工程师",
                "联系方式：felix.z@ai.com / 138-0000-0000",
                "",
                "个人总结",
                "致力于通过人工智能与前沿工程实践解决复杂商业问题。",
                "",
                "核心项目",
                "云端分布式架构重构：负责公司核心系统的微服务转型。"
        )));
        texts.put("platform", String.join("\n", List.of(
                "张伟 | 高级架构师 · 8 年经验",
                "上海 · 清华大学 · 计算机科学与技术",
                "",
                "一句话亮点",
                "擅长分布式系统与云原生架构，具备从 0 到 1 推动复杂技术方案落地的经验。"
        )));
        texts.put("email", String.join("\n", List.of(
                "主题：候选人自荐 - 张伟（高级架构师）",
                "",
                "您好，",
                "",
                "基于我近期的人才画像分析，我在分布式系统建设、微服务架构升级和跨团队技术推进方面具备明确证据。",
                "",
                "此致",
                "敬礼",
                "张伟"
        )));
        return texts;
    }

    public record MockInterviewQuestion(
            String question,
            String questionReason,
            List<String> targetCapabilities,
            String stage
    ) {
    }
}
