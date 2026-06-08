# AI Talent Agent

AI Talent Agent 是一个以人才画像为核心的 AI 招聘基础设施项目。

项目使命：

> 让 AI 比 HR 更了解候选人。

项目愿景：

> 构建可持续积累的人才画像数据库，让候选人的真实能力与企业的真实岗位需求高质量连接。

## 核心定位

AI Talent Agent 第一阶段不是 ATS、招聘 CRM，也不是单纯的简历润色工具。它的核心是：

> 通过简历解析和 AI 深度访谈，生成可信的人才画像，并基于人才画像完成简历生成和岗位匹配。

同时，它不应强迫 HR 放弃现有工具。更现实的产品定位是：

> 兼容 ATS、招聘平台和办公协作工具的候选人理解层。

## 核心闭环

候选人侧：

```text
上传简历 -> Resume Agent -> Interview Agent -> Profile Agent -> Talent Database -> Resume Builder Agent
```

企业侧：

```text
HR 岗位需求 -> Match Agent -> Talent Database -> 候选人推荐
```

## MVP 建议

第一阶段建议优先验证候选人侧画像闭环：

1. 简历上传与解析。
2. AI 访谈补全信息。
3. 生成人才画像。
4. 基于画像生成简历版本。

HR 匹配侧建议作为第二阶段接入，因为匹配质量依赖人才画像质量。

## 文档入口

- `docs/README.md`：文档索引和阅读顺序。
- `docs/DOC_AUDIT.md`：原始 `.docx` 的内容审计和缺口分析。
- `docs/PRODUCT_BRIEF.md`：产品定义、用户、场景和边界。
- `docs/PRD.md`：MVP 产品需求、用户故事和验收标准。
- `docs/TALENT_PROFILE.md`：人才画像维度、评分、证据和置信度。
- `docs/INTERVIEW_DESIGN.md`：AI 访谈阶段、追问策略和停止条件。
- `docs/AGENT_DESIGN.md`：Agent 职责、输入输出和协作协议。
- `docs/MATCHING_DESIGN.md`：岗位画像、匹配算法和推荐解释。
- `docs/HR_COMPATIBILITY.md`：兼容传统 HR 工具、ATS、招聘平台和协作流程。
- `docs/MVP_ROADMAP.md`：MVP 路线、阶段拆解和研发前置清单。
- `docs/USER_FLOW.md`：候选人侧、HR 侧和内部调试侧的完整用户流程。
- `docs/PAGE_SPEC.md`：每个页面的目标、模块、按钮和状态。
- `docs/UI_COMPONENTS.md`：上传、访谈、画像、匹配和导出相关组件。
- `docs/UI_CONTENT.md`：关键产品文案、提示、按钮和错误文案。
- `docs/DESIGN_PRINCIPLES.md`：视觉风格、信息层级和设计原则。
- `docs/UI_MVP_SCOPE.md`：第一轮 UI 原型生成范围和验收标准。
- `docs/STITCH_UI_GUIDE.md`：如何使用 Stitch 生成全套 UI。

## 当前阶段

当前阶段的重点是完善文档，不急于开发。

在进入工程实现前，需要先确认：

- 人才画像维度是否稳定。
- AI 访谈是否能有效收集证据。
- Agent 数据契约是否清晰。
- 匹配解释是否能让 HR 理解和信任。
- 输出材料是否能兼容 HR 已有工作流。
- UI 页面流程和组件规格是否足够清晰。
- MVP 范围是否足够小且可验证。

