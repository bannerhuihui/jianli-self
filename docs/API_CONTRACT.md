# API Contract

## 1. 文档目的

本文件定义 AI Talent Agent **第一版后端 HTTP API 契约**，供 `services/java-api`（Spring Boot）实现，并与 `packages/domain`、`packages/api` 对齐。

**第一版范围：**

- 仅 **求职者（Candidate）闭环**：上传简历 → 校对 → AI 访谈 → 人才画像 → 简历生成与导出。
- **暂不做 HR 侧接口**（岗位、匹配、导出包等保留扩展位，见第 12 节）。
- **暂不对外开放**：内测 / 邀请制，网络层或网关层限制访问。
- **求职者零付费**：完整流程默认可用，鉴权层预留权益字段，第一版不启用计费校验。

**相关文档：**

| 文档 | 关系 |
| --- | --- |
| `AGENT_DESIGN.md` | Agent 内部输入输出（不直接暴露给前端） |
| `packages/domain` | 对外 DTO 字段的 TypeScript 单一事实来源 |
| `USER_FLOW.md` / `PAGE_SPEC.md` | 页面动作与 API 映射 |
| `FRONTEND_ARCHITECTURE.md` | 身份策略与前后端边界 |

---

## 2. 技术约定

### 2.1 基础信息

| 项 | 约定 |
| --- | --- |
| 实现 | Java 21（或 17）+ Spring Boot 3 |
| Base Path | `/api/v1` |
| 协议 | HTTPS only（生产环境） |
| 数据格式 | `application/json`；文件上传用 `multipart/form-data` |
| 字符编码 | UTF-8 |
| 时间 | ISO-8601，UTC 存储，响应带 `Z` 后缀 |
| ID 格式 | 字符串，建议 `prefix_uuid`，如 `journey_a1b2c3` |

### 2.2 统一响应包装

成功（有 body）：

```json
{
  "data": {},
  "requestId": "req_01h2x3y4z5",
  "timestamp": "2026-06-09T08:00:00Z"
}
```

成功（无 body，如 DELETE）：

```http
HTTP/1.1 204 No Content
X-Request-Id: req_01h2x3y4z5
```

失败：

```json
{
  "error": {
    "code": "RESUME_PARSE_FAILED",
    "message": "未能从文件中稳定提取结构化信息",
    "details": {
      "warnings": ["扫描件识别率低"],
      "missingFields": ["workExperience"]
    }
  },
  "requestId": "req_01h2x3y4z5",
  "timestamp": "2026-06-09T08:00:00Z"
}
```

### 2.3 HTTP 状态码

| 状态码 | 用途 |
| --- | --- |
| 200 | 查询、更新成功 |
| 201 | 创建成功 |
| 202 | 已接受异步任务 |
| 204 | 删除成功 |
| 400 | 请求参数错误 |
| 401 | 未登录 / Token 无效 |
| 403 | 无权限（含内测未授权、旅程不属于当前用户） |
| 404 | 资源不存在 |
| 409 | 状态冲突（如在解析未完成时发起访谈） |
| 413 | 文件过大 |
| 415 | 文件类型不支持 |
| 422 | 业务校验失败（如无画像就生成简历） |
| 429 | 限流 |
| 500 | 服务端错误 |
| 503 | 依赖不可用（LLM、存储等） |

### 2.4 错误码命名

格式：`DOMAIN_ACTION_REASON`，全大写蛇形。

求职者侧常用：

| code | 说明 |
| --- | --- |
| `AUTH_TOKEN_INVALID` | Token 无效或过期 |
| `AUTH_ACCESS_DENIED` | 内测访问被拒绝 |
| `JOURNEY_NOT_FOUND` | 旅程不存在 |
| `JOURNEY_STATE_INVALID` | 当前步骤不允许该操作 |
| `RESUME_FILE_TOO_LARGE` | 文件超过 20MB |
| `RESUME_FILE_TYPE_UNSUPPORTED` | 非 PDF / Word / Docx |
| `RESUME_PARSE_FAILED` | 解析失败 |
| `INTERVIEW_NOT_STARTED` | 访谈未开始 |
| `PROFILE_NOT_READY` | 画像未生成 |
| `RESUME_VERSION_NOT_FOUND` | 简历版本不存在 |
| `TASK_NOT_FOUND` | 异步任务不存在 |
| `TASK_FAILED` | 异步任务失败 |
| `RATE_LIMITED` | 请求过于频繁 |

