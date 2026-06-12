# apps/mobile 源码结构

uni-app + Vue3 前端，当前以 **H5 桌面优先** 交付 MVP 演示。

## 目录说明

| 路径 | 职责 |
|------|------|
| `constants/flows.ts` | 求职者 / HR 五步流程的步骤名与路由 |
| `constants/match-detail-display.ts` | 匹配详情页展示用 mock 文案 |
| `components/` | 通用 UI（顶栏、步骤条、图标、状态面板等） |
| `pages/candidate/*` | 求职者流程 5 页 |
| `pages/hr/*` | HR 流程 5 页 |
| `styles/` | 全局 SCSS：token、布局壳、按钮体系 |
| `utils/feedback.ts` | Toast / 复制 / 模拟异步 |
| `utils/navigation.ts` | 流程步骤跳转 |
| `utils/flow-steps.ts` | ProgressSteps 的 props 工厂 |

## 样式约定

- 流程页根节点：`class="page hr-page"` 或 `class="page candidate-flow-page"`
- 主操作按钮：`flow-btn flow-btn--primary`
- 次操作按钮：`flow-btn flow-btn--secondary`
- HR 页仍可使用别名 `hr-primary-action` / `hr-secondary-action`

## 数据

业务 mock 来自 workspace 包 `@ai-talent-agent/shared`（`mock.ts`）。
页面内仅保留 **展示层** 常量（如匹配详情叙述文案）。
