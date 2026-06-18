# 火山方舟本地联调配置指南

本文说明接入真实大模型时，你需要从火山引擎控制台准备哪些信息。

## 你需要准备的 5 项

| # | 名称 | 去哪找 | 示例格式 |
|---|------|--------|----------|
| 1 | **API Key** | 火山方舟控制台 → API Key 管理 → 创建 | `xxxxxxxx-xxxx-xxxx` |
| 2 | **Resume Endpoint** | 在线推理 → Doubao-Seed-2.0-**pro** → 推理接入点 ID | `ep-202406xxxxxx-xxxxx` |
| 3 | **Interview Endpoint** | 同上，建议 **2.0-lite** 或 pro | `ep-202406xxxxxx-xxxxx` |
| 4 | **Profile Endpoint** | 同上，建议 **2.0-pro** | `ep-202406xxxxxx-xxxxx` |
| 5 | **Resume Builder Endpoint** | 同上，建议 **2.0-lite** 或 pro | `ep-202406xxxxxx-xxxxx` |

> **注意**：填的是「推理接入点 ID」（`ep-` 开头），不是模型名称 `Doubao-Seed-2.0-pro`。

### 最简方案（省事先跑通）

四个 Agent 可以 **共用一个 pro 接入点**，在控制台为 `Doubao-Seed-2.0-pro` 创建一个接入点，四个 endpoint 填同一个 `ep-xxx` 即可。

推荐拆分方案：

| Agent | 模型 |
|-------|------|
| Resume | Doubao-Seed-2.0-pro |
| Interview | Doubao-Seed-2.0-lite |
| Profile | Doubao-Seed-2.0-pro |
| Resume Builder | Doubao-Seed-2.0-lite |

## 配置方式（二选一）

### 方式 A：本地配置文件（推荐）

```bash
cd services/java-api
cp application-local.yml.example application-local.yml
# 编辑 application-local.yml 填入 api-key 和 endpoints
```

`application-local.yml` 已在 `.gitignore` 中，不会提交到 Git。

### 方式 B：环境变量

```bash
export VOLCENGINE_API_KEY="你的API Key"
export VOLCENGINE_ENDPOINT_RESUME="ep-xxx"
export VOLCENGINE_ENDPOINT_INTERVIEW="ep-xxx"
export VOLCENGINE_ENDPOINT_PROFILE="ep-xxx"
export VOLCENGINE_ENDPOINT_RESUME_BUILDER="ep-xxx"
```

然后启动时设置：

```bash
# application-local.yml 中 agents.mode: llm
# 或临时：
SPRING_APPLICATION_JSON='{"app":{"agents":{"mode":"llm"}}}' mvn spring-boot:run
```

## 启动与验证

```bash
cd services/java-api
mvn spring-boot:run
```

### 1. 健康检查

```bash
curl http://localhost:8080/actuator/health
```

### 2. 登录

```bash
curl -s -X POST http://localhost:8080/api/v1/auth/session \
  -H 'Content-Type: application/json' \
  -d '{"authProvider":"anonymous","deviceId":"volc-test-1","platform":"h5"}'
```

### 3. 完整 LLM 流程

用返回的 `accessToken`：

1. `POST /api/v1/journeys` 创建旅程
2. `POST /api/v1/journeys/{id}/resume-files` 上传 **可选中文字的 PDF 或 docx**
3. `POST /api/v1/journeys/{id}/parse-resume` → 轮询 `GET /api/v1/tasks/{taskId}`
4. 后续按 `docs/API_CONTRACT.md` 继续

## 模式切换

| `app.agents.mode` | 行为 |
|-------------------|------|
| `mock`（默认） | 固定 Mock 数据，无需 API Key |
| `llm` | 调用火山方舟，需配置 API Key 和 Endpoint |

## 常见问题

**Q: 解析失败「未能从文件中提取文本」**  
A: 上传扫描版 PDF。第一版请用可选中文字的 PDF/docx。

**Q: HTTP 503 LLM_CONFIG_MISSING**  
A: 检查 API Key 和对应 Agent 的 Endpoint 是否已填。

**Q: HTTP 503 LLM_REQUEST_FAILED**  
A: 检查 API Key 权限、接入点是否已开通、账户余额/体验额度。

**Q: JSON 解析失败**  
A: 查看日志中的模型原始输出；可尝试换 pro 模型或调低 temperature。

## 相关代码

| 路径 | 说明 |
|------|------|
| `llm/VolcengineLlmClient.java` | 火山方舟 HTTP 调用 |
| `agent/AgentOrchestrator.java` | Mock / LLM 切换与四个 Agent |
| `parser/ResumeTextExtractor.java` | PDF/docx 本地抽文本 |
| `resources/prompts/*.md` | Agent Prompt 模板 |