---

## 3. 鉴权与身份（可扩展设计）

第一版 **不要求求职者注册账号**，但必须设计可扩展的身份层，便于后续接入微信公众号、小程序、企业微信 HR。

### 3.1 设计原则

1. **无感体验**：首次进入即可创建匿名会话，走完五步流程。
2. **零付费**：候选人默认拥有 `candidate.full_flow` 权益，接口层不做付费校验。
3. **可绑定**：匿名会话后续可绑定微信 `openid` / `unionid`，旅程数据不丢失。
4. **角色分离**：第一版仅启用 `CANDIDATE`；`HR`、`ADMIN` 角色与接口前缀预留，默认 403。
5. **内测门禁**：与身份鉴权分离，通过网关或独立校验实现。

### 3.2 请求头

| Header | 必填 | 说明 |
| --- | --- | --- |
| `Authorization` | 是（除 `/auth/*` 外） | `Bearer <accessToken>` |
| `X-Client-Platform` | 建议 | `h5` \| `mp-weixin` \| `internal` |
| `X-Request-Id` | 否 | 客户端生成，便于链路追踪；未传则由服务端生成 |
| `X-Invite-Code` | 内测期建议 | 邀请码，配合「暂不对外开放」 |

### 3.3 Access Token 结构（JWT 示例）

第一版可用 **opaque token + Redis**，或直接 **JWT**。Claims 建议固定以下字段，便于扩展：

```json
{
  "sub": "user_01h2x3y4z5",
  "sid": "session_01h2x3y4z5",
  "role": "CANDIDATE",
  "authProvider": "anonymous",
  "entitlements": ["candidate.full_flow"],
  "bindings": {
    "wechatMpOpenId": null,
    "wechatOaOpenId": null,
    "unionId": null
  },
  "iat": 1717912800,
  "exp": 1718517600
}
```

| 字段 | 说明 |
| --- | --- |
| `sub` | 用户 ID，匿名与微信绑定后保持不变 |
| `sid` | 会话 ID，用于踢下线、续期 |
| `role` | `CANDIDATE` \| `HR` \| `ADMIN`（v1 仅 `CANDIDATE`） |
| `authProvider` | `anonymous` \| `wechat_mp` \| `wechat_oa` \| `internal` |
| `entitlements` | 权益列表；v1 候选人固定 `candidate.full_flow` |
| `bindings` | 第三方身份绑定，未绑定时为 `null` |

Token 有效期建议：**7 天**，支持 refresh。

### 3.4 内测访问控制（暂不对外开放）

与业务鉴权 **两层独立**：

```text
请求
  -> 网关 / WAF（IP 白名单、VPN、域名不公开）
  -> Invite Code 校验（可选，X-Invite-Code）
  -> Bearer Token 校验
  -> 业务接口
```

第一版实现优先级：

1. 部署环境不公开域名 + IP 白名单。
2. `POST /auth/session` 校验 `inviteCode`（配置项开关）。
3. 微信 OAuth 可后置。

### 3.5 鉴权相关接口

#### `POST /auth/session`

创建或恢复会话。求职者 **无需注册**，匿名即可。

**Request**

```json
{
  "authProvider": "anonymous",
  "inviteCode": "beta-2026",
  "deviceId": "client-generated-uuid",
  "platform": "h5"
}
```

`authProvider` 枚举：

| 值 | v1 状态 | 说明 |
| --- | --- | --- |
| `anonymous` | ✅ 实现 | 设备 ID 生成匿名用户 |
| `wechat_mp` | 🔜 预留 | 小程序 `wx.login` code 换 session |
| `wechat_oa` | 🔜 预留 | 公众号网页授权 code |
| `internal` | 🔜 预留 | 内部调试账号 |

**Response `201`**

