# Agent Design

## 1. 总体原则

AI Talent Agent 的 Agent 系统应优先保证：

- 输入输出结构稳定。
- 结论可解释。
- 证据可追溯。
- 不编造候选人信息。
- 失败时可降级和重试。

MVP 阶段不建议过早引入复杂自主 Agent 框架。可以先用服务编排方式实现：

```text
Resume Agent -> Interview Agent -> Profile Agent -> Resume Builder Agent
                                  -> Match Agent
```

## 2. Agent 协作流程

候选人侧：

```text
Resume File
  -> Resume Agent
  -> Structured Resume
  -> Interview Agent
  -> Interview Facts
  -> Profile Agent
  -> Talent Profile
  -> Resume Builder Agent
  -> Resume Versions
```

HR 侧：

```text
HR Requirement
  -> Match Agent
  -> Job Profile
  -> Talent Profiles
  -> Match Results
```

## 3. 通用输出约束

所有 Agent 必须遵守：

- 输出 JSON 必须可解析。
- 不确定内容必须标记 `confidence`。
- 结论必须尽可能引用 `evidenceIds`。
- 不得编造不存在的事实。
- 缺失信息必须放入 `missingFields` 或 `risks`。
- 错误不能静默吞掉，必须返回可诊断信息。

通用字段：

```json
{
  "agentName": "string",
  "version": "string",
  "confidence": "high",
  "warnings": [],
  "missingFields": []
}
```

## 4. Resume Agent

### 4.1 职责

Resume Agent 负责把简历原始文本转成结构化简历数据。

输入来源：

- PDF。
- Word。
- 后续可扩展图片 OCR。

### 4.2 输入

```json
{
  "candidateId": "candidate_001",
  "resumeText": "简历原始文本",
  "fileMetadata": {
    "fileName": "resume.pdf",
    "fileType": "pdf"
  }
}
```

### 4.3 输出

```json
{
  "agentName": "ResumeAgent",
  "version": "v1",
  "basicInfo": {
    "name": "",
    "email": "",
    "phone": "",
    "location": ""
  },
  "education": [],
  "workExperience": [],
  "projects": [],
  "skills": [],
  "certifications": [],
  "awards": [],
  "rawEvidence": [],
  "missingFields": [],
  "warnings": [],
  "confidence": "medium"
}
```

### 4.4 约束

- 不得根据常识补全学校、公司、时间和成果。
- 原文缺失的信息必须留空或标记缺失。
- 需要保存原始文本引用，方便用户校对。
- 对无法解析的段落放入 `warnings`。

### 4.5 失败场景

- 简历文本为空。
- PDF 解析乱码。
- 简历格式过于复杂。
- 语言混杂导致字段提取不稳定。

失败处理：

- 返回原始文本质量问题。
- 提示用户上传更清晰版本。
- 允许用户手动录入关键字段。

## 5. Interview Agent

### 5.1 职责

Interview Agent 基于结构化简历和已知画像缺口，生成上下文相关问题，并从回答中抽取事实。

### 5.2 输入

```json
{
  "candidateId": "candidate_001",
  "structuredResume": {},
  "previousTurns": [],
  "missingEvidence": [],
  "currentStage": "experience_exploration"
}
```

### 5.3 输出

```json
{
  "agentName": "InterviewAgent",
  "version": "v1",
  "stage": "deep_dive",
  "question": "你在 XX 项目中具体负责哪一部分？",
  "questionReason": "需要补充执行能力和逻辑能力证据",
  "targetCapabilities": ["executionAbility", "logicAbility"],
  "extractedFacts": [],
  "missingEvidence": [],
  "shouldStop": false,
  "confidence": "medium"
}
```

### 5.4 约束

- 问题必须基于简历或历史回答。
- 一轮只问一个主要问题。
- 不追问敏感隐私。
- 不诱导候选人夸大经历。
- 抽取事实时不得加入模型推测。

## 6. Profile Agent

### 6.1 职责

Profile Agent 汇总结构化简历和访谈事实，生成人才画像。

### 6.2 输入

```json
{
  "candidateId": "candidate_001",
  "structuredResume": {},
  "interviewFacts": [],
  "profileSchemaVersion": "v1"
}
```

