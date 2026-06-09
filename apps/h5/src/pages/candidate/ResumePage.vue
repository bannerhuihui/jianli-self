<script setup lang="ts">
import { mockResume, mockTalentProfile } from '@ai-talent-agent/shared';
import StepBar from '../../components/StepBar.vue';
</script>

<template>
  <main class="mx-auto max-w-7xl px-4 py-8 md:px-8">
    <StepBar :steps="['上传简历','解析校对','AI 访谈','人才画像','简历生成']" :current="4" />
    <div class="mt-8 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
      <div><h1 class="text-2xl font-bold">生成您的专属简历版本</h1><p class="mt-2 text-slate-600">基于 AI 深度分析，为您提供多维度求职解决方案。</p></div>
      <div class="flex gap-2"><button class="rounded-xl border border-blue-200 px-4 py-2 font-semibold text-[#004ac6]">复制内容</button><button class="rounded-xl border border-blue-200 px-4 py-2 font-semibold text-[#004ac6]">导出 Word</button><button class="rounded-xl bg-[#004ac6] px-4 py-2 font-semibold text-white">导出 PDF</button></div>
    </div>
    <section class="mt-6 grid gap-6 lg:grid-cols-[300px_1fr]">
      <aside class="space-y-4">
        <div class="rounded-2xl border-2 border-[#004ac6] bg-white p-5 shadow-sm"><div class="flex justify-between"><h2 class="font-bold">ATS 版本</h2><span class="rounded-full bg-emerald-50 px-2 py-1 text-xs text-emerald-700">高通过率</span></div><p class="mt-3 text-sm leading-6 text-slate-600">针对招聘系统算法优化，结构清晰，确保解析无误。</p></div>
        <div class="rounded-2xl bg-white p-5 shadow-sm"><div class="flex justify-between"><h2 class="font-bold">HR 版本</h2><span class="rounded-full bg-blue-50 px-2 py-1 text-xs text-[#004ac6]">视觉优先</span></div><p class="mt-3 text-sm leading-6 text-slate-600">重点突出，适合人工阅读。</p></div>
        <div class="rounded-2xl bg-blue-50 p-5 text-sm text-slate-600">所有版本均由真实经历和访谈事实生成，不会编造项目、学历或数据。</div>
      </aside>
      <section class="rounded-3xl bg-white p-6 shadow-sm">
        <div class="mx-auto max-w-xl rounded-2xl border border-slate-200 bg-white p-8 shadow-lg">
          <h2 class="text-3xl font-bold">{{ mockResume.basicInfo.name }}</h2>
          <p class="mt-2 text-sm text-slate-500">{{ mockResume.basicInfo.title }} | {{ mockResume.basicInfo.location }} | {{ mockResume.basicInfo.email }}</p>
          <h3 class="mt-6 border-b pb-2 font-bold">个人总结</h3><p class="mt-3 text-sm leading-6">{{ mockTalentProfile.summary }}</p>
          <h3 class="mt-6 border-b pb-2 font-bold">工作经历</h3><ul class="mt-3 list-disc space-y-2 pl-5 text-sm"><li v-for="item in mockResume.workExperience" :key="item">{{ item }}</li></ul>
          <h3 class="mt-6 border-b pb-2 font-bold">技能</h3><p class="mt-3 text-sm">{{ mockResume.skills.join('、') }}</p>
        </div>
      </section>
    </section>
  </main>
</template>
