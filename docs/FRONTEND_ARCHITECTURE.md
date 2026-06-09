# Frontend Architecture

## 1. 结论

AI Talent Agent 前端正式主线采用 `uni-app + Vue3`，以 `apps/mobile` 作为主要应用。

原因：

- 求职者从微信公众号进入，不需要主动注册账号。
- 第一阶段需要公众号 H5、企业微信 H5、普通移动浏览器可用。
- 第二阶段需要微信小程序。
- 未来如果做 App，希望候选人侧流程可以继续复用。
- 团队倾向 Vue 技术栈，uni-app 更适合微信生态和多端复用。

当前 `apps/h5` 是早期 Vue3 + Vite 原型，用于验证页面流程和 mock 数据，不再作为正式主线继续投入。

## 2. 应用结构

```text
.
├── apps/
│   ├── mobile/   # 正式主线：uni-app + Vue3
│   ├── h5/       # 早期原型：Vue3 + Vite
│   ├── mini/     # 旧预留目录，后续可废弃或合并到 mobile
│   └── admin/    # 后续 HR 桌面工作台，可选
├── packages/
│   ├── api/
│   ├── domain/
│   ├── shared/
│   └── design-tokens/
├── services/
│   └── java-api/ # 后续 Java 后端预留
└── docs/
```

## 3. `apps/mobile` 目标端

`apps/mobile` 负责：

- 公众号 H5。
- 企业微信 H5。
- 普通移动浏览器。
- 微信小程序。
- 未来 App 初版。

常用命令：

```bash
npm run dev:mobile:h5
npm run dev:mobile:mp-weixin
npm run build:mobile:h5
npm run build:mobile:mp-weixin
```

## 4. 用户身份策略

产品体验上，求职者不需要注册账号。

技术上仍需要身份标识：

- 公众号 H5：通过服务号网页授权拿 `openid`。
- 微信小程序：通过 `wx.login` 获取小程序 `openid`。
- 同主体绑定开放平台后，用 `unionid` 打通公众号和小程序身份。
- HR 端：通过企业微信身份进入，使用企业微信 `userid`。

第一阶段不接真实授权，只保留流程和接口边界。

## 5. 页面策略

候选人侧优先移动端体验，当前主流程已经在 `apps/mobile` 中落地：

- 首页。
- 简历上传。
- 简历解析预览 / 校对。
- AI 访谈（默认文字聊天，预留语音模式）。
- 人才画像。
- 简历生成与多版本预览。

HR 侧当前也已经在 `apps/mobile` 中形成演示闭环：

- 岗位需求输入。
- 岗位画像确认。
- 候选人推荐列表。
- 候选人匹配详情。
- HR 导出工作站。

后续如果 HR 工作台复杂度继续上升，再将批量管理、权限、运营配置、数据看板等能力拆到 `apps/admin`。

## 6. 共享层

`packages/domain`：领域类型。

`packages/shared`：mock 数据、环境检测、工具函数。

`packages/api`：API client，第一阶段使用 mock，后续接 Java 后端。

`packages/design-tokens`：颜色、断点、间距等设计基础。

## 7. Java 后端预留

后续 Java 后端位于 `services/java-api`。

建议职责：

- 微信公众号 / 小程序 / 企业微信身份绑定。
- 简历上传与解析任务。
- Agent 编排接口。
- 人才画像与岗位匹配数据存储。
- HR 导出包生成。

## 8. Stitch 导出内容使用原则

`docs/stitch_ai_talent_agent/` 仍作为视觉参考。

不要直接复制静态 HTML 到 uni-app；应提取页面结构、色彩、卡片、按钮和信息层级，用 uni-app 组件重写。