### 6.3 输出

```json
{
  "agentName": "ProfileAgent",
  "version": "v1",
  "summary": "",
  "capabilityScores": {
    "learningAbility": {
      "score": 3,
      "reason": "",
      "evidenceIds": [],
      "confidence": "medium"
    },
    "logicAbility": {},
    "communicationAbility": {},
    "executionAbility": {},
    "innovationAbility": {},
    "leadershipAbility": {},
    "stressTolerance": {},
    "careerStability": {}
  },
  "strengths": [],
  "risks": [],
  "careerPreferences": {},
  "recommendedRoles": [],
  "evidence": [],
  "confidence": "medium"
}
```

### 6.4 约束

- 每个能力评分必须有理由。
- 高分必须有强证据。
- 低置信度必须显式标记。
- 不得使用人格化、歧视性或不可验证标签。
- 不得用学校、年龄、性别做不当推断。

## 7. Resume Builder Agent

### 7.1 职责

Resume Builder Agent 根据结构化简历和人才画像生成不同用途的简历版本。

MVP 版本：

- ATS 版。
- HR 版。

后续版本：

- Boss 直聘版。
- 猎聘版。
- 岗位定制版。

### 7.2 输入

```json
{
  "candidateId": "candidate_001",
  "structuredResume": {},
  "talentProfile": {},
  "targetVersion": "ats",
  "targetJob": null
}
```

### 7.3 输出

```json
{
  "agentName": "ResumeBuilderAgent",
  "version": "v1",
  "resumeVersion": "ats",
  "title": "",
  "summary": "",
  "sections": [],
  "usedEvidenceIds": [],
  "warnings": [],
  "confidence": "medium"
}
```

### 7.4 约束

- 不得编造经历、公司、学历、成果和数据。
- 可以优化表达，但必须保持事实一致。
- 对缺少量化结果的经历，不得自行添加数字。
- HR 版可以强化亮点，ATS 版要保持结构清晰。

## 8. Match Agent

### 8.1 职责

Match Agent 负责拆解 HR 岗位需求，生成岗位画像，并基于人才画像推荐候选人。

### 8.2 输入

```json
{
  "jobRequirement": "自然语言岗位需求",
  "candidateProfiles": [],
  "matchingWeights": {
    "experience": 0.3,
    "skills": 0.3,
    "profile": 0.25,
    "careerPreference": 0.15
  }
}
```

### 8.3 输出

```json
{
  "agentName": "MatchAgent",
  "version": "v1",
  "jobProfile": {
    "title": "",
    "responsibilities": [],
    "requiredSkills": [],
    "preferredSkills": [],
    "requiredCapabilities": [],
    "careerPreferenceSignals": [],
    "missingFields": []
  },
  "recommendations": [
    {
      "candidateId": "",
      "matchScore": 0,
      "scoreBreakdown": {
        "experience": 0,
        "skills": 0,
        "profile": 0,
        "careerPreference": 0
      },
      "matchReasons": [],
      "risks": [],
      "interviewQuestions": [],
      "evidenceIds": []
    }
  ],
  "confidence": "medium"
}
```

### 8.4 约束

- 推荐必须解释原因。
- 风险点必须明确来源。
- 不确定项必须生成面试问题。
- 不得因为单一关键词完全否定候选人。
- 不得输出歧视性筛选理由。

## 9. Prompt 设计要求

每个 Agent 的 Prompt 必须包含：

- 角色定义。
- 输入说明。
- 输出 JSON schema。
- 禁止编造规则。
- 证据引用规则。
- 置信度规则。
- 失败处理规则。

推荐把 Prompt 作为版本化资产管理：

```text
prompts/
  resume_agent_v1.md
  interview_agent_v1.md
  profile_agent_v1.md
  resume_builder_agent_v1.md
  match_agent_v1.md
```

## 10. 日志与审计

每次 Agent 运行建议记录：

- agentName。
- promptVersion。
- inputHash。
- output JSON。
- warnings。
- latency。
- model。
- createdAt。

这些日志用于：

- 调试 Prompt。
- 复盘画像质量。
- 分析匹配错误。
- 后续构建评测集。

