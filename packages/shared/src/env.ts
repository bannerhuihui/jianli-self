export type RuntimeEnv = 'wechat' | 'wecom' | 'mobile-browser' | 'desktop-browser';

export function isWechatBrowser(userAgent = navigator.userAgent): boolean {
  return /MicroMessenger/i.test(userAgent) && !/wxwork/i.test(userAgent);
}

export function isWeComBrowser(userAgent = navigator.userAgent): boolean {
  return /wxwork/i.test(userAgent);
}

export function isMobileBrowser(userAgent = navigator.userAgent): boolean {
  return /Mobile|Android|iPhone|iPad/i.test(userAgent);
}

export function getRuntimeEnv(userAgent = navigator.userAgent): RuntimeEnv {
  if (isWeComBrowser(userAgent)) return 'wecom';
  if (isWechatBrowser(userAgent)) return 'wechat';
  if (isMobileBrowser(userAgent)) return 'mobile-browser';
  return 'desktop-browser';
}
