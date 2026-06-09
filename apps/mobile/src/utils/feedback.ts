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

export function simulateDelay(ms = 1200): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

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
