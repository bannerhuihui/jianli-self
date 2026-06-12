<template>
  <view class="page detail-page">
    <AppTopNav active="HR 工作台" />

    <view class="container detail-container">
      <ProgressSteps v-bind="createFlowStepsProps(HR_FLOW, 3)" navigable />

      <view class="notice-banner">
        <AppIcon name="info" :size="20" color="#eeefff" filled />
        <text>建议进入面试：候选人与岗位高度匹配，但需验证稳定性与到岗时间。</text>
      </view>

      <view class="detail-layout">
        <main class="main-stack">
          <view class="action-bar">
            <button class="back-link" @tap="goBack">
              <AppIcon name="arrow_back" :size="20" color="#565e74" />
              <text>返回列表</text>
            </button>
            <view class="top-actions">
              <button class="hr-secondary-action" @tap="copyReasons">
                <AppIcon name="content_copy" :size="20" color="#565e74" />
                <text>复制推荐理由</text>
              </button>
              <button class="hr-primary-action hr-primary-action--gradient" @tap="exportPackage">
                <AppIcon name="ios_share" :size="20" color="#ffffff" />
                <text>导出推荐包</text>
              </button>
            </view>
          </view>

          <view class="score-grid">
            <section class="score-card talent-card">
              <view class="score-watermark">
                <AppIcon name="auto_awesome" :size="48" color="#2563eb" />
              </view>
              <text class="score-label">综合匹配分</text>
              <view class="ring-wrap">
                <svg class="score-ring" viewBox="0 0 128 128">
                  <circle
                    class="ring-track"
                    cx="64"
                    cy="64"
                    r="58"
                    fill="transparent"
                    stroke-width="8"
                  />
                  <circle
                    class="ring-fill"
                    cx="64"
                    cy="64"
                    r="58"
                    fill="transparent"
                    stroke-width="8"
                    stroke-linecap="round"
                    :stroke-dasharray="ringCircumference"
                    :stroke-dashoffset="ringOffset"
                  />
                </svg>
                <text class="ring-number">{{ match.totalScore }}</text>
              </view>
              <view class="confidence-badge">
                <AppIcon name="check_circle" :size="14" color="#059669" filled />
                <text>{{ confidenceLabel }}</text>
              </view>
            </section>

            <section class="breakdown-card talent-card">
              <view class="section-title">
                <AppIcon name="analytics" :size="20" color="#004ac6" />
                <text>匹配维度拆解</text>
              </view>
              <view class="breakdown-columns">
                <view class="break-column">
                  <view v-for="item in breakdownLeft" :key="item.label" class="break-item">
                    <view class="break-head">
                      <text>{{ item.label }}</text>
                      <text class="break-value">{{ item.value }}%</text>
                    </view>
                    <view class="bar-track">
                      <view class="bar-fill" :style="{ width: `${item.value}%` }" />
                    </view>
                  </view>
                </view>
                <view class="break-column">
                  <view v-for="item in breakdownRight" :key="item.label" class="break-item">
                    <view class="break-head">
                      <text>{{ item.label }}</text>
                      <text class="break-value">{{ item.value }}%</text>
                    </view>
                    <view class="bar-track">
                      <view class="bar-fill" :style="{ width: `${item.value}%` }" />
                    </view>
                  </view>
                </view>
              </view>
            </section>
          </view>

          <section class="talent-card card-block">
            <view class="card-title-row">
              <view class="section-title">
                <AppIcon name="verified" :size="20" color="#004ac6" />
                <text>核心推荐理由</text>
              </view>
              <text class="muted">基于简历内容与岗位 JD 实时分析</text>
            </view>
            <view class="reason-list">
              <view v-for="reason in detailReasons" :key="reason.title" class="reason-block">
                <text class="reason-title">{{ reason.title }}</text>
                <view class="reason-evidence-row">
                  <text class="reason-evidence">{{ reason.evidence }}</text>
                  <button class="evidence-link" @tap="viewEvidence(reason.title)">
                    <text>查看证据</text>
                    <AppIcon name="open_in_new" :size="16" color="#004ac6" />
                  </button>
                </view>
              </view>
            </view>
          </section>

          <section class="talent-card card-block">
            <view class="section-title section-title-gap">
              <AppIcon name="warning" :size="20" color="#ba1a1a" />
              <text>需关注事项</text>
            </view>
            <view class="risk-grid">
              <view v-for="risk in detailRisks" :key="risk.title" class="risk-card">
                <AppIcon name="info" :size="20" color="#ba1a1a" />
                <view>
                  <text class="risk-title">{{ risk.title }}</text>
                  <text class="risk-desc">{{ risk.desc }}</text>
                </view>
              </view>
            </view>
          </section>

          <section class="talent-card card-block">
            <view class="card-title-row">
              <view class="section-title">
                <AppIcon name="chat" :size="20" color="#004ac6" />
                <text>建议面试问题</text>
              </view>
              <button class="copy-questions-btn" @tap="copyQuestions">
                <AppIcon name="content_copy" :size="18" color="#004ac6" />
                <text>复制面试问题</text>
              </button>
            </view>
            <view class="question-list">
              <view v-for="(item, index) in interviewItems" :key="item.title" class="question-card">
                <text class="question-title">{{ item.title }}</text>
                <text class="question-text">{{ item.text }}</text>
                <AppIcon name="edit" :size="18" color="#565e74" class="question-edit" />
              </view>
            </view>
          </section>
        </main>

        <aside class="side-stack">
          <section class="talent-card profile-card">
            <view class="profile-head">
              <view class="avatar-large">{{ match.candidate.name.slice(0, 1) }}</view>
              <view>
                <text class="candidate-name">{{ candidateDisplay.name }}</text>
                <text class="candidate-title">{{ candidateDisplay.title }}</text>
                <view class="profile-tags">
                  <text class="profile-tag">{{ candidateDisplay.experienceTag }}</text>
                  <text class="profile-tag">{{ candidateDisplay.educationTag }}</text>
                </view>
              </view>
            </view>
            <view class="profile-meta-grid">
              <view class="meta-item">
                <AppIcon name="location_on" :size="18" color="#565e74" />
                <text>{{ candidateDisplay.location }}</text>
              </view>
              <view class="meta-item">
                <AppIcon name="payments" :size="18" color="#565e74" />
                <text>{{ candidateDisplay.salary }}</text>
              </view>
            </view>
          </section>

          <section class="resume-shell">
            <view class="resume-toolbar">
              <text>简历预览 (PDF)</text>
              <view class="toolbar-actions">
                <button class="icon-btn" @tap="zoomResume">
                  <AppIcon name="zoom_in" :size="20" color="rgba(255,255,255,0.7)" />
                </button>
                <button class="icon-btn" @tap="downloadResume">
                  <AppIcon name="download" :size="20" color="rgba(255,255,255,0.7)" />
                </button>
                <button class="icon-btn" @tap="fullscreenResume">
                  <AppIcon name="fullscreen" :size="20" color="rgba(255,255,255,0.7)" />
                </button>
              </view>
            </view>
            <scroll-view class="resume-scroll" scroll-y>
              <view class="resume-paper">
                <view class="resume-header">
                  <text class="resume-doc-title">{{ resumePreview.title }}</text>
                  <text class="resume-contact">{{ resumePreview.contact }}</text>
                </view>

                <view class="resume-section">
                  <text class="resume-section-title">工作经历</text>
                  <view
                    v-for="(job, index) in resumePreview.jobs"
                    :key="job.company"
                    class="resume-job"
                    :class="{ spaced: index === 0 }"
                  >
                    <view class="resume-job-head">
                      <text class="resume-bold">{{ job.company }}</text>
                      <text class="resume-bold">{{ job.period }}</text>
                    </view>
                    <text class="resume-role">{{ job.role }}</text>
                    <view v-if="job.bullets?.length" class="resume-bullets">
                      <text v-for="bullet in job.bullets" :key="bullet" class="resume-bullet">· {{ bullet }}</text>
                    </view>
                  </view>
                </view>

                <view class="resume-section">
                  <text class="resume-section-title">教育背景</text>
                  <view class="resume-job-head">
                    <text>{{ resumePreview.education.line }}</text>
                    <text>{{ resumePreview.education.period }}</text>
                  </view>
                </view>

                <view class="resume-section">
                  <text class="resume-section-title">技术栈</text>
                  <text class="resume-stack">{{ resumePreview.stack }}</text>
                </view>
              </view>
            </scroll-view>
          </section>
        </aside>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { mockMatches } from '@ai-talent-agent/shared';
