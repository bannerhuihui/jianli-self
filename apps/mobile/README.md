# AI Talent Agent Mobile

`apps/mobile` 是正式前端主线，基于 uni-app + Vue3。

目标端：

- 公众号 H5。
- 企业微信 H5。
- 普通移动浏览器。
- 微信小程序。
- 未来 App。

常用命令：

```bash
npm run dev:mobile:h5
npm run dev:mobile:mp-weixin
npm run build:mobile:h5
npm run build:mobile:mp-weixin
```

当前阶段先用 mock 数据跑通页面流程，后续再接微信授权、企微身份和真实后端 API。
