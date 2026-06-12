/**
 * 流程步骤间导航。
 * - 回退步骤使用 redirectTo，避免页面栈过深
 * - 前进步骤使用 navigateTo，保留返回手势
 */
export function navigateFlowStep(
  routes: readonly string[],
  targetIndex: number,
  currentIndex: number,
  query?: Record<string, string>,
) {
  if (targetIndex === currentIndex || targetIndex < 0 || targetIndex >= routes.length) return;

  const base = routes[targetIndex];
  const queryString = query
    ? `?${Object.entries(query)
        .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
        .join('&')}`
    : '';
  const url = `${base}${queryString}`;

  if (targetIndex < currentIndex) {
    uni.redirectTo({
      url,
      fail: () => uni.navigateTo({ url }),
    });
    return;
  }

  uni.navigateTo({ url });
}
