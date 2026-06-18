import { getApiBaseUrl } from './config';
import { ApiClientError } from './errors';
import { apiRequest } from './http';
import { getAccessToken, getJourneyId, setJourneyId } from './storage';
import type {
  ApiStructuredResume,
  ApiTalentProfile,
  InterviewSession,
  InterviewTurnResponse,
  ResumeFileUpload,
  ResumeVersion,
  ResumeVersionKey,
  StructuredResumePatch,
  TaskAccepted,
} from './types';

interface JourneySummary {
  id: string;
  userId: string;
  status: string;
  currentStep: string;
}

export async function createJourney(): Promise<JourneySummary> {
  const journey = await apiRequest<JourneySummary>('/journeys', { method: 'POST' });
  setJourneyId(journey.id);
  return journey;
}

export async function getActiveJourney(): Promise<JourneySummary> {
  return apiRequest<JourneySummary>('/journeys/active');
}

export async function ensureActiveJourneyId(): Promise<string> {
  const cached = getJourneyId();
  if (cached) {
    return cached;
  }

  try {
    const journey = await getActiveJourney();
    setJourneyId(journey.id);
    return journey.id;
  } catch (error) {
    if (error instanceof ApiClientError && error.code === 'JOURNEY_NOT_FOUND') {
      const journey = await createJourney();
      return journey.id;
    }
    throw error;
  }
}

export function uploadResumeFile(
  journeyId: string,
  filePath: string,
  onProgress?: (percent: number) => void,
): Promise<ResumeFileUpload> {
  return new Promise((resolve, reject) => {
    const token = getAccessToken();
    const uploadTask = uni.uploadFile({
      url: `${getApiBaseUrl()}/journeys/${journeyId}/resume-files`,
      filePath,
      name: 'file',
      header: {
        Authorization: `Bearer ${token}`,
        'X-Client-Platform': 'h5',
      },
      success: (res) => {
        if (res.statusCode < 200 || res.statusCode >= 300) {
          try {
            const body = JSON.parse(res.data) as { error?: { code?: string; message?: string } };
            reject(new ApiClientError(
              body.error?.code ?? 'UPLOAD_FAILED',
              body.error?.message ?? '上传失败',
            ));
          } catch {
            reject(new ApiClientError('UPLOAD_FAILED', '上传失败'));
          }
          return;
        }
        const body = JSON.parse(res.data) as { data: ResumeFileUpload };
        resolve(body.data);
      },
      fail: (err) => {
        reject(new ApiClientError('UPLOAD_FAILED', err.errMsg || '上传失败'));
      },
    });

    if (onProgress) {
      uploadTask.onProgressUpdate((event) => onProgress(event.progress));
    }
  });
}

export async function startParseResume(journeyId: string, fileId?: string): Promise<TaskAccepted> {
  return apiRequest<TaskAccepted>(`/journeys/${journeyId}/parse-resume`, {
    method: 'POST',
    body: fileId ? { fileId } : {},
  });
}

export async function getStructuredResume(journeyId: string): Promise<ApiStructuredResume> {
  return apiRequest<ApiStructuredResume>(`/journeys/${journeyId}/structured-resume`);
}

export async function getResumeFile(journeyId: string): Promise<ResumeFileUpload> {
  return apiRequest<ResumeFileUpload>(`/journeys/${journeyId}/resume-file`);
}

export async function patchStructuredResume(
  journeyId: string,
  patch: StructuredResumePatch,
): Promise<ApiStructuredResume> {
  return apiRequest<ApiStructuredResume>(`/journeys/${journeyId}/structured-resume`, {
    method: 'PATCH',
    body: patch,
  });
}

export async function confirmStructuredResume(journeyId: string): Promise<{
  interviewSessionId: string;
  stage: string;
  journeyStatus: string;
}> {
  return apiRequest(`/journeys/${journeyId}/structured-resume/confirm`, { method: 'POST' });
}

export async function getInterviewSession(journeyId: string): Promise<InterviewSession> {
  return apiRequest<InterviewSession>(`/journeys/${journeyId}/interview`);
}

export async function submitInterviewTurn(journeyId: string, content: string): Promise<InterviewTurnResponse> {
  return apiRequest<InterviewTurnResponse>(`/journeys/${journeyId}/interview/turns`, {
    method: 'POST',
    body: { content },
  });
}

export async function skipInterviewQuestion(journeyId: string): Promise<InterviewSession> {
  return apiRequest<InterviewSession>(`/journeys/${journeyId}/interview/skip`, { method: 'POST' });
}

export async function completeInterview(journeyId: string): Promise<{ journeyId: string; status: string }> {
  return apiRequest<{ journeyId: string; status: string }>(`/journeys/${journeyId}/interview/complete`, { method: 'POST' });
}

export async function startProfileGeneration(journeyId: string): Promise<TaskAccepted> {
  return apiRequest<TaskAccepted>(`/journeys/${journeyId}/profile/generate`, { method: 'POST' });
}

export async function getTalentProfile(journeyId: string): Promise<ApiTalentProfile> {
  return apiRequest<ApiTalentProfile>(`/journeys/${journeyId}/profile`);
}

export async function listResumeVersions(journeyId: string): Promise<ResumeVersion[]> {
  return apiRequest<ResumeVersion[]>(`/journeys/${journeyId}/resume-versions`);
}

export async function generateResumeVersion(
  journeyId: string,
  versionKey: ResumeVersionKey,
): Promise<TaskAccepted> {
  return apiRequest<TaskAccepted>(`/journeys/${journeyId}/resume-versions`, {
    method: 'POST',
    body: { versionKey },
  });
}

export async function getResumeVersion(
  journeyId: string,
  versionKey: ResumeVersionKey,
): Promise<ResumeVersion> {
  return apiRequest<ResumeVersion>(`/journeys/${journeyId}/resume-versions/${versionKey}`);
}

export async function exportResumeVersion(
  journeyId: string,
  versionKey: ResumeVersionKey,
  format: 'pdf' | 'docx',
): Promise<TaskAccepted> {
  return apiRequest<TaskAccepted>(`/journeys/${journeyId}/resume-versions/${versionKey}/export`, {
    method: 'POST',
    body: { format },
  });
}