```json
{
  "data": {
    "accessToken": "eyJ...",
    "refreshToken": "rt_...",
    "expiresIn": 604800,
    "user": {
      "id": "user_01h2x3y4z5",
      "role": "CANDIDATE",
      "authProvider": "anonymous",
      "entitlements": ["candidate.full_flow"],
      "activeJourneyId": null
    }
  }
}
```

#### `POST /auth/refresh`

用 `refreshToken` 续期。

#### `GET /auth/me`

返回当前用户与会话信息；若有进行中的旅程，带 `activeJourneyId`。

#### `POST /auth/bind/wechat`（预留）

将当前匿名用户绑定微信。Body 含 `code` + `platform`，响应更新 `bindings`。

---

## 4. 核心资源：Candidate Journey

一次完整的求职者体验对应一条 **Journey**（旅程），串联五步流程状态。

### 4.1 Journey 状态机

```text
created
  -> resume_uploaded
  -> resume_parsing
  -> resume_review       （校对）
  -> interview_active    （访谈中）
  -> interview_completed
  -> profile_generating
  -> profile_ready       （画像就绪）
  -> resume_generating
  -> completed           （至少一个简历版本已生成）
```

非法跳转返回 `409 JOURNEY_STATE_INVALID`。

### 4.2 Journey 对象

```json
{
  "id": "journey_01h2x3y4z5",
  "userId": "user_01h2x3y4z5",
  "status": "resume_review",
  "currentStep": "review",
  "steps": {
    "upload": { "completed": true, "completedAt": "2026-06-09T08:01:00Z" },
    "review": { "completed": false },
    "interview": { "completed": false },
    "profile": { "completed": false },
    "resume": { "completed": false }
  },
  "resumeFileId": "file_01h2x3y4z5",
  "structuredResumeId": "sr_01h2x3y4z5",
  "interviewSessionId": "iv_01h2x3y4z5",
  "talentProfileId": "tp_01h2x3y4z5",
  "createdAt": "2026-06-09T08:00:00Z",
  "updatedAt": "2026-06-09T08:05:00Z"
}
```

`currentStep` 枚举：`upload` | `review` | `interview` | `profile` | `resume`

与前端 `CANDIDATE_FLOW` 一一对应。

### 4.3 Journey 接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `POST` | `/journeys` | 创建新旅程（若无进行中旅程） |
| `GET` | `/journeys/active` | 获取当前用户进行中的旅程 |
| `GET` | `/journeys/{journeyId}` | 旅程详情与步骤进度 |
| `POST` | `/journeys/{journeyId}/restart` | 重新上传简历时重置后续步骤（可选） |

**权限**：`journey.userId` 必须等于 Token `sub`，否则 `403`。

---

## 5. 异步任务模型

简历解析、画像生成、简历版本生成均为 **长耗时 Agent 任务**，统一走 Task API。

### 5.1 Task 对象

```json
{
  "id": "task_01h2x3y4z5",
  "journeyId": "journey_01h2x3y4z5",
  "type": "PARSE_RESUME",
  "status": "running",
  "progress": 45,
  "result": null,
  "error": null,
  "createdAt": "2026-06-09T08:02:00Z",
  "updatedAt": "2026-06-09T08:02:05Z",
  "completedAt": null
}
```

`type` 枚举：

| type | 触发接口 | 成功后产物 |
| --- | --- | --- |
| `PARSE_RESUME` | `POST .../parse-resume` | `StructuredResume` |
| `GENERATE_PROFILE` | `POST .../profile/generate` | `TalentProfile` |
| `GENERATE_RESUME_VERSION` | `POST .../resume-versions` | `ResumeVersion` |
| `EXPORT_RESUME` | `POST .../resume-versions/{key}/export` | 文件下载 URL |

`status` 枚举：`pending` | `running` | `succeeded` | `failed` | `cancelled`

### 5.2 Task 接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/tasks/{taskId}` | 轮询任务状态；`succeeded` 时 `result` 有值 |
| `POST` | `/tasks/{taskId}/cancel` | 取消进行中的任务（可选） |

**轮询建议**：间隔 1–2s，超时 120s 提示用户重试。

