/**
 * 用户反馈工具：Toast、剪贴板、模拟异步与统一 loading 包装。
 * MVP 阶段用于演示交互闭环，后续可替换为真实 API 调用。
 */

export function showToast(title: string, icon: 'success' | 'error' | 'none' = 'none') {
  uni.showToast({ title, icon, duration: 2000 });
}

export function copyText(text: string, successMessage = '已复制到剪贴板') {
  uni.setClipboardData({
    data: text,
    success: () => showToast(successMessage, 'success'),
    fail: () => showToast('复制失败，请稍后重试', 'error'),
  });
}

/** 模拟网络/生成耗时，便于演示 loading 状态。 */
export function simulateDelay(ms = 1200): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

/**
 * 包装异步任务：自动 showLoading / hideLoading，并按结果弹出提示。
 * @returns 是否执行成功
 */
export async function runAsyncAction(
  task: () => Promise<void>,
  options: { loading?: string; success?: string; fail?: string },
): Promise<boolean> {
  if (options.loading) {
    uni.showLoading({ title: options.loading, mask: true });
  }
  try {
    await task();
    if (options.loading) uni.hideLoading();
    if (options.success) showToast(options.success, 'success');
    return true;
  } catch {
    if (options.loading) uni.hideLoading();
    showToast(options.fail || '操作失败，请稍后重试', 'error');
    return false;
  }
}
