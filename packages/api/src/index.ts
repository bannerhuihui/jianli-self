import { mockJobProfile, mockMatches, mockResume, mockTalentProfile } from '@ai-talent-agent/shared';

export async function getStructuredResume() {
  return mockResume;
}

export async function getTalentProfile() {
  return mockTalentProfile;
}

export async function getJobProfile() {
  return mockJobProfile;
}

export async function getMatchResults() {
  return mockMatches;
}

export async function getMatchDetail(candidateId: string) {
  return mockMatches.find((match) => match.candidate.id === candidateId) ?? mockMatches[0];
}
