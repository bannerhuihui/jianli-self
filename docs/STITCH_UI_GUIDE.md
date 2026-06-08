# Stitch UI Guide

## 1. 目标

本文件说明如何使用 Stitch 生成 AI Talent Agent 的全套 UI 原型。

Stitch 适合从自然语言、图片或线框生成高保真 UI，并支持迭代调整、导出前端代码或粘贴到 Figma。根据 Google 官方介绍，Stitch 可以通过自然语言和图片输入生成 UI，并支持迭代和导出前端代码。

参考：

- https://stitch.withgoogle.com/
- https://developers.googleblog.com/en/stitch-a-new-way-to-design-uis/
- https://blog.google/innovation-and-ai/models-and-research/google-labs/stitch-ai-ui-design/

## 2. 使用策略

不要一次生成完整产品。

建议按三轮生成：

1. 候选人主流程。
2. HR 推荐流程。
3. 统一设计系统和细节状态。

这样更容易控制质量。

## 3. 准备材料

这一节是背景资料，不是必填步骤。第一轮可以直接跳到第 4 节复制“总提示词”。

如果 Stitch 生成结果太泛、不像本项目，或者遗漏了人才画像、证据链、置信度、HR 导出兼容等重点，再回到这些文档中复制相关摘要补充给 Stitch：

- `PRODUCT_BRIEF.md`
- `PRD.md`
- `USER_FLOW.md`
- `PAGE_SPEC.md`
- `UI_COMPONENTS.md`
- `UI_CONTENT.md`
- `DESIGN_PRINCIPLES.md`
- `UI_MVP_SCOPE.md`
- `HR_COMPATIBILITY.md`

如果 Stitch 支持上传或粘贴长上下文，优先粘贴这些文档的摘要，而不是全部原文。

全局要求：

- 所有界面可见文字必须使用中文。
- 页面标题、按钮、标签、卡片标题、示例数据、错误提示、空状态、导航菜单都使用中文。
- 英文可以只保留在技术缩写中，例如 ATS、CSV、JSON、PDF。
- 不要生成英文 UI 文案，否则后续评审效果会不直观。

## 4. 总提示词

第一条建议使用这个总提示词：

```text
Design a desktop-first high-fidelity UI for an AI recruiting product called AI Talent Agent.

Product positioning:
AI Talent Agent is a candidate understanding layer compatible with ATS, recruiting platforms, Excel, email, Feishu, WeCom, and other HR workflows. It does not replace HR systems. It helps users parse resumes, run AI interviews, generate talent profiles, create resume versions, and produce HR-friendly candidate recommendation packages.

Design vibe:
Professional, clean, trustworthy, calm, structured, data-informed, human-centered. Avoid playful, flashy, cyberpunk, or overly futuristic visuals.

Primary users:
1. Candidate: uploads resume, reviews parsed resume, completes AI interview, views talent profile, generates resume versions.
2. HR: enters job requirement, reviews candidate recommendations, checks match reasons, risks, interview questions, and exports results to existing workflows.

Generate a desktop web app UI with a modern B2B SaaS style. Use cards, clear step progress, confidence badges, evidence drawers, risk cards, and export actions.

Language requirement:
All visible UI copy must be in Simplified Chinese, including page titles, buttons, labels, cards, navigation, sample data, empty states, loading states, error messages, and helper text. Keep only necessary technical acronyms such as ATS, CSV, JSON, and PDF in English.

First generate the candidate flow screens:
1. Landing page
2. Resume upload page
3. Resume parsing review page
4. AI interview page
5. Talent profile page
6. Resume generation page

Important UX requirements:
- Every AI conclusion should have evidence and confidence.
- Resume parsing results must be editable.
- Complex resume templates should show a warning.
- Talent profile should include 8 dimensions: learning ability, logic ability, communication ability, execution ability, innovation ability, leadership ability, stress tolerance, career stability.
- Resume generation should include ATS version and HR version.
- Use Simplified Chinese UI copy everywhere.
```

## 5. 第二轮：HR 流程提示词

