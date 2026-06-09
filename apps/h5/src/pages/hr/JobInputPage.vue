<template>
  <main class="mx-auto max-w-7xl px-4 py-8 md:px-8">
    <h1 class="text-2xl font-bold">输入岗位需求</h1>
    <p class="mt-2 text-slate-600">请在下方描述您的岗位需求，AI 将基于输入自动解析并生成专业岗位画像。</p>
    <section class="mt-8 grid gap-6 lg:grid-cols-[1fr_320px]">
      <div class="rounded-3xl bg-white p-6 shadow-sm">
        <div class="mb-3 flex justify-between"><h2 class="font-bold">需求详细描述</h2><button class="text-sm text-[#004ac6]">清空内容</button></div>
        <textarea class="h-80 w-full rounded-2xl border border-blue-100 bg-blue-50/40 p-5 leading-7" placeholder="请输入详细的岗位需求，例如：高级前端开发工程师，5-8年经验，熟悉 React、Node.js、TypeScript，负责 SaaS 平台核心业务开发..." />
        <div class="mt-4 flex flex-wrap gap-2"><button class="rounded-full bg-blue-50 px-4 py-2 text-sm">高级前端工程师</button><button class="rounded-full bg-blue-50 px-4 py-2 text-sm">资深产品经理</button><button class="rounded-full bg-blue-50 px-4 py-2 text-sm">Java 开发专家</button></div>
        <RouterLink to="/hr/job/profile" class="mt-6 inline-flex rounded-xl bg-[#004ac6] px-6 py-3 font-semibold text-white">生成岗位画像</RouterLink>
      </div>
      <aside class="space-y-5"><div class="rounded-3xl bg-blue-50 p-6"><h2 class="font-bold">填写小贴士</h2><ol class="mt-4 space-y-3 text-sm leading-6 text-slate-700"><li>1. 尽可能包含年限、技能、职责、软素质。</li><li>2. 可直接粘贴现有 JD，AI 会自动整理。</li><li>3. 不确定的地方可以留空，后续再补充。</li></ol></div><div class="rounded-3xl border border-dashed border-blue-200 p-6 text-center text-slate-500">画像预览区</div></aside>
    </section>
  </main>
</template>
