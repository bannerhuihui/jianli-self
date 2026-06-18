<template>
  <view class="processing-overlay">
    <view class="processing-modal">
      <view class="scan-card">
        <view class="scan-line" />
        <view class="scan-skeleton">
          <view class="scan-bar w-75" />
          <view class="scan-bar w-full" />
          <view class="scan-bar w-50" />
          <view class="scan-bar w-85" />
          <view class="scan-bar w-66" />
        </view>
      </view>
      <text class="processing-title">{{ title }}</text>
      <text class="processing-desc">{{ description }}</text>
      <view class="processing-track"><view class="processing-bar" /></view>
      <text v-if="tag" class="processing-tag">{{ tag }}</text>
    </view>
  </view>
</template>

<script setup lang="ts">
withDefaults(
  defineProps<{
    title?: string;
    description?: string;
    tag?: string;
  }>(),
  {
    title: 'AI 正在处理中...',
    description: '请稍候，我们正在为你处理数据。',
    tag: '',
  },
);
</script>

<style lang="scss" scoped>
.processing-overlay {
  position: fixed;
  inset: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32rpx;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(12px);
}
.processing-modal {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 720rpx;
  padding: 80rpx 64rpx;
  border-radius: 32rpx;
  background: #fff;
  box-shadow: 0 24rpx 64rpx rgba(15, 23, 42, 0.2);
  text-align: center;
}
.scan-card {
  position: relative;
  overflow: hidden;
  width: 192rpx;
  height: 256rpx;
  margin-bottom: 64rpx;
  border: 4rpx solid #dbe1ff;
  border-radius: 16rpx;
}
.scan-line {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  height: 4rpx;
  background: linear-gradient(90deg, transparent, #2563eb, transparent);
  animation: scan 3s linear infinite;
}
@keyframes scan {
  0% { transform: translateY(0); }
  100% { transform: translateY(256rpx); }
}
.scan-skeleton {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  padding: 16rpx;
  opacity: 0.3;
}
.scan-bar { height: 8rpx; border-radius: 999rpx; background: #c3c6d7; }
.scan-bar.w-75 { width: 75%; }
.scan-bar.w-full { width: 100%; }
.scan-bar.w-50 { width: 50%; }
.scan-bar.w-85 { width: 83%; }
.scan-bar.w-66 { width: 66%; }
.processing-title { color: #0b1c30; font-size: 40rpx; font-weight: 600; line-height: 1.4; }
.processing-desc { margin-top: 16rpx; color: #434655; font-size: 32rpx; line-height: 1.5; }
.processing-track { width: 100%; height: 6rpx; margin-top: 48rpx; overflow: hidden; border-radius: 999rpx; background: #e5eeff; }
.processing-bar { width: 65%; height: 100%; background: #004ac6; animation: processing-pulse 1.6s ease-in-out infinite alternate; }
@keyframes processing-pulse { from { width: 35%; } to { width: 85%; } }
.processing-tag { margin-top: 32rpx; color: #004ac6; font-size: 24rpx; font-weight: 500; letter-spacing: 0.12em; text-transform: uppercase; }
</style>
