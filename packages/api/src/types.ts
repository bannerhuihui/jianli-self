import type { Candidate, StructuredResume } from '@ai-talent-agent/domain';

export interface ApiEnvelope<T> {
  data: T;
  requestId: string;
  timestamp: string;
}

export interface ApiErrorEnvelope {
  error: {
    code: string;
    message: string;
    details?: Record<string, unknown>;
  };
  requestId: string;
  timestamp: string;
}

export interface SessionUser {
  id: string;
  role: string;
  authProvider: string;
  entitlements: string[];
  activeJourneyId: string | null;
}

export interface SessionData {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
  user: SessionUser;
}

export interface ResumeFileUpload {
  fileId: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  uploadedAt: string;
  fileUrl?: string | null;
}

export interface TaskAccepted {
  taskId: string;
  status: string;
}

export interface InterviewTurn {
  id: string;
  role: 'agent' | 'user';
  content: string;
  questionReason?: string;
  targetCapabilities?: string[];
  createdAt?: string;
}

export interface InterviewSession {
  id: string;
  journeyId: string;
  stage: string;
  status: string;
  turns: InterviewTurn[];
  missingEvidence: string[];
  canGenerateProfile: boolean;
  progress: number;
}

export interface InterviewTurnResponse {
  userTurn: InterviewTurn;
  agentTurn: InterviewTurn;
  missingEvidence: string[];
  canGenerateProfile: boolean;
  stage: string;
}

export interface ProfileEvidence {
  id: string;
  source: string;
  snippet: string;
  capabilityKeys: string[];
}

export interface ProfileCapability {
  key: string;
  name: string;
  score: number;
  confidence: 'high' | 'medium' | 'low';
  reason: string;
  evidenceIds?: string[];
}

export interface ApiTalentProfile {
  id: string;
  journeyId: string;
  candidate: Candidate;
  summary: string;
  overallScore: number;
  capabilities: ProfileCapability[];
  strengths: string[];
  risks: string[];
  preferences: string[];
  recommendedRoles: string[];
  confidence?: 'high' | 'medium' | 'low';
  evidence?: ProfileEvidence[];
  generatedAt?: string;
}

export interface AsyncTask {
  id: string;
  journeyId: string;
  type: string;
  status: 'pending' | 'running' | 'succeeded' | 'failed' | 'cancelled';
  progress: number;
  result?: Record<string, unknown> | null;
  error?: { code?: string; message?: string } | null;
  createdAt: string;
  updatedAt: string;
  completedAt?: string | null;
}

export interface ApiStructuredResume extends StructuredResume {
  id: string;
  journeyId: string;
  parseQualityScore?: number;
  confidence?: string;
  warnings?: string[];
  missingFields?: string[];
  updatedAt?: string;
}

export type ResumeVersionKey = 'ats' | 'hr' | 'platform' | 'email';

export interface ResumeVersion {
  id: string;
  journeyId: string;
  versionKey: ResumeVersionKey;
  title: string;
  content: string;
  contentFormat: 'plain' | 'markdown';
  confidence: 'high' | 'medium' | 'low';
  warnings: string[];
  usedEvidenceIds: string[];
  generatedAt: string;
}

export interface ResumeExportResult {
  downloadUrl: string;
  expiresAt: string;
  fileName: string;
}

export type { Candidate };
