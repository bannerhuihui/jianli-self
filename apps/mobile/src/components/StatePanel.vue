<template>
  <view class="state-panel" :class="tone">
    <AppIcon v-if="icon" :name="icon" :size="iconSize || 28" :color="iconColor" />
    <text class="state-title">{{ title }}</text>
    <text v-if="description" class="state-desc">{{ description }}</text>
    <slot />
  </view>
</template>

<script setup lang="ts">
import AppIcon from './AppIcon.vue';

withDefaults(defineProps<{
  tone?: 'info' | 'loading' | 'error' | 'empty' | 'warning';
  title: string;
  description?: string;
  icon?: string;
  iconColor?: string;
  iconSize?: number;
}>(), {
  tone: 'info',
  iconColor: '#004ac6',
});
</script>

<style lang="scss" scoped>
.state-panel {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 18rpx;
  border-radius: 24rpx;
  padding: 40rpx 32rpx;
  text-align: center;
}
.state-panel.info { border: 2rpx solid #dbe1ff; background: rgba(219,225,255,0.28); }
.state-panel.loading { border: 2rpx solid #c3c6d7; background: #fff; }
.state-panel.error { border: 2rpx solid rgba(186,26,26,0.24); background: rgba(255,218,214,0.28); }
.state-panel.empty { border: 2rpx dashed #c3c6d7; background: #f8f9ff; }
.state-panel.warning { border: 2rpx solid #fde68a; background: #fffbeb; }
.state-title { color: #0b1c30; font-size: 30rpx; font-weight: 900; }
.state-desc { color: #565e74; font-size: 26rpx; line-height: 1.65; }
</style>
