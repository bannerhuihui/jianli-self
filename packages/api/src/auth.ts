import { apiRequest } from './http';
import {
  getAccessToken,
  getOrCreateDeviceId,
  setAccessToken,
  setJourneyId,
  setRefreshToken,
} from './storage';
import type { SessionData, SessionUser } from './types';

export async function createSession(): Promise<SessionData> {
  const session = await apiRequest<SessionData>('/auth/session', {
    method: 'POST',
    auth: false,
    body: {
      authProvider: 'anonymous',
      deviceId: getOrCreateDeviceId(),
      platform: 'h5',
    },
  });

  setAccessToken(session.accessToken);
  setRefreshToken(session.refreshToken);
  if (session.user.activeJourneyId) {
    setJourneyId(session.user.activeJourneyId);
  }
  return session;
}

export async function ensureSession(): Promise<SessionUser> {
  if (getAccessToken()) {
    try {
      return await apiRequest<SessionUser>('/auth/me');
    } catch {
      const session = await createSession();
      return session.user;
    }
  }
  const session = await createSession();
  return session.user;
}
