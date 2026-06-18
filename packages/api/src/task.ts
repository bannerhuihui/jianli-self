import { ApiClientError } from './errors';
import { apiRequest, sleep } from './http';
import type { AsyncTask } from './types';

export async function getTask(taskId: string): Promise<AsyncTask> {
  return apiRequest<AsyncTask>(`/tasks/${taskId}`);
}

export async function waitForTask(
  taskId: string,
  options?: { intervalMs?: number; timeoutMs?: number },
): Promise<AsyncTask> {
  const intervalMs = options?.intervalMs ?? 1500;
  const timeoutMs = options?.timeoutMs ?? 120000;
  const startedAt = Date.now();

  while (Date.now() - startedAt < timeoutMs) {
    const task = await getTask(taskId);
    if (task.status === 'succeeded' || task.status === 'failed') {
      return task;
    }
    await sleep(intervalMs);
  }

  throw new ApiClientError('TASK_TIMEOUT', '解析超时，请稍后重试');
}
