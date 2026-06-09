export type Confidence = 'high' | 'medium' | 'low';

export interface Candidate {
  id: string;
  name: string;
  title: string;
  location: string;
  experienceYears: number;
  education: string;
  phone?: string;
  email?: string;
}

export interface CapabilityScore {
  key: string;
  name: string;
  score: number;
  confidence: Confidence;
  reason: string;
}

export interface TalentProfile {
  candidate: Candidate;
  summary: string;
  overallScore: number;
  capabilities: CapabilityScore[];
  strengths: string[];
  risks: string[];
  preferences: string[];
  recommendedRoles: string[];
}

export interface StructuredResume {
  basicInfo: Candidate;
  education: string[];
  workExperience: string[];
  projects: string[];
  skills: string[];
}

export interface InterviewTurn {
  id: string;
  role: 'agent' | 'user';
  content: string;
  targetCapabilities?: string[];
}

export interface JobProfile {
  id: string;
  title: string;
  seniority: string;
  responsibilities: string[];
  requiredSkills: string[];
  bonusSkills: string[];
  requiredCapabilities: string[];
  missingFields: string[];
}

export interface MatchResult {
  candidate: Candidate;
  totalScore: number;
  breakdown: {
    experience: number;
    skills: number;
    profile: number;
    careerPreference: number;
  };
  reasons: string[];
  risks: string[];
  interviewQuestions: string[];
}

export interface ExportPackage {
  formats: string[];
  rows: Array<Record<string, string | number>>;
}
