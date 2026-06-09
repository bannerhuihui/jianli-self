<template>
  <main class="mx-auto max-w-7xl px-4 py-10 md:px-8">
    <section class="grid gap-8 rounded-3xl bg-gradient-to-br from-blue-50 to-white p-8 md:grid-cols-2 md:p-12">
      <div class="space-y-6">
        <p class="inline-flex rounded-full bg-blue-100 px-3 py-1 text-sm font-semibold text-[#004ac6]">下一代 AI 招聘基础设施</p>
        <h1 class="text-3xl font-bold leading-tight md:text-5xl">AI 先理解候选人，再生成简历和岗位匹配</h1>
        <p class="text-base leading-7 text-slate-600 md:text-lg">通过简历解析、AI 深度访谈和人才画像，输出可解释的简历版本、推荐理由、风险点和面试问题。</p>
        <div class="flex flex-col gap-3 sm:flex-row">
          <RouterLink to="/candidate/upload" class="rounded-xl bg-[#004ac6] px-5 py-3 text-center font-semibold text-white">立即开始</RouterLink>
          <RouterLink to="/hr/job/new" class="rounded-xl border border-blue-200 bg-white px-5 py-3 text-center font-semibold text-[#004ac6]">查看 HR 工作台</RouterLink>
        </div>
      </div>
      <div class="rounded-2xl border border-blue-100 bg-white p-6 shadow-lg">
        <div class="mb-4 h-3 w-24 rounded-full bg-blue-100" />
        <div class="space-y-3">
          <div class="h-12 rounded-xl bg-blue-50" />
          <div class="grid grid-cols-3 gap-3">
            <div class="rounded-xl bg-emerald-50 p-4 text-center text-sm font-semibold text-emerald-700">人才画像</div>
            <div class="rounded-xl bg-blue-50 p-4 text-center text-sm font-semibold text-blue-700">证据链</div>
            <div class="rounded-xl bg-amber-50 p-4 text-center text-sm font-semibold text-amber-700">待验证</div>
          </div>
        </div>
      </div>
    </section>
    <section class="mt-10 grid gap-4 md:grid-cols-4">
      <div class="rounded-2xl bg-white p-5 shadow-sm"><h3 class="font-bold">简历深度解析</h3><p class="mt-2 text-sm text-slate-600">提取教育、工作、项目和技能。</p></div>
      <div class="rounded-2xl bg-white p-5 shadow-sm"><h3 class="font-bold">AI 深度访谈</h3><p class="mt-2 text-sm text-slate-600">围绕经历追问，补全画像证据。</p></div>
      <div class="rounded-2xl bg-white p-5 shadow-sm"><h3 class="font-bold">人才画像</h3><p class="mt-2 text-sm text-slate-600">8 个能力维度，结论可追溯。</p></div>
      <div class="rounded-2xl bg-white p-5 shadow-sm"><h3 class="font-bold">HR 兼容输出</h3><p class="mt-2 text-sm text-slate-600">摘要、Excel、邮件、企微均可用。</p></div>
    </section>
  </main>
</template>
