<script setup lang="ts">
import { computed } from 'vue';
import { getRuntimeEnv } from '@ai-talent-agent/shared';

const env = getRuntimeEnv();
const message = computed(() => {
  if (env === 'wechat') return '当前在微信内打开：第一版已预留公众号授权、分享和上传兼容能力。';
  if (env === 'wecom') return '当前在企业微信内打开：推荐优先使用复制摘要、转发链接和导出包。';
  if (env === 'mobile-browser') return '当前为手机浏览器：适合候选人查看画像和生成简历。';
  return '当前为桌面预览：适合 HR 工作台、批量筛选和导出演示。';
});
</script>

<template>
  <div class="border-b border-blue-100 bg-blue-50 px-4 py-2 text-center text-xs text-[#004ac6] md:text-sm">
    {{ message }}
  </div>
</template>
