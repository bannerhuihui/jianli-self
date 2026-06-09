# Mini Program App Plan

`apps/mini` 是第二阶段微信小程序端预留目录。

第一阶段不开发小程序，原因：

- 优先验证公众号 H5 / 企业微信 H5 流程。
- 避免过早处理小程序包体、组件差异和审核流程。
- 先稳定候选人画像、AI 访谈和 HR 推荐流程。

第二阶段建议技术栈：

- uni-app。
- Vue 3。
- TypeScript。

复用范围：

- `packages/domain`：领域类型。
- `packages/api`：API client。
- `packages/shared`：mock 数据、环境工具、通用函数。
- `packages/design-tokens`：颜色、间距、字体和断点。

迁移顺序：

1. 候选人首页。
2. 简历上传 / 手动录入。
3. AI 访谈。
4. 人才画像。
5. 简历版本查看。

HR 侧小程序工作台后置，第一版仅提供候选人摘要和推荐包查看。
