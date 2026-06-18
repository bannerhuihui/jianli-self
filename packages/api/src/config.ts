const DEFAULT_API_BASE_URL = 'http://localhost:8080/api/v1';

export function getApiBaseUrl(): string {
  const fromEnv = typeof import.meta !== 'undefined'
    ? (import.meta as ImportMeta & { env?: Record<string, string> }).env?.VITE_API_BASE_URL
    : undefined;
  return fromEnv || DEFAULT_API_BASE_URL;
}
