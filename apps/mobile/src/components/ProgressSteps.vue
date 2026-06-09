<template>
  <view class="steps" :style="{ gridTemplateColumns: `repeat(${steps.length}, 1fr)` }">
    <view v-for="(step, index) in steps" :key="step" class="step" :class="{ active: index <= activeIndex }">
      <view class="dot">{{ index + 1 }}</view>
      <text class="label">{{ step }}</text>
    </view>
  </view>
</template>

<script setup lang="ts">
defineProps<{
  steps: string[];
  activeIndex: number;
}>();
</script>

<style lang="scss" scoped>
.steps {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12rpx;
}
.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10rpx;
  color: #9aa3b5;
}
.dot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44rpx;
  height: 44rpx;
  border-radius: 999rpx;
  background: #edf1f7;
  color: #7a8399;
  font-size: 24rpx;
  font-weight: 900;
}
.label {
  font-size: 22rpx;
  text-align: center;
}
.active .dot {
  background: #004ac6;
  color: #fff;
}
.active .label {
  color: #004ac6;
  font-weight: 800;
}
</style>