候选人流程满意后，再输入：

```text
Continue the same design system and generate the HR workflow screens.

Language requirement:
All visible UI copy must be in Simplified Chinese, including page titles, buttons, labels, cards, navigation, sample data, empty states, loading states, error messages, and helper text. Keep only necessary technical acronyms such as ATS, CSV, JSON, and PDF in English.

Screens:
1. Job requirement input page
2. Job profile confirmation page
3. Candidate recommendation list page
4. Candidate match detail page
5. HR export package page

HR workflow principles:
- HR should not be forced to adopt a new system.
- Outputs must be compatible with existing workflows: Excel/CSV, email, Feishu, WeCom, DingTalk, ATS-friendly fields.
- Candidate cards should show match score, score breakdown, key reasons, risk tags, and actions.
- Match detail page should show recommendation reasons, evidence, risks, and 3-5 interview questions.
- Export page should support copying summaries and downloading Excel/CSV.

Use Simplified Chinese UI copy everywhere. Keep the visual style professional, calm, and trustworthy.
```

## 6. 第三轮：组件和状态提示词

再输入：

```text
Refine the design system and add important UI states and reusable components.

Language requirement:
All visible UI copy must be in Simplified Chinese, including component labels, status text, empty states, loading states, error messages, and helper text. Keep only necessary technical acronyms such as ATS, CSV, JSON, and PDF in English.

Components to standardize:
- Step progress
- Upload card
- Resume parse quality card
- Editable resume section
- AI interview chat panel
- Capability score card
- Evidence drawer
- Confidence badge
- Risk card
- Match score breakdown
- Candidate recommendation card
- Export package panel

States to include:
- Empty state
- Loading state
- Error state
- Low confidence state
- Parsing failed state
- Export failed state

Keep the layout desktop-first, clean, professional, and suitable for a recruiting SaaS product. Use Simplified Chinese UI copy everywhere.
```

## 7. 单页精修提示词

如果某个页面效果不好，不要整体重做。使用局部精修提示：

```text
Refine only the Talent Profile page.

Make the information hierarchy clearer:
1. Overall candidate summary at the top
2. 8 capability score cards
3. Strengths and risks
4. Career preferences
5. Evidence drawer

Each capability card must show score, reason, confidence, and a button to view evidence.
Do not make the page look like a generic dashboard. It should feel like a professional career profile.
Use Simplified Chinese UI copy everywhere.
```

HR 详情页精修：

```text
Refine only the Candidate Match Detail page.

The page should help HR decide whether to interview this candidate.
Show:
- Candidate one-page summary
- Total match score
- Score breakdown: experience, skills, profile, career preference
- Recommendation reasons with evidence
- Risks that need verification
- 3-5 interview questions
- Export actions: copy summary, copy interview questions, export package

Make the design compatible with traditional HR workflows.
Use Simplified Chinese UI copy everywhere.
```

## 8. 生成后的检查清单

每轮生成后检查：

- 是否能看懂完整流程。
- 是否体现“候选人理解层”，而不是普通简历工具。
- 是否有证据链和置信度。
- HR 侧是否有复制、导出、摘要、面试问题。
- 页面是否过度炫技。
- 页面是否过于传统和沉重。
- 中文文案是否自然。
- 首屏是否清楚表达产品价值。

## 9. 导出建议

设计满意后：

1. 先导出或复制到 Figma 做视觉精修。
2. 再导出前端代码作为参考。
3. 不建议直接把 Stitch 导出的代码当最终工程代码。
4. 前端实现时应以 `PAGE_SPEC.md`、`UI_COMPONENTS.md` 和 `UI_CONTENT.md` 为准。

## 10. 推荐工作方式

推荐流程：

```text
文档 -> Stitch 生成候选人流程 -> 人工检查 -> Stitch 生成 HR 流程 -> 人工检查 -> Figma 精修 -> 前端实现
```

不要跳过人工检查。AI 生成的 UI 可能视觉不错，但流程和产品逻辑仍需要对照文档校验。

