# AI Talent Agent 文档索引

本目录用于沉淀 AI Talent Agent 的产品、Agent、画像、访谈、匹配和 MVP 规划文档。

## 推荐阅读顺序

1. `DOC_AUDIT.md`
   - 理解原始设计文档已经明确了什么、还缺什么。

2. `PRODUCT_BRIEF.md`
   - 理解产品定位、目标用户、核心场景和第一阶段边界。

3. `PRD.md`
   - 理解 MVP 需求、用户故事、功能范围和验收标准。

4. `TALENT_PROFILE.md`
   - 理解人才画像的维度、评分、证据和置信度机制。

5. `INTERVIEW_DESIGN.md`
   - 理解 AI 访谈如何围绕画像维度收集信息。

6. `AGENT_DESIGN.md`
   - 理解各 Agent 的职责、输入输出和协作协议。

7. `MATCHING_DESIGN.md`
   - 理解 HR 岗位需求如何拆解，以及候选人如何匹配推荐。

8. `HR_COMPATIBILITY.md`
   - 理解如何兼容传统 HR 工具、ATS、招聘平台和协作流程。

9. `MVP_ROADMAP.md`
   - 理解后续研发顺序、里程碑和暂不做范围。

10. `USER_FLOW.md`
   - 理解候选人侧、HR 侧和内部调试侧的完整流程。

11. `PAGE_SPEC.md`
   - 理解每个页面的目标、模块、按钮、状态和错误处理。

12. `UI_COMPONENTS.md`
   - 理解 UI 组件体系和复用方式。

13. `UI_CONTENT.md`
   - 理解产品关键文案、提示文案和错误文案。

14. `DESIGN_PRINCIPLES.md`
   - 理解视觉风格、信息层级和可信设计原则。

15. `UI_MVP_SCOPE.md`
   - 理解第一轮 UI 原型应该生成哪些页面，哪些暂不做。

16. `STITCH_UI_GUIDE.md`
   - 理解如何使用 Stitch 分批生成全套 UI。

17. `API_CONTRACT.md`
   - 理解第一版后端 HTTP API、鉴权扩展与求职者流程接口。

18. `FRONTEND_ARCHITECTURE.md`
   - 理解前端主线、共享层与 Java 后端边界。

## 文档职责边界

| 文档 | 职责 |
| --- | --- |
| `DOC_AUDIT.md` | 审计原始 `.docx`，标记已明确内容和缺失内容 |
| `PRODUCT_BRIEF.md` | 定义产品为什么存在、服务谁、第一版解决什么问题 |
| `PRD.md` | 定义 MVP 功能需求、用户故事、验收标准和不做范围 |
| `TALENT_PROFILE.md` | 定义人才画像体系，是项目的核心资产文档 |
| `INTERVIEW_DESIGN.md` | 定义 Interview Agent 如何提问、追问、抽取事实 |
| `AGENT_DESIGN.md` | 定义所有 Agent 的输入输出、协作链路和约束 |
| `MATCHING_DESIGN.md` | 定义岗位画像、匹配算法、推荐解释和风险提示 |
| `HR_COMPATIBILITY.md` | 定义如何兼容传统 HR 工具、招聘渠道、ATS 和协作流程 |
| `MVP_ROADMAP.md` | 定义阶段计划、优先级、里程碑和研发前置清单 |
| `USER_FLOW.md` | 定义候选人侧、HR 侧和内部调试侧用户路径 |
| `PAGE_SPEC.md` | 定义每个页面的目标、模块、按钮和状态 |
| `UI_COMPONENTS.md` | 定义 UI 组件库和复用规则 |
| `UI_CONTENT.md` | 定义界面文案、提示文案和错误文案 |
| `DESIGN_PRINCIPLES.md` | 定义 UI 气质、视觉方向和信息层级 |
| `UI_MVP_SCOPE.md` | 定义第一轮 UI 原型范围 |
| `STITCH_UI_GUIDE.md` | 定义使用 Stitch 生成 UI 的流程和提示词 |
| `API_CONTRACT.md` | 定义 REST API、鉴权、异步任务与 `packages/domain` 对齐 |
| `FRONTEND_ARCHITECTURE.md` | 定义 uni-app 主线、身份策略与后端预留 |

## 维护原则

- 产品边界变化时，先更新 `PRODUCT_BRIEF.md` 和 `PRD.md`。
- 画像维度变化时，先更新 `TALENT_PROFILE.md`，再同步影响到访谈、Agent 和匹配文档。
- Agent 输入输出变化时，先更新 `AGENT_DESIGN.md`，再进入开发。
- 匹配权重或解释逻辑变化时，先更新 `MATCHING_DESIGN.md`。
- HR 输出形态、导出格式或第三方工具兼容策略变化时，先更新 `HR_COMPATIBILITY.md`。
- MVP 范围变化时，最后同步到 `MVP_ROADMAP.md`。
- 页面、组件或交互变化时，更新 `USER_FLOW.md`、`PAGE_SPEC.md` 和 `UI_COMPONENTS.md`。
- UI 文案变化时，更新 `UI_CONTENT.md`。
- 使用 Stitch 生成 UI 前，先检查 `UI_MVP_SCOPE.md` 和 `STITCH_UI_GUIDE.md`。
- 后端接口或鉴权变化时，先更新 `API_CONTRACT.md`，再同步 `packages/domain` 与 `packages/api`。

