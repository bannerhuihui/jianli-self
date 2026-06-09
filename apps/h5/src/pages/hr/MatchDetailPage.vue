<script setup lang="ts">
import { mockMatches } from '@ai-talent-agent/shared';
const match = mockMatches[0];
</script>
<template>
  <main class="mx-auto max-w-7xl px-4 py-8 md:px-8">
    <RouterLink to="/hr/candidates" class="text-sm text-[#004ac6]">← 返回列表</RouterLink>
    <section class="mt-6 grid gap-6 lg:grid-cols-[1fr_320px]">
      <div class="space-y-6">
        <div class="rounded-3xl bg-white p-6 shadow-sm"><div class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between"><div><p class="text-sm font-semibold text-emerald-700">建议进入面试</p><h1 class="mt-2 text-2xl font-bold">{{ match.candidate.name }} · 匹配详情</h1><p class="mt-2 text-slate-600">候选人与岗位高度匹配，但需验证稳定性与到岗时间。</p></div><div class="grid h-24 w-24 place-items-center rounded-full border-8 border-blue-100 text-3xl font-bold text-[#004ac6]">{{ match.totalScore }}</div></div></div>
        <div class="rounded-3xl bg-white p-6 shadow-sm"><h2 class="font-bold">匹配维度拆解</h2><div class="mt-4 grid gap-4 md:grid-cols-4"><div class="rounded-xl bg-blue-50 p-4">项目经验<br><b>{{ match.breakdown.experience }}%</b></div><div class="rounded-xl bg-blue-50 p-4">专业技能<br><b>{{ match.breakdown.skills }}%</b></div><div class="rounded-xl bg-blue-50 p-4">画像匹配<br><b>{{ match.breakdown.profile }}%</b></div><div class="rounded-xl bg-blue-50 p-4">职业倾向<br><b>{{ match.breakdown.careerPreference }}%</b></div></div></div>
        <div class="rounded-3xl bg-white p-6 shadow-sm"><h2 class="font-bold">核心推荐理由</h2><div class="mt-4 space-y-3"><div v-for="item in match.reasons" :key="item" class="rounded-xl bg-blue-50 p-4 text-sm text-slate-700">{{ item }} <button class="ml-2 text-[#004ac6]">查看证据</button></div></div></div>
        <div class="rounded-3xl bg-rose-50 p-6"><h2 class="font-bold text-rose-800">需关注事项</h2><div class="mt-4 grid gap-3 md:grid-cols-2"><div v-for="item in match.risks" :key="item" class="rounded-xl bg-white p-4 text-sm text-rose-700">{{ item }}</div></div></div>
        <div class="rounded-3xl bg-white p-6 shadow-sm"><div class="flex justify-between"><h2 class="font-bold">建议面试问题</h2><button class="text-sm text-[#004ac6]">复制全部</button></div><div class="mt-4 space-y-3"><div v-for="(q, index) in match.interviewQuestions" :key="q" class="rounded-xl bg-blue-50 p-4 text-sm"><b>Q{{ index + 1 }}：</b>{{ q }}</div></div></div>
      </div>
      <aside class="space-y-5"><div class="rounded-3xl bg-white p-6 shadow-sm"><div class="flex items-center gap-3"><div class="grid h-14 w-14 place-items-center rounded-2xl bg-blue-100 text-2xl font-bold text-[#004ac6]">{{ match.candidate.name[0] }}</div><div><h2 class="text-xl font-bold">{{ match.candidate.name }}</h2><p class="text-sm text-slate-500">{{ match.candidate.title }}</p></div></div><p class="mt-4 text-sm text-slate-600">{{ match.candidate.location }} · {{ match.candidate.experienceYears }} 年经验 · {{ match.candidate.education }}</p></div><div class="rounded-3xl bg-white p-6 shadow-sm"><h2 class="font-bold">简历预览</h2><div class="mt-4 rounded-2xl border bg-slate-50 p-4 text-sm leading-6">{{ match.candidate.name }} 的个人简历<br>高级架构师 / 技术专家<br>工作经历、项目经历、技能栈...</div></div><RouterLink to="/hr/export" class="block rounded-xl bg-[#004ac6] px-4 py-3 text-center font-semibold text-white">导出推荐包</RouterLink></aside>
    </section>
  </main>
</template>
