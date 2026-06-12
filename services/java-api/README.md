# Java API Service

AI Talent Agent 第一版后端，基于 **Spring Boot 3 + Java 17**。

当前实现：

- 求职者五步闭环 REST API（对齐 `docs/API_CONTRACT.md`）
- 匿名会话鉴权（JWT，可扩展微信绑定）
- Journey 状态机 + 异步 Task 轮询
- **Mock Agent**（`app.agents.mode=mock`），无需大模型即可跑通全流程

## 快速启动

```bash
cd services/java-api
mvn spring-boot:run
```

服务默认监听 `http://localhost:8080`。

或在仓库根目录：

```bash
npm run dev:api
```

## 配置

`src/main/resources/application.yml`：

| 配置项 | 说明 | 默认 |
| --- | --- | --- |
| `app.agents.mode` | `mock` / 未来 `llm` | `mock` |
| `app.agents.mock-delay-ms` | Mock 任务模拟延迟 | `3000` |
| `app.invite.enabled` | 是否启用邀请码 | `false` |
| `app.auth.jwt-secret` | JWT 密钥（≥32 字符） | 开发默认值 |

## 冷启动调用示例

```bash
# 1. 创建匿名会话
curl -s -X POST http://localhost:8080/api/v1/auth/session \
  -H 'Content-Type: application/json' \
  -d '{"authProvider":"anonymous","deviceId":"demo-device-1","platform":"h5"}'

# 2. 使用返回的 accessToken
export TOKEN="<accessToken>"

# 3. 创建旅程
curl -s -X POST http://localhost:8080/api/v1/journeys \
  -H "Authorization: Bearer $TOKEN"

# 4. 上传简历 → 解析 → 轮询任务 → 后续步骤见 API_CONTRACT.md
```

## 测试

```bash
mvn test
```

## 目录结构

```text
src/main/java/com/aitalentagent/api/
├── auth/          # JWT 鉴权
├── agent/         # Mock Agent 数据
├── config/        # Security、CORS、异步线程池
├── domain/        # 领域模型
├── repository/    # 内存存储（后续可换 JPA）
├── service/       # 业务编排
└── web/           # REST Controllers
```

## 下一步

1. `packages/api` 接入真实 HTTP client
2. PostgreSQL + JPA 替换 `InMemoryStore`
3. 实现 `LlmClient` 接口，将 Mock Agent 替换为真实 Agent
