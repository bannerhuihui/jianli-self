import { getApiBaseUrl } from './config';
import { ApiClientError } from './errors';
import { getAccessToken } from './storage';
import type { ApiEnvelope, ApiErrorEnvelope } from './types';

type RequestOptions = {
  method?: string;
  body?: unknown;
  auth?: boolean;
};

export async function apiRequest<T>(path: string, options: RequestOptions = {}): Promise<T> {
  const headers: Record<string, string> = {
    Accept: 'application/json',
    'X-Client-Platform': 'h5',
  };

  if (options.body !== undefined) {
    headers['Content-Type'] = 'application/json';
  }

  if (options.auth !== false) {
    const token = getAccessToken();
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
  }

  const response = await fetch(`${getApiBaseUrl()}${path}`, {
    method: options.method ?? (options.body !== undefined ? 'POST' : 'GET'),
    headers,
    body: options.body !== undefined ? JSON.stringify(options.body) : undefined,
  });

  if (response.status === 204) {
    return undefined as T;
  }

  const text = await response.text();
  const payload = text ? JSON.parse(text) as ApiEnvelope<T> | ApiErrorEnvelope : null;

  if (!response.ok) {
    const errorBody = payload as ApiErrorEnvelope | null;
    throw new ApiClientError(
      errorBody?.error?.code ?? 'HTTP_ERROR',
      errorBody?.error?.message ?? `请求失败 (${response.status})`,
      errorBody?.error?.details,
    );
  }

  return (payload as ApiEnvelope<T>).data;
}

export function sleep(ms: number): Promise<void> {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
