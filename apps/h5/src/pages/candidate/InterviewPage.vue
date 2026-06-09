<script setup lang="ts">
import StepBar from '../../components/StepBar.vue';
const turns = [
  ['agent','请结合您的项目经历，谈谈您在团队合作中遇到冲突时是如何处理的？'],
  ['user','在一次负责的电商平台重构项目中，曾与技术负责人就架构选择产生分歧。我主动使用服务架构对比表来量化不同方案。'],
  ['agent','这能补充沟通能力与逻辑能力证据。请再说明最后如何达成共识。'],
];
</script>

<template>
  <main class="mx-auto max-w-6xl px-4 py-8 md:px-8">
    <StepBar :steps="['上传简历','解析校对','AI 访谈','人才画像','简历生成']" :current="2" />
    <section class="mt-8 grid gap-6 lg:grid-cols-[240px_1fr_280px]">
      <aside class="rounded-3xl bg-white p-5 shadow-sm"><h2 class="font-bold">访谈进度</h2><p class="mt-4 text-sm text-slate-600">当前阶段：能力验证</p><p class="mt-2 text-sm text-slate-600">问题 3 / 8</p><div class="mt-4 rounded-xl bg-blue-50 p-3 text-sm text-[#004ac6]">目标能力：沟通能力、逻辑能力</div></aside>
      <section class="rounded-3xl bg-white p-5 shadow-sm"><h1 class="text-2xl font-bold">AI 深度访谈</h1><div class="mt-6 space-y-4"><div v-for="(turn, index) in turns" :key="index" :class="['rounded-2xl p-4 text-sm leading-6', turn[0] === 'agent' ? 'bg-blue-50 text-slate-700' : 'ml-auto bg-[#004ac6] text-white md:w-4/5']">{{ turn[1] }}</div></div><div class="mt-6 flex gap-3"><input class="flex-1 rounded-xl border border-blue-100 p-3" placeholder="输入您的回答..." /><RouterLink to="/candidate/profile" class="rounded-xl bg-[#004ac6] px-5 py-3 font-semibold text-white">生成画像</RouterLink></div></section>
      <aside class="rounded-3xl bg-white p-5 shadow-sm"><h2 class="font-bold">为什么问这个问题？</h2><p class="mt-3 text-sm leading-6 text-slate-600">用于补充沟通能力和逻辑能力的证据，不会单独决定最终评价。</p><button class="mt-5 w-full rounded-xl border border-blue-200 py-3 text-sm font-semibold text-[#004ac6]">跳过此问题</button></aside>
    </section>
  </main>
</template>
