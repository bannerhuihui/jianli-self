<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router';
import RuntimeNotice from './components/RuntimeNotice.vue';
</script>

<template>
  <div class="min-h-screen bg-[#f8f9ff] text-[#0b1c30]">
    <RuntimeNotice />
    <header class="sticky top-0 z-30 border-b border-blue-100 bg-white/90 backdrop-blur">
      <div class="mx-auto flex max-w-7xl items-center justify-between px-4 py-3 md:px-8">
        <RouterLink to="/" class="text-lg font-bold text-[#004ac6]">AI Talent Agent</RouterLink>
        <nav class="hidden items-center gap-6 text-sm text-slate-600 md:flex">
          <RouterLink to="/" class="hover:text-[#004ac6]">首页</RouterLink>
          <RouterLink to="/candidate/upload" class="hover:text-[#004ac6]">我的简历</RouterLink>
          <RouterLink to="/candidate/interview" class="hover:text-[#004ac6]">AI 访谈</RouterLink>
          <RouterLink to="/candidate/profile" class="hover:text-[#004ac6]">个人画像</RouterLink>
          <RouterLink to="/hr/job/new" class="hover:text-[#004ac6]">HR 工作台</RouterLink>
        </nav>
        <RouterLink to="/hr/export" class="rounded-full bg-blue-50 px-3 py-2 text-xs font-semibold text-[#004ac6] md:hidden">导出</RouterLink>
      </div>
    </header>
    <RouterView />
  </div>
</template>