`failed` 时 `error` 结构与统一错误体一致，并保留 Agent `warnings` / `missingFields`。

---

## 6. 步骤一：上传简历

对应页面：`/pages/candidate/upload/index`

### 6.1 上传文件

#### `POST /journeys/{journeyId}/resume-files`

`multipart/form-data`

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `file` | File | PDF、Word、Docx；最大 20MB |

**Response `201`**

```json
{
  "data": {
    "fileId": "file_01h2x3y4z5",
    "fileName": "resume.pdf",
    "fileType": "pdf",
    "fileSize": 1048576,
    "uploadedAt": "2026-06-09T08:01:00Z"
  }
}
```

### 6.2 触发解析

#### `POST /journeys/{journeyId}/parse-resume`

**Request**（可选）

```json
{
  "fileId": "file_01h2x3y4z5"
}
```

省略 `fileId` 时使用该旅程最新上传文件。

**Response `202`**

```json
{
  "data": {
    "taskId": "task_01h2x3y4z5",
    "status": "pending"
  }
}
```

解析成功后，`GET /tasks/{taskId}` 的 `result`：

```json
{
  "structuredResumeId": "sr_01h2x3y4z5",
  "parseQualityScore": 0.82,
  "confidence": "medium",
  "warnings": ["复杂表格可能影响工作经历提取"],
  "missingFields": ["certifications"]
}
```

**失败场景**（`PAGE_SPEC` 对齐）：格式不支持 `415`、文件过大 `413`、文本无法提取 `422 RESUME_PARSE_FAILED`。

### 6.3 手动录入（解析失败降级）

#### `POST /journeys/{journeyId}/structured-resume/manual`

跳过文件解析，创建空结构化简历，旅程进入 `resume_review`。用于前端 `?source=manual` 路径。

---

## 7. 步骤二：简历校对

对应页面：`/pages/candidate/review/index`

DTO 对齐 `packages/domain` 的 `StructuredResume`：

```typescript
interface StructuredResume {
  basicInfo: Candidate;
  education: string[];
  workExperience: string[];
  projects: string[];
  skills: string[];
}
```

### 7.1 查询

#### `GET /journeys/{journeyId}/structured-resume`

**Response `200`**

```json
{
  "data": {
    "id": "sr_01h2x3y4z5",
    "journeyId": "journey_01h2x3y4z5",
    "basicInfo": {
      "id": "candidate_01h2x3y4z5",
      "name": "张伟",
      "title": "高级软件工程师",
      "location": "上海",
      "experienceYears": 8,
      "education": "清华大学 · 计算机科学与技术",
      "phone": "138****0000",
      "email": "zhang***@example.com"
    },
    "education": ["清华大学 · 计算机科学与技术 · 学士 · 2016-2020"],
    "workExperience": ["阿里巴巴 · 高级软件工程师 · ..."],
    "projects": ["AI 智能人才评估系统"],
    "skills": ["TypeScript", "Node.js"],
    "parseQualityScore": 0.82,
    "confidence": "medium",
    "warnings": [],
    "missingFields": [],
    "updatedAt": "2026-06-09T08:10:00Z"
  }
}
```

### 7.2 保存校对

#### `PATCH /journeys/{journeyId}/structured-resume`

支持部分更新（JSON Merge Patch 语义）。

**Response `200`**：返回完整 `StructuredResume`。

### 7.3 确认并进入访谈

#### `POST /journeys/{journeyId}/structured-resume/confirm`

校验必填字段后，旅程状态 → `interview_active`，并初始化访谈会话。

**Response `200`**

```json
{
  "data": {
    "interviewSessionId": "iv_01h2x3y4z5",
    "stage": "experience_exploration",
    "journeyStatus": "interview_active"
  }
}
```

---

## 8. 步骤三：AI 访谈

对应页面：`/pages/candidate/interview/index`

访谈轮次对齐 `packages/domain` 的 `InterviewTurn`，并扩展 Agent 字段（见 `AGENT_DESIGN.md` §5）。

### 8.1 访谈会话对象

