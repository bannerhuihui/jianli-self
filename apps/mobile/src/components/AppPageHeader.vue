<template>
  <view class="page-header">
    <view class="header-top">
      <text v-if="eyebrow" class="eyebrow">{{ eyebrow }}</text>
      <text v-if="meta" class="meta">{{ meta }}</text>
    </view>
    <text class="title">{{ title }}</text>
    <text v-if="description" class="description">{{ description }}</text>
  </view>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  eyebrow?: string;
  meta?: string;
  description?: string;
}>();
</script>

<style lang="scss" scoped>
.page-header {
  display: flex;
  flex-direction: column;
  gap: 18rpx;
  padding: 16rpx 0 10rpx;
}
.header-top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20rpx;
}
.eyebrow {
  align-self: flex-start;
  border-radius: 999rpx;
  padding: 8rpx 18rpx;
  background: #eaf1ff;
  color: #004ac6;
  font-size: 24rpx;
  font-weight: 800;
}
.meta {
  color: #7a8399;
  font-size: 24rpx;
}
.title {
  font-size: 48rpx;
  font-weight: 900;
  line-height: 1.18;
  color: #0b1c30;
}
.description {
  color: #565e74;
  font-size: 28rpx;
  line-height: 1.7;
}
@media (min-width: 768px) {
  .title { font-size: 56rpx; }
  .description { max-width: 960rpx; }
}
</style>
