<!--
  流程步骤条：仅负责展示当前进度。
  当传入 routes + navigable 时，可点击已到达的步骤进行跳转（见 utils/navigation.ts）。
-->
<template>
  <view class="steps" :style="{ gridTemplateColumns: `repeat(${steps.length}, 1fr)` }">
    <view
      v-for="(step, index) in steps"
      :key="step"
      class="step"
      :class="{
        active: index <= activeIndex,
        current: index === activeIndex,
        navigable: navigable && !!routes?.length,
      }"
      @tap="onStepTap(index)"
    >
      <view class="dot">{{ index + 1 }}</view>
      <text class="label">{{ step }}</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { navigateFlowStep } from '../utils/navigation';

const props = defineProps<{
  steps: string[];
  activeIndex: number;
  routes?: readonly string[];
  navigable?: boolean;
}>();

function onStepTap(index: number) {
  if (!props.navigable || !props.routes?.length) return;
  navigateFlowStep(props.routes, index, props.activeIndex);
}
</script>

<style lang="scss" scoped>
// 列数由模板内联 style 动态设置，此处不写死 repeat(4)
.steps {
  display: grid;
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
.current .dot {
  box-shadow: 0 0 0 4rpx rgba(0, 74, 198, 0.16);
}
.step.navigable {
  cursor: pointer;
}
</style>
