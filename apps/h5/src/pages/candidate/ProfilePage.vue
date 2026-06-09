<script setup lang="ts">
import { mockTalentProfile } from '@ai-talent-agent/shared';
import ConfidenceBadge from '../../components/ConfidenceBadge.vue';
import StepBar from '../../components/StepBar.vue';
</script>

<template>
  <main class="mx-auto max-w-7xl px-4 py-8 md:px-8">
    <StepBar :steps="['上传简历','解析校对','AI 访谈','人才画像','简历生成']" :current="3" />
    <section class="mt-8 rounded-3xl bg-white p-6 shadow-sm">
      <div class="grid gap-6 lg:grid-cols-[360px_1fr]">
        <div class="rounded-3xl bg-blue-50 p-6 text-center">
          <div class="mx-auto grid h-48 w-48 place-items-center rounded-full border-[18px] border-blue-200 bg-white">
            <div><p class="text-sm text-slate-500">综合得分</p><p class="text-4xl font-bold text-[#004ac6]">{{ mockTalentProfile.overallScore }}</p></div>
          </div>
          <div class="mt-5 grid grid-cols-2 gap-2 text-xs text-slate-600">
            <span v-for="item in mockTalentProfile.capabilities" :key="item.key">• {{ item.name }}</span>
          </div>
        </div>
        <div class="space-y-5">
          <div>
            <p class="text-sm font-semibold text-[#004ac6]">{{ mockTalentProfile.candidate.name }} · {{ mockTalentProfile.candidate.title }}</p>
            <h1 class="mt-2 text-2xl font-bold">人才画像报告</h1>
            <p class="mt-3 leading-7 text-slate-600">{{ mockTalentProfile.summary }}</p>
          </div>
          <div class="grid gap-4 md:grid-cols-2">
            <div class="rounded-2xl bg-emerald-50 p-4"><h2 class="font-bold text-emerald-800">核心优势</h2><ul class="mt-3 space-y-2 text-sm text-emerald-900"><li v-for="item in mockTalentProfile.strengths" :key="item">✅ {{ item }}</li></ul></div>
            <div class="rounded-2xl bg-amber-50 p-4"><h2 class="font-bold text-amber-800">待验证风险</h2><ul class="mt-3 space-y-2 text-sm text-amber-900"><li v-for="item in mockTalentProfile.risks" :key="item">⚠️ {{ item }}</li></ul></div>
          </div>
        </div>
      </div>
    </section>

    <section class="mt-6 grid gap-4 md:grid-cols-2 xl:grid-cols-4">
      <article v-for="cap in mockTalentProfile.capabilities" :key="cap.key" class="rounded-2xl bg-white p-5 shadow-sm">
        <div class="flex items-center justify-between"><h3 class="font-bold">{{ cap.name }}</h3><ConfidenceBadge :confidence="cap.confidence" /></div>
        <p class="mt-4 text-3xl font-bold text-[#004ac6]">{{ cap.score }}<span class="text-sm text-slate-400"> / 5.0</span></p>
        <p class="mt-3 min-h-12 text-sm leading-6 text-slate-600">{{ cap.reason }}</p>
        <button class="mt-4 w-full rounded-xl bg-blue-50 py-2 text-sm font-semibold text-[#004ac6]">查看证据 →</button>
      </article>
    </section>

    <section class="mt-6 grid gap-4 lg:grid-cols-2">
      <div class="rounded-2xl bg-white p-5 shadow-sm"><h2 class="font-bold">职业偏好与岗位推荐</h2><div class="mt-4 flex flex-wrap gap-2"><span v-for="item in mockTalentProfile.preferences" :key="item" class="rounded-full bg-blue-50 px-3 py-1 text-sm text-[#004ac6]">{{ item }}</span></div><div class="mt-4 flex flex-wrap gap-2"><span v-for="item in mockTalentProfile.recommendedRoles" :key="item" class="rounded-full bg-[#004ac6] px-3 py-1 text-sm text-white">{{ item }}</span></div></div>
      <div class="rounded-2xl bg-white p-5 shadow-sm"><h2 class="font-bold">证据链入口 / 溯源中心</h2><div class="mt-4 space-y-3 text-sm text-slate-600"><p class="rounded-xl bg-blue-50 p-3">原始简历溯源：提取到 14 个核心能力证明点。</p><p class="rounded-xl bg-blue-50 p-3">面试语料分析：AI 识别到逻辑表达与协作证据。</p></div></div>
    </section>
    <RouterLink to="/candidate/resume" class="mt-6 block rounded-xl bg-[#004ac6] px-6 py-3 text-center font-semibold text-white">生成简历版本</RouterLink>
  </main>
</template>
