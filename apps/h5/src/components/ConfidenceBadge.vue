<script setup lang="ts">
const props = defineProps<{ confidence: 'high' | 'medium' | 'low' }>();
const label = { high: '高置信度', medium: '中等置信度', low: '低置信度' }[props.confidence];
const klass = { high: 'bg-emerald-50 text-emerald-700', medium: 'bg-indigo-50 text-indigo-700', low: 'bg-amber-50 text-amber-700' }[props.confidence];
</script>

<template><span :class="['rounded-full px-2.5 py-1 text-xs font-semibold', klass]">{{ label }}</span></template>
