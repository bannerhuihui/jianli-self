<script setup lang="ts">
import StepBar from '../../components/StepBar.vue';
</script>

<template>
  <main class="mx-auto max-w-6xl px-4 py-8 md:px-8">
    <StepBar :steps="['上传简历','解析校对','AI 访谈','人才画像','简历生成']" :current="0" />
    <section class="mt-8 grid gap-6 lg:grid-cols-[1fr_320px]">
      <div class="rounded-3xl border border-dashed border-blue-300 bg-white p-8 text-center shadow-sm">
        <div class="mx-auto mb-4 grid h-16 w-16 place-items-center rounded-2xl bg-blue-50 text-3xl">📄</div>
        <h1 class="text-2xl font-bold">上传您的简历</h1>
        <p class="mt-3 text-slate-600">支持 PDF、Word。建议上传结构清晰的单栏简历，以提高解析准确性。</p>
        <div class="mt-8 rounded-2xl bg-blue-50 p-8 text-slate-500">拖拽文件至此，或点击浏览</div>
        <div class="mt-6 rounded-2xl bg-amber-50 p-4 text-left text-sm text-amber-800">复杂视觉模板、文本框、双栏排版和大量图标可能影响解析效果。精美模板更适合作为最终导出模板。</div>
        <RouterLink to="/candidate/review" class="mt-6 inline-flex rounded-xl bg-[#004ac6] px-6 py-3 font-semibold text-white">开始解析</RouterLink>
      </div>
      <aside class="rounded-3xl bg-white p-6 shadow-sm">
        <h2 class="text-lg font-bold">AI 深度解析</h2>
        <p class="mt-3 text-sm leading-6 text-slate-600">系统会识别核心技能、职业发展轨迹、项目影响力，并在下一步让您校对结果。</p>
        <ul class="mt-5 space-y-3 text-sm text-slate-700">
          <li>✅ 核心技能与技术栈</li>
          <li>✅ 职业发展轨迹</li>
          <li>✅ 项目贡献与影响力</li>
        </ul>
      </aside>
    </section>
  </main>
</template>
