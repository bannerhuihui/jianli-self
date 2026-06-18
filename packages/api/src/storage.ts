const KEYS = {
  accessToken: 'aita_access_token',
  refreshToken: 'aita_refresh_token',
  journeyId: 'aita_journey_id',
  deviceId: 'aita_device_id',
} as const;

function read(key: string): string {
  if (typeof uni !== 'undefined') {
    return uni.getStorageSync(key) || '';
  }
  if (typeof localStorage !== 'undefined') {
    return localStorage.getItem(key) || '';
  }
  return '';
}

function write(key: string, value: string): void {
  if (typeof uni !== 'undefined') {
    uni.setStorageSync(key, value);
    return;
  }
  if (typeof localStorage !== 'undefined') {
    localStorage.setItem(key, value);
  }
}

export function getAccessToken(): string {
  return read(KEYS.accessToken);
}

export function setAccessToken(token: string): void {
  write(KEYS.accessToken, token);
}

export function getRefreshToken(): string {
  return read(KEYS.refreshToken);
}

export function setRefreshToken(token: string): void {
  write(KEYS.refreshToken, token);
}

export function getJourneyId(): string {
  return read(KEYS.journeyId);
}

export function setJourneyId(journeyId: string): void {
  write(KEYS.journeyId, journeyId);
}

export function getOrCreateDeviceId(): string {
  let deviceId = read(KEYS.deviceId);
  if (!deviceId) {
    const random = typeof crypto !== 'undefined' && 'randomUUID' in crypto
      ? crypto.randomUUID()
      : `${Date.now()}-${Math.random().toString(16).slice(2)}`;
    deviceId = `web-${random}`;
    write(KEYS.deviceId, deviceId);
  }
  return deviceId;
}
