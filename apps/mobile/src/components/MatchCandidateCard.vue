<template>
  <AppCard>
    <view class="candidate-head">
      <view class="identity">
        <text class="name">{{ name }}</text>
        <text class="meta">{{ title }} · {{ location }} · {{ experienceYears }} 年</text>
      </view>
      <view class="score-block">
        <text class="score">{{ score }}</text>
        <text class="score-label">匹配分</text>
      </view>
    </view>

    <view class="breakdown">
      <view v-for="item in breakdownItems" :key="item.label" class="breakdown-item">
        <text class="breakdown-value">{{ item.value }}</text>
        <text class="breakdown-label">{{ item.label }}</text>
      </view>
    </view>

    <view class="columns">
      <view>
        <text class="block-title">推荐理由</text>
        <InfoList :items="reasons" />
      </view>
      <view>
        <text class="block-title risk-title">风险点</text>
        <InfoList :items="risks" />
      </view>
    </view>

    <navigator url="/pages/hr/export/index" class="secondary-button">加入导出包</navigator>
  </AppCard>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import AppCard from './AppCard.vue';
import InfoList from './InfoList.vue';

const props = defineProps<{
  name: string;
  title: string;
  location: string;
  experienceYears: number;
  score: number;
  breakdown: {
    experience: number;
    skills: number;
    profile: number;
    careerPreference: number;
  };
  reasons: string[];
  risks: string[];
}>();

const breakdownItems = computed(() => [
  { label: '经验', value: props.breakdown.experience },
  { label: '技能', value: props.breakdown.skills },
  { label: '画像', value: props.breakdown.profile },
  { label: '偏好', value: props.breakdown.careerPreference },
]);
</script>

<style lang="scss" scoped>
.candidate-head {
  display: flex;
  justify-content: space-between;
  gap: 24rpx;
}
.identity { display: flex; flex: 1; flex-direction: column; gap: 10rpx; }
.name { color: #0b1c30; font-size: 36rpx; font-weight: 900; }
.meta { color: #69738a; font-size: 26rpx; line-height: 1.5; }
.score-block { text-align: right; }
.score { display: block; color: #004ac6; font-size: 58rpx; font-weight: 900; line-height: 1; }
.score-label { color: #7a8399; font-size: 22rpx; }
.breakdown { display: grid; grid-template-columns: repeat(4, 1fr); gap: 12rpx; }
.breakdown-item { border-radius: 18rpx; background: #f4f7fb; padding: 18rpx 8rpx; text-align: center; }
.breakdown-value { display: block; color: #0b1c30; font-weight: 900; }
.breakdown-label { color: #7a8399; font-size: 22rpx; }
.columns { display: grid; gap: 22rpx; }
.block-title { display: block; margin-bottom: 12rpx; color: #047857; font-weight: 900; }
.risk-title { color: #b45309; }
@media (min-width: 768px) { .columns { grid-template-columns: repeat(2, 1fr); } }
</style>
