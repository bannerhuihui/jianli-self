<template>
  <view class="app-shell">
    <slot />
  </view>
</template>

<style lang="scss">
page {
  background: #f8f9ff;
  color: #0b1c30;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}
.app-shell {
  min-height: 100vh;
}
</style>
