<script setup lang="ts">
import { mockJobProfile } from '@ai-talent-agent/shared';
</script>
<template>
  <main class="mx-auto max-w-7xl px-4 py-8 md:px-8">
    <div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between"><div><p class="text-sm font-semibold text-[#004ac6]">AI 岗位画像分析已完成</p><h1 class="mt-2 text-2xl font-bold">岗位画像确认</h1><p class="mt-2 text-slate-600">请校对 AI 提取的关键要求，确保画像准确。</p></div><RouterLink to="/hr/candidates" class="rounded-xl bg-[#004ac6] px-5 py-3 font-semibold text-white">开始匹配候选人 →</RouterLink></div>
    <section class="mt-8 grid gap-6 lg:grid-cols-[1fr_320px]">
      <div class="space-y-5">
        <div class="rounded-3xl bg-white p-6 shadow-sm"><div class="flex justify-between"><h2 class="font-bold">基本信息</h2><button class="text-sm text-[#004ac6]">编辑全部</button></div><div class="mt-5 grid gap-4 md:grid-cols-2"><p><span class="text-sm text-slate-500">岗位名称</span><br><strong>{{ mockJobProfile.title }}</strong></p><p><span class="text-sm text-slate-500">经验要求</span><br><strong>{{ mockJobProfile.seniority }}</strong></p></div></div>
        <div class="rounded-3xl bg-white p-6 shadow-sm"><h2 class="font-bold">核心职责</h2><ul class="mt-4 list-disc space-y-2 pl-5 text-slate-700"><li v-for="item in mockJobProfile.responsibilities" :key="item">{{ item }}</li></ul><div class="mt-4 rounded-xl bg-blue-50 p-3 text-sm text-[#004ac6]">AI 建议：可补充云服务管理经验要求。</div></div>
        <div class="grid gap-4 md:grid-cols-2"><div class="rounded-2xl bg-white p-5 shadow-sm"><h2 class="font-bold">必须具备</h2><div class="mt-3 flex flex-wrap gap-2"><span v-for="item in mockJobProfile.requiredSkills" :key="item" class="rounded-full bg-blue-50 px-3 py-1 text-sm text-[#004ac6]">{{ item }}</span></div></div><div class="rounded-2xl bg-white p-5 shadow-sm"><h2 class="font-bold">加分项</h2><div class="mt-3 flex flex-wrap gap-2"><span v-for="item in mockJobProfile.bonusSkills" :key="item" class="rounded-full bg-slate-100 px-3 py-1 text-sm">{{ item }}</span></div></div></div>
      </div>
      <aside class="space-y-5"><div class="rounded-3xl bg-white p-6 shadow-sm"><h2 class="font-bold">岗位能力要求</h2><div class="mt-5 space-y-3"><div v-for="cap in mockJobProfile.requiredCapabilities" :key="cap"><div class="mb-1 flex justify-between text-sm"><span>{{ cap }}</span><span>高</span></div><div class="h-2 rounded-full bg-blue-50"><div class="h-2 w-4/5 rounded-full bg-[#004ac6]"></div></div></div></div></div><div class="rounded-3xl bg-amber-50 p-5 text-sm text-amber-900">建议补充：工作模式偏好和到岗时间要求。</div></aside>
    </section>
  </main>
</template>