```json
{
  "id": "iv_01h2x3y4z5",
  "journeyId": "journey_01h2x3y4z5",
  "stage": "deep_dive",
  "status": "active",
  "turns": [
    {
      "id": "turn_001",
      "role": "agent",
      "content": "你在 XX 项目中具体负责哪一部分？",
      "questionReason": "需要补充执行能力证据",
      "targetCapabilities": ["executionAbility", "logicAbility"],
      "createdAt": "2026-06-09T08:15:00Z"
    }
  ],
  "missingEvidence": ["communicationAbility"],
  "canGenerateProfile": false,
  "progress": 0.4
}
```

### 8.2 接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/journeys/{journeyId}/interview` | 获取访谈会话与历史 |
| `POST` | `/journeys/{journeyId}/interview/turns` | 用户回答；Agent 返回下一问 |
| `POST` | `/journeys/{journeyId}/interview/skip` | 跳过当前问题 |
| `POST` | `/journeys/{journeyId}/interview/complete` | 结束访谈，进入画像生成 |

#### `POST .../interview/turns`

**Request**

```json
{
  "content": "我负责架构设计和核心模块开发，团队 5 人。"
}
```

**Response `200`**

```json
{
  "data": {
    "userTurn": { "id": "turn_002", "role": "user", "content": "..." },
    "agentTurn": {
      "id": "turn_003",
      "role": "agent",
      "content": "...",
      "questionReason": "...",
      "targetCapabilities": ["leadershipAbility"],
      "shouldStop": false
    },
    "missingEvidence": ["communicationAbility"],
    "canGenerateProfile": true,
    "stage": "deep_dive"
  }
}
```

Agent 内部 I/O 遵循 `AGENT_DESIGN.md` Interview Agent；**前端只看到整理后的 Turn**。

---

## 9. 步骤四：人才画像

对应页面：`/pages/candidate/profile/index`

DTO 对齐 `packages/domain` 的 `TalentProfile`。

### 9.1 生成画像

#### `POST /journeys/{journeyId}/profile/generate`

**Response `202`**

```json
{
  "data": {
    "taskId": "task_01h2x3y4z5"
  }
}
```

任务成功后 `result.talentProfileId` 可用于 GET。

### 9.2 查询画像

#### `GET /journeys/{journeyId}/profile`

**Response `200`**

```json
{
  "data": {
    "id": "tp_01h2x3y4z5",
    "journeyId": "journey_01h2x3y4z5",
    "candidate": { "id": "...", "name": "张伟", "title": "..." },
    "summary": "该候选人具备较强的...",
    "overallScore": 94,
    "capabilities": [
      {
        "key": "learningAbility",
        "name": "学习能力",
        "score": 4.8,
        "confidence": "high",
        "reason": "能快速掌握并应用新技术...",
        "evidenceIds": ["ev_001", "ev_002"]
      }
    ],
    "strengths": ["复杂系统架构经验丰富"],
    "risks": ["职业稳定性待验证"],
    "preferences": ["技术深耕", "架构方向"],
    "recommendedRoles": ["资深架构专家"],
    "confidence": "medium",
    "evidence": [
      {
        "id": "ev_001",
        "source": "resume",
        "snippet": "主导微服务架构升级...",
        "capabilityKeys": ["executionAbility"]
      }
    ],
    "generatedAt": "2026-06-09T08:30:00Z"
  }
}
```

### 9.3 证据链（可选拆分）

若画像体过大，可拆：

#### `GET /journeys/{journeyId}/profile/evidence`

---

## 10. 步骤五：简历生成与导出

对应页面：`/pages/candidate/resume/index`

第一版交付目标：**生成可用简历内容并支持复制 / 导出**。版本类型对齐 `packages/shared` 的 `ResumeVersionKey`：

| key | 说明 | v1 |
| --- | --- | --- |
| `ats` | ATS 友好纯文本结构 | ✅ |
| `hr` | HR 阅读版 | ✅ |
| `platform` | 招聘平台简介 | ✅ |
| `email` | 邮件正文 | ✅ |

### 10.1 ResumeVersion 对象

