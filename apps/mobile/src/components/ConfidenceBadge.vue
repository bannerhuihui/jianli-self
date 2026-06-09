<template>
  <AppTag :label="label" :tone="tone" />
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { Confidence } from '@ai-talent-agent/domain';
import AppTag from './AppTag.vue';

const props = defineProps<{ confidence: Confidence }>();

const label = computed(() => {
  if (props.confidence === 'high') return '高置信度';
  if (props.confidence === 'medium') return '中等置信度';
  return '需验证';
});

const tone = computed(() => {
  if (props.confidence === 'high') return 'green';
  if (props.confidence === 'medium') return 'blue';
  return 'amber';
});
</script>
