import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../pages/HomePage.vue';
import UploadPage from '../pages/candidate/UploadPage.vue';
import ReviewPage from '../pages/candidate/ReviewPage.vue';
import InterviewPage from '../pages/candidate/InterviewPage.vue';
import ProfilePage from '../pages/candidate/ProfilePage.vue';
import ResumePage from '../pages/candidate/ResumePage.vue';
import JobInputPage from '../pages/hr/JobInputPage.vue';
import JobProfilePage from '../pages/hr/JobProfilePage.vue';
import CandidateListPage from '../pages/hr/CandidateListPage.vue';
import MatchDetailPage from '../pages/hr/MatchDetailPage.vue';
import ExportPage from '../pages/hr/ExportPage.vue';

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomePage },
    { path: '/candidate/upload', component: UploadPage },
    { path: '/candidate/review', component: ReviewPage },
    { path: '/candidate/interview', component: InterviewPage },
    { path: '/candidate/profile', component: ProfilePage },
    { path: '/candidate/resume', component: ResumePage },
    { path: '/hr/job/new', component: JobInputPage },
    { path: '/hr/job/profile', component: JobProfilePage },
    { path: '/hr/candidates', component: CandidateListPage },
    { path: '/hr/candidates/:id', component: MatchDetailPage },
    { path: '/hr/export', component: ExportPage },
  ],
});
