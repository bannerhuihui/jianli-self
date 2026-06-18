import { defineConfig } from 'vite';
import uniPlugin from '@dcloudio/vite-plugin-uni';

const uni = typeof uniPlugin === 'function' ? uniPlugin : (uniPlugin as unknown as { default: typeof uniPlugin }).default;

const pkgEntry = (name: string) =>
  new URL(`../../packages/${name}/src/index.ts`, import.meta.url).pathname;

export default defineConfig({
  plugins: [uni()],
  resolve: {
    // 直接指向工作区包源码，绕开 node_modules 软链：
    // 否则改动 packages/*/src 不会触发 HMR（vite 默认不监听 node_modules），
    // 导致新增 export 读到旧缓存而报 "does not provide an export named ..."。
    alias: {
      '@ai-talent-agent/api': pkgEntry('api'),
      '@ai-talent-agent/domain': pkgEntry('domain'),
      '@ai-talent-agent/shared': pkgEntry('shared'),
    },
  },
  optimizeDeps: {
    // 工作区包走源码，避免预构建缓存遗漏新增 export 导致页面加载失败
    exclude: ['@ai-talent-agent/shared', '@ai-talent-agent/domain', '@ai-talent-agent/api'],
  },
  css: {
    preprocessorOptions: {
      scss: {
        // 使用 Dart Sass 新编译器 API，消除 legacy-js-api 弃用警告
        api: 'modern-compiler',
      },
    },
  },
});