```json
{
  "id": "rv_01h2x3y4z5",
  "journeyId": "journey_01h2x3y4z5",
  "versionKey": "ats",
  "title": "张伟 - 高级软件工程师",
  "content": "纯文本或 Markdown 全文",
  "contentFormat": "plain",
  "confidence": "medium",
  "warnings": ["部分量化数据来自访谈，建议核对"],
  "usedEvidenceIds": ["ev_001"],
  "generatedAt": "2026-06-09T08:40:00Z"
}
```

Agent 内部遵循 `AGENT_DESIGN.md` Resume Builder Agent。

### 10.2 接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| `GET` | `/journeys/{journeyId}/resume-versions` | 已生成版本列表 |
| `POST` | `/journeys/{journeyId}/resume-versions` | 生成指定版本（异步） |
| `GET` | `/journeys/{journeyId}/resume-versions/{versionKey}` | 获取单个版本内容 |
| `POST` | `/journeys/{journeyId}/resume-versions/{versionKey}/regenerate` | 重新生成 |
| `POST` | `/journeys/{journeyId}/resume-versions/{versionKey}/export` | 导出 PDF / Word（异步） |

#### `POST .../resume-versions`

**Request**

```json
{
  "versionKey": "ats"
}
```

**Response `202`**：`{ "taskId": "..." }`

#### `POST .../resume-versions/{versionKey}/export`

**Request**

```json
{
  "format": "pdf"
}
```

`format`：`pdf` | `docx`

**Response `202`**，任务完成后 `result`：

```json
{
  "downloadUrl": "https://...",
  "expiresAt": "2026-06-09T09:00:00Z",
  "fileName": "resume-ats-张伟.pdf"
}
```

---

## 11. 页面与 API 映射总表

| 步骤 | 前端路由 | 核心 API |
| --- | --- | --- |
| 上传简历 | `/pages/candidate/upload/index` | `POST /resume-files` → `POST /parse-resume` → `GET /tasks/{id}` |
| 简历校对 | `/pages/candidate/review/index` | `GET/PATCH /structured-resume` → `POST .../confirm` |
| AI 访谈 | `/pages/candidate/interview/index` | `GET/POST /interview/*` |
| 人才画像 | `/pages/candidate/profile/index` | `POST /profile/generate` → `GET /profile` |
| 简历生成 | `/pages/candidate/resume/index` | `POST/GET /resume-versions` → `POST .../export` |

**冷启动推荐调用顺序：**

```text
POST /auth/session
POST /journeys
POST /journeys/{id}/resume-files
POST /journeys/{id}/parse-resume
GET  /tasks/{taskId}                       # 直到 succeeded
GET  /journeys/{id}/structured-resume
PATCH /journeys/{id}/structured-resume     # 用户校对
POST /journeys/{id}/structured-resume/confirm
POST /journeys/{id}/interview/turns        # 多轮
POST /journeys/{id}/interview/complete
POST /journeys/{id}/profile/generate
GET  /tasks/{taskId}
GET  /journeys/{id}/profile
POST /journeys/{id}/resume-versions        # 按需生成各版本
POST /journeys/{id}/resume-versions/ats/export
```

---

## 12. HR 侧（暂不实现，预留）

以下路径 **第一版不实现**，返回 `404` 或网关层不路由。实现时参考 `MATCHING_DESIGN.md` 与 `AGENT_DESIGN.md` Match Agent。

| 预留前缀 | 资源 |
| --- | --- |
| `/api/v1/hr/jobs` | 岗位需求、岗位画像 |
| `/api/v1/hr/matches` | 候选人推荐、匹配详情 |
| `/api/v1/hr/exports` | HR 导出包 |

鉴权届时使用 `role: HR` + 企业微信 `userid` 绑定。

---

## 13. Agent 层与 REST 层边界

```text
┌─────────────────────────────────────────┐
│  apps/mobile  (packages/api client)      │
└──────────────────┬──────────────────────┘
                   │ REST /api/v1
┌──────────────────▼──────────────────────┐
│  services/java-api                       │
│  ├─ Controllers                          │
│  ├─ Journey / Task 编排                  │
│  ├─ Auth / Entitlement                   │
│  └─ AgentOrchestrator                    │
└──────────────────┬──────────────────────┘
                   │ 内部调用（不对前端暴露）
┌──────────────────▼──────────────────────┐
│  Agent Services（AGENT_DESIGN 契约）      │
│  Resume / Interview / Profile / Builder  │
└─────────────────────────────────────────┘
```