import {
  MATCH_BREAKDOWN_LEFT,
  MATCH_BREAKDOWN_RIGHT,
  MATCH_DETAIL_REASONS,
  MATCH_DETAIL_RISKS,
  MATCH_INTERVIEW_ITEMS,
  MATCH_SALARY_ESTIMATE,
  buildResumePreview,
} from '../../../constants/match-detail-display';
import { HR_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { copyText, runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';

const candidateId = ref(mockMatches[0].candidate.id);
const ringCircumference = 2 * Math.PI * 58;

onLoad((query) => {
  if (typeof query?.id === 'string' && query.id) {
    candidateId.value = query.id;
  }
});

const match = computed(
  () => mockMatches.find((item) => item.candidate.id === candidateId.value) ?? mockMatches[0],
);

const ringOffset = computed(
  () => ringCircumference * (1 - match.value.totalScore / 100),
);

const confidenceLabel = computed(() => {
  if (match.value.totalScore >= 90) return '极高匹配度';
  if (match.value.totalScore >= 80) return '高度匹配';
  return '中度匹配';
});

const breakdownLeft = MATCH_BREAKDOWN_LEFT;
const breakdownRight = MATCH_BREAKDOWN_RIGHT;
const detailReasons = MATCH_DETAIL_REASONS;
const detailRisks = MATCH_DETAIL_RISKS;
const interviewItems = MATCH_INTERVIEW_ITEMS;

const candidateDisplay = computed(() => ({
  name: match.value.candidate.name,
  title: match.value.candidate.title,
  experienceTag: `${match.value.candidate.experienceYears}年经验`,
  educationTag: match.value.candidate.education,
  location: match.value.candidate.location,
  salary: MATCH_SALARY_ESTIMATE,
}));

const resumePreview = computed(() => buildResumePreview(match.value.candidate.name));

function goBack() {
  uni.navigateBack({
    fail: () => {
      uni.navigateTo({ url: '/pages/hr/candidates/index' });
    },
  });
}

function copyReasons() {
  const current = match.value;
  const text = [
    `候选人：${current.candidate.name}`,
    `匹配分：${current.totalScore}`,
    '',
    '推荐理由：',
    ...detailReasons.map((item) => `${item.title}\n${item.evidence}`),
    '',
    '需关注事项：',
    ...detailRisks.map((item) => `${item.title}：${item.desc}`),
  ].join('\n');
  copyText(text, '推荐理由已复制');
}

function copyQuestions() {
  const text = interviewItems.map((item) => `${item.title}\n${item.text}`).join('\n\n');
  copyText(text, '面试问题已复制');
}

async function exportPackage() {
  await runAsyncAction(() => simulateDelay(1000), {
    loading: '正在准备推荐包',
    success: '推荐包已生成',
  });
  uni.navigateTo({ url: '/pages/hr/export/index' });
}

function viewEvidence(title: string) {
  showToast(`查看证据：${title}`);
}

function zoomResume() {
  showToast('放大预览（MVP 占位）');
}

function downloadResume() {
  showToast('开始下载简历', 'success');
}

function fullscreenResume() {
  showToast('全屏预览（MVP 占位）');
}
</script>

<style lang="scss" scoped>
.detail-container {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  padding-bottom: 48rpx;
}

.notice-banner {
  display: flex;
  align-items: center;
  gap: 24rpx;
  padding: 24rpx 32rpx;
  border-radius: 16rpx;
  background: #2563eb;
  color: #eeefff;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.43;
  box-shadow: 0 4rpx 16rpx rgba(37, 99, 235, 0.12);
}

.detail-layout {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.main-stack,
.side-stack {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.action-bar {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  margin: 0;
  padding: 0;
  background: transparent;
  color: #565e74;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
  &::after { border: none; }
}

.top-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 24rpx;
}

.talent-card {
  border: 2rpx solid #e2e8f0;
  border-radius: 16rpx;
  background: #fff;
  transition: box-shadow 0.2s;
}

.score-grid {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.score-card {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  padding: 48rpx;
}

.score-watermark {
  position: absolute;
  top: 16rpx;
  right: 16rpx;
  opacity: 0.2;
}

.score-label {
  color: #565e74;
  font-size: 24rpx;
  font-weight: 500;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  line-height: 1.33;
}

.ring-wrap {
  position: relative;
  width: 256rpx;
  height: 256rpx;
  margin-top: 16rpx;
}

.score-ring {
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}

.ring-track { stroke: #d3e4fe; }
.ring-fill { stroke: #004ac6; }

.ring-number {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0b1c30;
  font-size: 60rpx;
  font-weight: 700;
  line-height: 1;
}

.confidence-badge {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 24rpx;
  border-radius: 999rpx;
  padding: 4rpx 16rpx;
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}

.breakdown-card,
.card-block {
  padding: 48rpx;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 16rpx;
  color: #0b1c30;
  font-size: 36rpx;
  font-weight: 600;
  line-height: 1.33;
}

.section-title-gap { margin-bottom: 32rpx; }

.breakdown-columns {
  display: grid;
  gap: 48rpx;
  margin-top: 32rpx;
}

.break-column {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.break-head {
  display: flex;
  justify-content: space-between;
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}

.break-value { color: #004ac6; }

.bar-track {
  height: 16rpx;
  margin-top: 8rpx;
  border-radius: 999rpx;
  background: #d3e4fe;
  overflow: hidden;
}

.bar-fill {
  height: 100%;
  border-radius: 999rpx;
  background: #004ac6;
}

.card-title-row {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  margin-bottom: 32rpx;
}

.muted {
  color: #565e74;
  font-size: 28rpx;
  line-height: 1.43;
}

.reason-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.reason-block {
  padding: 24rpx;
  border-left: 8rpx solid #004ac6;
  border-radius: 0 16rpx 16rpx 0;
  background: #eff4ff;
}

.reason-title {
  display: block;
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.43;
}

.reason-evidence-row {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  margin-top: 8rpx;
}

.reason-evidence {
  color: #565e74;
  font-size: 28rpx;
  line-height: 1.43;
}

.evidence-link {
  display: inline-flex;
  align-items: center;
  gap: 4rpx;
  align-self: flex-start;
  margin: 0;
  padding: 0;
  background: transparent;
  color: #004ac6;
  font-size: 24rpx;
  font-weight: 500;
  &::after { border: none; }
}

.risk-grid {
  display: grid;
  gap: 24rpx;
}

.risk-card {
  display: flex;
  gap: 24rpx;
  padding: 24rpx;
  border-radius: 16rpx;
  background: rgba(255, 218, 214, 0.3);
}

.risk-title {
  display: block;
  color: #93000a;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.43;
}

.risk-desc {
  display: block;
  margin-top: 4rpx;
  color: #93000a;
  font-size: 28rpx;
  line-height: 1.43;
  opacity: 0.8;
}

.copy-questions-btn {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  margin: 0;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
  background: transparent;
  color: #004ac6;
  font-size: 28rpx;
  font-weight: 600;
  &::after { border: none; }
}

.question-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.question-card {
  position: relative;
  padding: 24rpx;
  border-radius: 16rpx;
  background: #e5eeff;
}

.question-title {
  display: block;
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.43;
  margin-bottom: 8rpx;
}

.question-text {
  color: #565e74;
  font-size: 28rpx;
  line-height: 1.43;
}

.question-edit {
  position: absolute;
  top: 24rpx;
  right: 24rpx;
  opacity: 0.45;
}

.profile-card {
  padding: 48rpx;
}

.profile-head {
  display: flex;
  gap: 32rpx;
  align-items: flex-start;
  margin-bottom: 32rpx;
}

.avatar-large {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 128rpx;
  height: 128rpx;
  border-radius: 999rpx;
  background: #dbe1ff;
  color: #004ac6;
  font-size: 48rpx;
  font-weight: 700;
  flex-shrink: 0;
}

.candidate-name {
  display: block;
  color: #0b1c30;
  font-size: 48rpx;
  font-weight: 600;
  line-height: 1.33;
}

.candidate-title {
  display: block;
  margin-top: 4rpx;
  color: #565e74;
  font-size: 32rpx;
  line-height: 1.5;
}

.profile-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-top: 16rpx;
}

.profile-tag {
  border-radius: 8rpx;
  padding: 4rpx 16rpx;
  background: #d3e4fe;
  color: #434655;
  font-size: 24rpx;
  line-height: 1.33;
}

.profile-meta-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
  border-top: 2rpx solid #c3c6d7;
  padding-top: 24rpx;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  color: #565e74;
  font-size: 28rpx;
  line-height: 1.43;
}

.resume-shell {
  display: flex;
  flex-direction: column;
  min-height: 800rpx;
  border-radius: 16rpx;
  overflow: hidden;
  background: #213145;
}

.resume-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 24rpx 32rpx;
  background: #3f465c;
  color: #fff;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}

.toolbar-actions {
  display: flex;
  gap: 24rpx;
}

.icon-btn {
  margin: 0;
  padding: 0;
  background: transparent;
  line-height: 1;
  &::after { border: none; }
}

.resume-scroll {
  flex: 1;
  max-height: 960rpx;
  padding: 32rpx;
  background: rgba(211, 228, 254, 0.2);
  box-sizing: border-box;
}

.resume-paper {
  min-height: 1200rpx;
  padding: 48rpx;
  background: #fff;
  box-shadow: 0 16rpx 48rpx rgba(15, 23, 42, 0.18);
  color: #0b1c30;
  font-size: 26rpx;
  line-height: 1.62;
}

.resume-header {
  padding-bottom: 32rpx;
  margin-bottom: 32rpx;
  border-bottom: 2rpx solid #e2e8f0;
  text-align: center;
}

.resume-doc-title {
  display: block;
  font-size: 40rpx;
  font-weight: 700;
  line-height: 1.3;
}

.resume-contact {
  display: block;
  margin-top: 8rpx;
  color: #434655;
}

.resume-section {
  margin-bottom: 32rpx;
}

.resume-section-title {
  display: block;
  margin-bottom: 16rpx;
  padding-bottom: 8rpx;
  border-bottom: 2rpx solid #e2e8f0;
  font-weight: 700;
}

.resume-job {
  margin-bottom: 16rpx;
}

.resume-job.spaced {
  margin-bottom: 24rpx;
}

.resume-job-head {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.resume-bold { font-weight: 700; }

.resume-role {
  display: block;
  color: #565e74;
  font-style: italic;
}

.resume-bullets {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
  margin-top: 8rpx;
  padding-left: 24rpx;
}

.resume-bullet {
  color: #434655;
}

.resume-stack {
  white-space: pre-line;
  color: #434655;
}

@media (min-width: 768px) {
  .notice-banner {
    padding: 12px 16px;
    font-size: 14px;
    border-radius: 12px;
  }

  .action-bar {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .back-link { font-size: 14px; }

  .detail-layout {
    flex-direction: row;
    align-items: flex-start;
    gap: 24px;
  }

  .main-stack {
    flex: 2;
    min-width: 0;
    gap: 24px;
  }

  .side-stack {
    flex: 1;
    min-width: 0;
    gap: 24px;
    position: sticky;
    top: 96px;
  }

  .score-grid {
    display: grid;
    grid-template-columns: 4fr 8fr;
    gap: 24px;
  }

  .score-card,
  .breakdown-card,
  .card-block,
  .profile-card {
    padding: 24px;
    border-radius: 12px;
  }

  .ring-wrap {
    width: 128px;
    height: 128px;
  }

  .ring-number { font-size: 30px; }

  .score-label { font-size: 12px; }
  .confidence-badge { font-size: 12px; }

  .section-title { font-size: 20px; }

  .breakdown-columns {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
  }

  .break-head { font-size: 14px; }

  .muted,
  .reason-title,
  .reason-evidence,
  .risk-title,
  .risk-desc,
  .question-title,
  .question-text,
  .candidate-title,
  .meta-item {
    font-size: 14px;
  }

  .candidate-name { font-size: 24px; }

  .avatar-large {
    width: 64px;
    height: 64px;
    font-size: 24px;
  }

  .card-title-row {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  .reason-evidence-row {
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
  }

  .reason-evidence {
    flex: 1;
    max-width: 80%;
  }

  .risk-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .resume-scroll {
    max-height: calc(100vh - 280px);
  }

  .resume-paper {
    min-height: 800px;
    padding: 24px;
    font-size: 13px;
  }

  .resume-doc-title { font-size: 20px; }

  .talent-card:hover {
    box-shadow: 0 4px 12px rgba(15, 23, 42, 0.05);
  }
}
</style>
