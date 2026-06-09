<script setup lang="ts">
defineProps<{ steps: string[]; current: number }>();
</script>

<template>
  <div class="flex flex-wrap items-center gap-2 text-xs md:text-sm">
    <template v-for="(step, index) in steps" :key="step">
      <div class="flex items-center gap-2">
        <span :class="['grid h-7 w-7 place-items-center rounded-full font-semibold', index <= current ? 'bg-[#004ac6] text-white' : 'bg-blue-50 text-slate-500']">{{ index + 1 }}</span>
        <span :class="index <= current ? 'font-semibold text-[#004ac6]' : 'text-slate-500'">{{ step }}</span>
      </div>
      <div v-if="index < steps.length - 1" class="hidden h-px w-10 bg-blue-100 md:block" />
    </template>
  </div>
</template>
