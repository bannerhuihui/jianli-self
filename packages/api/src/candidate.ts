import { ensureSession } from './auth';
import {
  ensureActiveJourneyId,
  exportResumeVersion,
  generateResumeVersion,
  getResumeFile,
  getResumeVersion,
  getTalentProfile,
  getStructuredResume,
  listResumeVersions,
  patchStructuredResume,
  startParseResume,
  uploadResumeFile,
} from './journey';
import { waitForTask } from './task';
import { ApiClientError } from './errors';
import type {
  ApiStructuredResume,
  ApiTalentProfile,
  ResumeExportResult,
  ResumeFileUpload,
  ResumeVersion,
  ResumeVersionKey,
  StructuredResumePatch,
} from './types';

export async function prepareCandidateContext(): Promise<{ journeyId: string }> {
  await ensureSession();
  const journeyId = await ensureActiveJourneyId();
  return { journeyId };
}

export async function uploadAndParseResume(
  filePath: string,
  hooks?: {
    onUploadProgress?: (percent: number) => void;
  },
): Promise<ApiStructuredResume> {
  const { journeyId } = await prepareCandidateContext();

  const uploaded = await uploadResumeFile(journeyId, filePath, hooks?.onUploadProgress);
  const accepted = await startParseResume(journeyId, uploaded.fileId);
  const task = await waitForTask(accepted.taskId);

  if (task.status === 'failed') {
    throw new ApiClientError(
      task.error?.code ?? 'TASK_FAILED',
      task.error?.message ?? '简历解析失败',
    );
  }

  return getStructuredResume(journeyId);
}

export async function getStructuredResumeForActiveJourney(): Promise<ApiStructuredResume> {
  const { journeyId } = await prepareCandidateContext();
  return getStructuredResume(journeyId);
}

export async function saveStructuredResumeForActiveJourney(
  patch: StructuredResumePatch,
): Promise<ApiStructuredResume> {
  const { journeyId } = await prepareCandidateContext();
  return patchStructuredResume(journeyId, patch);
}

export async function getResumeFileForActiveJourney(): Promise<ResumeFileUpload | null> {
  const { journeyId } = await prepareCandidateContext();
  try {
    return await getResumeFile(journeyId);
  } catch (error) {
    if (error instanceof ApiClientError && error.code === 'RESUME_FILE_NOT_FOUND') {
      return null;
    }
    throw error;
  }
}

export async function getTalentProfileForActiveJourney(): Promise<ApiTalentProfile> {
  const { journeyId } = await prepareCandidateContext();
  return getTalentProfile(journeyId);
}

async function generateAndFetchResumeVersion(
  journeyId: string,
  versionKey: ResumeVersionKey,
): Promise<ResumeVersion> {
  const accepted = await generateResumeVersion(journeyId, versionKey);
  const task = await waitForTask(accepted.taskId);
  if (task.status === 'failed') {
    throw new ApiClientError(
      task.error?.code ?? 'TASK_FAILED',
      task.error?.message ?? '简历版本生成失败',
    );
  }
  return getResumeVersion(journeyId, versionKey);
}

export async function ensureResumeVersionForActiveJourney(
  versionKey: ResumeVersionKey,
  options?: { regenerate?: boolean },
): Promise<ResumeVersion> {
  const { journeyId } = await prepareCandidateContext();

  if (options?.regenerate) {
    return generateAndFetchResumeVersion(journeyId, versionKey);
  }

  const existing = await listResumeVersions(journeyId);
  const matched = existing.find((version) => version.versionKey === versionKey);
  if (matched) {
    return matched;
  }

  return generateAndFetchResumeVersion(journeyId, versionKey);
}

export async function exportResumeVersionForActiveJourney(
  versionKey: ResumeVersionKey,
  format: 'pdf' | 'docx',
): Promise<ResumeExportResult> {
  const { journeyId } = await prepareCandidateContext();
  const accepted = await exportResumeVersion(journeyId, versionKey, format);
  const task = await waitForTask(accepted.taskId);
  if (task.status === 'failed') {
    throw new ApiClientError(
      task.error?.code ?? 'TASK_FAILED',
      task.error?.message ?? '导出失败',
    );
  }
  return task.result as unknown as ResumeExportResult;
}
