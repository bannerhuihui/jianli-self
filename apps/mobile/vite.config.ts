import { defineConfig } from 'vite';
import uniPlugin from '@dcloudio/vite-plugin-uni';

const uni = typeof uniPlugin === 'function' ? uniPlugin : (uniPlugin as unknown as { default: typeof uniPlugin }).default;

export default defineConfig({
  plugins: [uni()],
  optimizeDeps: {
    // 工作区包走源码，避免预构建缓存遗漏新增 export 导致页面加载失败
    exclude: ['@ai-talent-agent/shared', '@ai-talent-agent/domain', '@ai-talent-agent/api'],
  },
});
