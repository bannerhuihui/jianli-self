<template>
  <view class="top-nav">
    <view class="nav-inner">
      <view class="nav-left">
        <text class="brand-title">{{ title || 'AI Talent Agent' }}</text>
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
      <view v-if="showActions" class="nav-actions desktop-only">
        <view class="action-icon"><AppIcon name="notifications" :size="24" color="currentColor" /></view>
        <view class="action-icon"><AppIcon name="settings" :size="24" color="currentColor" /></view>
        <image class="user-avatar" src="https://lh3.googleusercontent.com/aida/AP1WRLu30TAtDshHijIOAoQ0uMx6rISLB5HBhsRBmYwnoecXp4U_My3590PLluvURJ1MN4KaMpTgJvq8XvlHOsrWnVcb6gqO7PQy1ekAOuJSZG_S4SX-zPn8fTkyRbgiawKLuyS8o6w0Nv_s9DwTvc1vno23ghD2SXqLvSnzuQDRuVVwAx6xoX8puBhkbumIe7qN8KZShafTJ2kUh3RW2pTFJwCfBMR-phVm5ux0pU3xMWg-dcqSiho0lf3IYSY" mode="aspectFill" />
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import AppIcon from './AppIcon.vue';

type NavItem = string | {
  label: string;
  url: string;
};

const props = withDefaults(defineProps<{
  title?: string;
  active?: string;
  items?: NavItem[];
  showActions?: boolean;
}>(), {
  items: () => [
    { label: '首页', url: '/pages/index/index' },
    { label: '求职者流程', url: '/pages/candidate/upload/index' },
    { label: 'HR 工作台', url: '/pages/hr/job/index' },
  ],
  showActions: true,
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
  z-index: 50;
  border-bottom: 2rpx solid #c3c6d7;
  background: #ffffff;
}
.nav-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  max-width: 1440px;
  min-height: 128rpx;
  margin: 0 auto;
  padding: 0 32rpx;
  box-sizing: border-box;
}
.nav-left {
  display: flex;
  align-items: center;
  gap: 64rpx;
  min-width: 0;
}
.brand-title {
  flex-shrink: 0;
  color: #004ac6;
  font-size: 40rpx;
  font-weight: 700;
  line-height: 1.4;
}
.nav-links {
  gap: 48rpx;
  align-items: center;
}
.nav-link {
  color: #434655;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
  padding: 0 0 8rpx;
  border-bottom: 4rpx solid transparent;
  text-decoration: none;
}
.nav-link.active {
  color: #004ac6;
  font-weight: 700;
  border-bottom-color: #004ac6;
}
.nav-actions {
  display: flex;
  align-items: center;
  gap: 32rpx;
  flex-shrink: 0;
}
.action-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48rpx;
  height: 48rpx;
  color: #434655;
  transition: color 0.2s ease;
  cursor: pointer;
}
.action-icon:hover { color: #004ac6; }
.user-avatar {
  width: 64rpx;
  height: 64rpx;
  border-radius: 999rpx;
  border: 2rpx solid #c3c6d7;
  background: #e5eeff;
}

@media (min-width: 768px) {
  .nav-inner {
    min-height: 64px;
    padding: 0 80px;
  }
  .nav-left { gap: 64px; }
  .brand-title { font-size: 20px; line-height: 28px; }
  .nav-links { gap: 48px; }
  .nav-link {
    font-size: 14px;
    line-height: 16px;
    padding-bottom: 4px;
    border-bottom-width: 2px;
  }
  .nav-actions { gap: 16px; }
  .user-avatar { width: 32px; height: 32px; }
}
</style>