- 前端 **只调用 REST**，不直连 LLM。
- Agent JSON 输入输出以 `AGENT_DESIGN.md` 为准。
- 每个 Task 保留 Agent 原始输出日志，供内部调试（`Internal Operator Path`）。

---

## 14. Spring Boot 模块建议

```text
services/java-api/
├── src/main/java/com/aitalentagent/api/
│   ├── ApiApplication.java
│   ├── config/          # Security, CORS, OpenAPI
│   ├── auth/            # Session, JWT, InviteCode, 未来 WeChat
│   ├── candidate/       # Journey, Resume, Interview, Profile
│   ├── task/            # 异步任务与轮询
│   ├── agent/           # Agent 编排客户端
│   ├── storage/         # 文件 OSS / 本地
│   └── common/          # 统一响应、异常、错误码
└── src/main/resources/
    ├── application.yml
    └── db/migration/    # Flyway
```

**建议依赖**：Spring Web、Spring Security、Spring Data JPA、Redis、Flyway、PostgreSQL、MinIO/OSS SDK。

---

## 15. 数据库核心表（草案）

| 表 | 说明 |
| --- | --- |
| `users` | 用户主表（匿名 / 微信绑定） |
| `user_bindings` | 第三方身份绑定 |
| `sessions` | 会话与 refresh token |
| `journeys` | 求职者旅程与状态 |
| `resume_files` | 上传文件元数据 |
| `structured_resumes` | 结构化简历 |
| `interview_sessions` | 访谈会话 |
| `interview_turns` | 访谈轮次 |
| `talent_profiles` | 人才画像 |
| `profile_evidence` | 证据链 |
| `resume_versions` | 生成的简历版本 |
| `async_tasks` | 异步任务 |
| `agent_run_logs` | Agent 输入输出审计 |

---

## 16. 限流与配额（v1）

求职者 **免费完整体验**，但为防止滥用建议：

| 维度 | v1 建议限额 |
| --- | --- |
| 每用户活跃旅程 | 1 条（新上传走 `restart`） |
| 每日解析次数 | 10 次 / user |
| 每日画像生成 | 5 次 / user |
| 每日简历导出 | 20 次 / user |
| 文件大小 | 20MB |

超限返回 `429 RATE_LIMITED`。**不做付费墙**；后续若引入计费，通过 `entitlements` 扩展，不改接口路径。

---

## 17. 版本演进

| 版本 | 范围 |
| --- | --- |
| **v1.0**（本文档） | 求职者五步闭环；匿名会话；内测门禁；Java 实现 |
| v1.1 | 微信公众号 / 小程序登录绑定 |
| v1.2 | HR 岗位与匹配 |
| v2.0 | 对外开放、运营配置、管理后台 |

接口版本通过 URL `/api/v1` 管理；破坏性变更升 `v2`。

---

## 18. 与前端 packages 对齐清单

实现后端时，建议同步：

1. 扩展 `packages/domain`：补充 `Journey`、`Task`、`ResumeVersion`、`Evidence` 类型。
2. 重写 `packages/api`：由 mock 改为 HTTP client，函数签名与本文档一致。
3. Agent 字段变更时：先改 `AGENT_DESIGN.md`，再改本文档与 `domain`。

---

## 19. 第一版验收标准

- [ ] 匿名用户可无注册走完五步流程。
- [ ] 上传 PDF/Word 后可异步解析并进入校对。
- [ ] 校对保存、确认后进入多轮访谈。
- [ ] 访谈结束后可生成画像，含能力分、优劣势、证据。
- [ ] 可生成 ATS/HR/平台/邮件四个简历版本。
- [ ] 可导出 PDF 或 Word（或返回可下载 URL）。
- [ ] 全流程无需付费校验。
- [ ] 未授权内测请求被拒绝（401/403）。
- [ ] Token 结构含 `role`、`entitlements`、`bindings`，便于后续接微信与 HR。
