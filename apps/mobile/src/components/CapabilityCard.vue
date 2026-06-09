<template>
  <AppCard class="capability-card">
    <view class="capability-head">
      <text class="name">{{ name }}</text>
      <ConfidenceBadge :confidence="confidence" />
    </view>
    <view class="score-row">
      <text class="score">{{ score.toFixed(1) }}</text>
      <text class="unit">/ 5</text>
    </view>
    <view class="bar"><view class="bar-inner" :style="{ width: `${Math.min(score / 5 * 100, 100)}%` }" /></view>
    <text class="reason">{{ reason }}</text>
  </AppCard>
</template>

<script setup lang="ts">
import type { Confidence } from '@ai-talent-agent/domain';
import AppCard from './AppCard.vue';
import ConfidenceBadge from './ConfidenceBadge.vue';

defineProps<{
  name: string;
  score: number;
  confidence: Confidence;
  reason: string;
}>();
</script>

<style lang="scss" scoped>
.capability-card { min-height: 250rpx; }
.capability-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16rpx;
}
.name {
  font-size: 30rpx;
  font-weight: 900;
  color: #0b1c30;
}
.score-row {
  display: flex;
  align-items: baseline;
  gap: 8rpx;
}
.score {
  color: #004ac6;
  font-size: 56rpx;
  font-weight: 900;
}
.unit {
  color: #7a8399;
  font-size: 24rpx;
}
.bar {
  overflow: hidden;
  height: 12rpx;
  border-radius: 999rpx;
  background: #e6ecf6;
}
.bar-inner {
  height: 100%;
  border-radius: 999rpx;
  background: linear-gradient(90deg, #004ac6, #5b7cfa);
}
.reason {
  color: #565e74;
  font-size: 26rpx;
  line-height: 1.6;
}
</style>
