<template>
  <view class="info-list">
    <view v-for="item in items" :key="item" class="info-item">
      <text class="bullet">•</text>
      <text class="content">{{ item }}</text>
    </view>
  </view>
</template>

<script setup lang="ts">
defineProps<{ items: string[] }>();
</script>

<style lang="scss" scoped>
.info-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}
.info-item {
  display: flex;
  gap: 14rpx;
  color: #344054;
  font-size: 27rpx;
  line-height: 1.65;
}
.bullet {
  color: #004ac6;
  font-weight: 900;
}
.content {
  flex: 1;
}
</style>
