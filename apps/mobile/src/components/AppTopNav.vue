<template>
  <view class="top-nav">
    <view class="brand">
      <text class="brand-title">{{ title || 'AI Talent Agent' }}</text>
      <text v-if="subtitle" class="brand-subtitle">{{ subtitle }}</text>
    </view>
    <view class="nav-links desktop-only">
      <navigator
        v-for="item in normalizedItems"
        :key="item.label"
        :url="item.url"
        class="nav-link"
        :class="{ active: item.label === active }"
      >
        {{ item.label }}
      </navigator>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed } from 'vue';

type NavItem = string | {
  label: string;
  url: string;
};

const props = withDefaults(defineProps<{
  title?: string;
  subtitle?: string;
  active?: string;
  items?: NavItem[];
}>(), {
  items: () => [
    { label: '首页', url: '/pages/index/index' },
    { label: '求职者流程', url: '/pages/candidate/upload/index' },
    { label: 'HR 工作台', url: '/pages/hr/job/index' },
  ],
});

const normalizedItems = computed(() => props.items.map((item) => {
  if (typeof item === 'string') {
    return { label: item, url: '/pages/index/index' };
  }
  return item;
}));
</script>

<style lang="scss" scoped>
.top-nav {
  position: sticky;
  top: 0;
  z-index: 20;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 32rpx;
  min-height: 92rpx;
  padding: 0 32rpx;
  border-bottom: 2rpx solid #c3c6d7;
  background: rgba(255, 255, 255, 0.94);
}
.brand { display: flex; flex-direction: column; gap: 6rpx; flex-shrink: 0; }
.brand-title { color: #004ac6; font-size: 34rpx; font-weight: 900; letter-spacing: -0.5rpx; }
.brand-subtitle { color: #565e74; font-size: 24rpx; }
.nav-links { flex: 1; justify-content: center; gap: 52rpx; }
.nav-link { color: #343849; font-size: 30rpx; font-weight: 900; padding: 32rpx 0 26rpx; border-bottom: 5rpx solid transparent; text-decoration: none; }
.nav-link.active { color: #004ac6; border-bottom-color: #004ac6; }
@media (min-width: 768px) {
  .top-nav { min-height: 78px; padding: 0 48px; gap: 72px; }
  .brand-title { font-size: 28px; }
  .nav-links { justify-content: flex-start; gap: 48px; }
  .nav-link { font-size: 20px; padding: 28px 0 22px; border-bottom-width: 4px; }
}
</style>
