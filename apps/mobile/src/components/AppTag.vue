<template>
  <text class="tag" :class="[`tone-${tone || 'blue'}`]">{{ label }}</text>
</template>

<script setup lang="ts">
defineProps<{
  label: string;
  tone?: 'blue' | 'green' | 'amber' | 'gray';
}>();
</script>

<style lang="scss" scoped>
.tag {
  display: inline-flex;
  align-items: center;
  border-radius: 999rpx;
  padding: 8rpx 18rpx;
  font-size: 24rpx;
  font-weight: 800;
}
.tone-blue { background: #eaf1ff; color: #004ac6; }
.tone-green { background: #e8fff4; color: #047857; }
.tone-amber { background: #fff4df; color: #b45309; }
.tone-gray { background: #eef2f7; color: #4b5563; }
</style>
