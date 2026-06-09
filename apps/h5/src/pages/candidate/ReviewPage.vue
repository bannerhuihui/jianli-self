<script setup lang="ts">
import { mockResume } from '@ai-talent-agent/shared';
import StepBar from '../../components/StepBar.vue';
</script>

<template>
  <main class="mx-auto max-w-7xl px-4 py-8 md:px-8">
    <StepBar :steps="['上传简历','解析校对','AI 访谈','人才画像','简历生成']" :current="1" />
    <section class="mt-8 grid gap-6 lg:grid-cols-[420px_1fr]">
      <aside class="rounded-3xl bg-blue-50 p-6">
        <div class="rounded-2xl bg-white p-6 shadow-sm">
          <h2 class="text-2xl font-bold">{{ mockResume.basicInfo.name }}的简历</h2>
          <p class="mt-2 text-sm text-slate-500">{{ mockResume.basicInfo.phone }} · {{ mockResume.basicInfo.email }}</p>
          <h3 class="mt-6 font-bold">工作经历</h3>
          <ul class="mt-2 list-disc space-y-2 pl-5 text-sm text-slate-700"><li v-for="item in mockResume.workExperience" :key="item">{{ item }}</li></ul>
          <h3 class="mt-6 font-bold">专业技能</h3>
          <p class="mt-2 text-sm text-slate-700">{{ mockResume.skills.join('、') }}</p>
        </div>
      </aside>
      <div class="space-y-4">
        <h1 class="text-2xl font-bold">校对简历解析结果</h1>
        <p class="text-slate-600">请检查 AI 提取的信息。确认后的内容会用于 AI 访谈和人才画像生成。</p>
        <section class="rounded-2xl bg-white p-5 shadow-sm"><div class="mb-3 flex justify-between"><h2 class="font-bold">基本信息</h2><span class="rounded-full bg-emerald-50 px-3 py-1 text-xs text-emerald-700">置信度：高</span></div><div class="grid gap-3 md:grid-cols-2"><input class="rounded-xl border border-blue-100 p-3" :value="mockResume.basicInfo.name" /><input class="rounded-xl border border-blue-100 p-3" :value="mockResume.basicInfo.phone" /></div></section>
        <section class="rounded-2xl border-l-4 border-amber-400 bg-white p-5 shadow-sm"><div class="mb-3 flex justify-between"><h2 class="font-bold">教育经历</h2><span class="rounded-full bg-amber-50 px-3 py-1 text-xs text-amber-700">建议复核</span></div><p class="text-sm text-slate-700">{{ mockResume.education[0] }}</p></section>
        <section class="rounded-2xl bg-white p-5 shadow-sm"><h2 class="font-bold">工作经历</h2><div class="mt-3 rounded-xl bg-blue-50 p-4 text-sm text-slate-700">{{ mockResume.workExperience[0] }}</div></section>
        <RouterLink to="/candidate/interview" class="block rounded-xl bg-[#004ac6] px-6 py-3 text-center font-semibold text-white">确认并开始 AI 访谈</RouterLink>
      </div>
    </section>
  </main>
</template>
