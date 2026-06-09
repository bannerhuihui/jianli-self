<template>
  <view class="app-card" :class="[`tone-${tone || 'default'}`]">
    <view v-if="title || subtitle" class="card-head">
      <text v-if="title" class="card-title">{{ title }}</text>
      <text v-if="subtitle" class="card-subtitle">{{ subtitle }}</text>
    </view>
    <slot />
  </view>
</template>

<script setup lang="ts">
defineProps<{
  title?: string;
  subtitle?: string;
  tone?: 'default' | 'blue' | 'green' | 'amber' | 'plain';
}>();
</script>

<style lang="scss" scoped>
.app-card {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
  border: 2rpx solid rgba(195, 198, 215, 0.68);
  border-radius: 24rpx;
  padding: 32rpx;
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 8rpx 40rpx rgba(0, 74, 198, 0.08);
}
.card-head {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}
.card-title {
  color: #0b1c30;
  font-size: 32rpx;
  font-weight: 900;
}
.card-subtitle {
  color: #565e74;
  font-size: 26rpx;
  line-height: 1.6;
}
.tone-blue { background: linear-gradient(135deg, rgba(255,255,255,0.94) 0%, rgba(229,238,255,0.96) 100%); }
.tone-green { background: linear-gradient(135deg, rgba(255,255,255,0.94) 0%, rgba(236,253,245,0.96) 100%); }
.tone-amber { background: linear-gradient(135deg, rgba(255,255,255,0.94) 0%, rgba(255,247,237,0.98) 100%); border-color: rgba(251, 191, 36, 0.45); }
.tone-plain { box-shadow: none; }
</style>
