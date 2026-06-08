# UI Components

## 1. 目标

本文件定义 AI Talent Agent 的核心 UI 组件。生成 UI 时应优先复用这些组件，避免每个页面风格不一致。

## 2. 全局组件

### 2.1 App Shell

用途：

- 承载顶部导航、侧边导航和页面内容。

内容：

- Logo。
- 当前流程入口：候选人、HR、内部调试。
- 用户状态。
- 帮助入口。

### 2.2 Step Progress

用途：

- 展示用户当前处于流程哪一步。

候选人侧步骤：

```text
上传简历 -> 校对解析 -> AI 访谈 -> 人才画像 -> 简历生成
```

HR 侧步骤：

```text
岗位需求 -> 岗位画像 -> 候选人推荐 -> 匹配详情 -> 导出
```

### 2.3 Status Badge

用途：

- 展示状态、置信度、风险等级。

类型：

- high confidence。
- medium confidence。
- low confidence。
- warning。
- error。
- ready。
- processing。

## 3. 候选人侧组件

### 3.1 Upload Card

用途：

- 上传简历。

内容：

- 拖拽上传区域。
- 支持格式。
- 文件大小限制。
- 复杂模板风险提示。
- 隐私说明。

状态：

- empty。
- file selected。
- uploading。
- error。

### 3.2 Resume Parse Quality Card

用途：

- 展示简历解析质量。

内容：

- 解析质量等级。
- 已识别字段数量。
- 缺失字段。
- 风险提示。

### 3.3 Editable Resume Section

用途：

- 展示和编辑结构化简历。

类型：

- Basic Info。
- Education。
- Work Experience。
- Project。
- Skills。

状态：

- complete。
- missing fields。
- low confidence。

### 3.4 Interview Chat Panel

用途：

- 承载 AI 访谈。

内容：

- AI 问题气泡。
- 用户回答气泡。
- 输入框。
- 跳过按钮。
- 发送按钮。
- 当前阶段。

### 3.5 Question Reason Card

用途：

- 解释 AI 为什么问这个问题。

内容：

- 本轮目标。
- 对应画像维度。
- 需要补充的证据。

### 3.6 Capability Score Card

用途：

- 展示单个能力维度。

内容：

- 能力名称。
- 1-5 分评分。
- 评分解释。
- 置信度。
- 证据入口。

### 3.7 Talent Profile Summary Card

用途：

- 展示候选人整体画像摘要。

内容：

- 一句话总结。
- 核心优势。
- 主要风险。
- 推荐岗位方向。
- 总体置信度。

### 3.8 Evidence Drawer

用途：

- 展示结论背后的证据。

内容：

- 证据来源：简历 / 访谈。
- 证据内容。
- 关联能力维度。
- 置信度。

### 3.9 Resume Preview

用途：

- 预览生成的简历内容。

内容：

- 简历标题。
- 个人摘要。
- 工作经历。
- 项目经历。
- 技能。
- 教育经历。

操作：

- 复制。
- 导出 Word。
- 导出 PDF。

## 4. HR 侧组件

### 4.1 Job Requirement Input

用途：

- 输入自然语言岗位需求。

内容：

- 大文本框。
- 示例需求。
- 关键字段提示。
- 生成岗位画像按钮。

### 4.2 Job Profile Card

用途：

- 展示 AI 拆解出的岗位画像。

内容：

- 岗位名称。
- 年限。
- 核心职责。
- 必备技能。
- 加分技能。
- 能力要求。
- 缺失字段。

### 4.3 Candidate Recommendation Card

用途：

- 在列表中展示候选人。

内容：

- 姓名。
- 当前岗位。
- 工作年限。
- 总匹配分。
- 分项得分。
- 推荐理由摘要。
- 风险标签。

操作：

- 查看详情。
- 复制摘要。
- 加入导出包。

### 4.4 Match Score Breakdown

用途：

- 展示分项匹配分。

分项：

- 经验匹配。
- 技能匹配。
- 画像匹配。
- 职业倾向。

### 4.5 Match Reason Card

用途：

- 展示推荐理由。

内容：

- 推荐结论。
- 对应岗位要求。
- 候选人证据。
- 置信度。

### 4.6 Risk Card

用途：

- 展示候选人风险点。

内容：

- 风险标题。
- 严重程度。
- 风险来源。
- 建议验证问题。

### 4.7 Interview Question List

用途：

- 展示建议面试问题。

内容：

- 问题。
- 验证目标。
- 关联风险。

### 4.8 Export Package Panel

用途：

- 导出 HR 推荐包。

内容：

- 候选人选择。
- 导出格式。
- 字段预览。
- 复制 / 下载按钮。

## 5. 内部调试组件

### 5.1 Agent Run Table

用途：

- 展示 Agent 运行记录。

字段：

- agentName。
- promptVersion。
- status。
- latency。
- confidence。
- createdAt。

### 5.2 JSON Viewer

用途：

- 查看 Agent 输入输出。

功能：

- 格式化展示。
- 复制。
- 折叠节点。

### 5.3 Warning Panel

用途：

- 展示 Agent 输出中的 warnings 和 missingFields。

## 6. 组件视觉建议

整体风格：

- 专业。
- 清晰。
- 克制。
- 可信。
- 面向 B2B 和职业服务，不要过度娱乐化。

颜色建议：

- 主色：深蓝或靛蓝。
- 辅助色：青绿，用于成功和高置信度。
- 警告色：琥珀，用于低置信度和风险。
- 错误色：红色，但少用。
- 背景：浅灰或近白。

排版建议：

- 多用卡片和分区。
- 避免一屏堆满信息。
- 重要结论先展示，证据可展开。
- HR 侧更像工作台，候选人侧更像引导式流程。

