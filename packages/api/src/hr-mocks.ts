import { mockJobProfile, mockMatches } from '@ai-talent-agent/shared';

export async function getJobProfile() {
  return mockJobProfile;
}

export async function getMatchResults() {
  return mockMatches;
}

export async function getMatchDetail(candidateId: string) {
  return mockMatches.find((match) => match.candidate.id === candidateId) ?? mockMatches[0];
}
