<template>
  <view class="page hr-page">
    <AppTopNav active="HR 工作台" />

    <view class="container profile-container">
      <ProgressSteps v-bind="createFlowStepsProps(HR_FLOW, 1)" navigable />

      <section class="header-row">
        <view class="header-copy">
          <view class="status-row">
            <AppIcon name="psychology" :size="20" color="#004ac6" />
            <text>AI 岗位画像分析已完成</text>
          </view>
          <text class="page-title">岗位画像确认</text>
          <text class="page-desc">请核对 AI 提取的关键要素，确保画像准确以实现精准匹配。</text>
        </view>
        <view class="header-actions">
          <button class="hr-secondary-action" @tap="exportProfile">
            <AppIcon name="download" :size="20" color="#0b1c30" />
            <text>导出画像</text>
          </button>
          <button class="hr-primary-action" @tap="goCandidates">
            <text>开始匹配候选人</text>
            <AppIcon name="arrow_forward" :size="20" color="#ffffff" />
          </button>
        </view>
      </section>

      <view class="profile-grid">
        <main class="main-stack">
          <section class="glass-card card-block">
            <view class="card-title-row">
              <view class="card-title-wrap">
                <AppIcon name="work" :size="20" color="#004ac6" />
                <text class="card-title">基本信息</text>
              </view>
              <button class="edit-all">编辑全部</button>
            </view>
            <view class="info-grid">
              <view class="info-field">
                <text class="field-label">岗位名称</text>
                <text class="editable strong">{{ job.title }}</text>
              </view>
              <view class="info-field">
                <text class="field-label">经验要求</text>
                <text class="editable">{{ job.seniority }}</text>
              </view>
            </view>
          </section>

          <section class="glass-card card-block">
            <view class="card-title-wrap section-title">
              <AppIcon name="assignment" :size="20" color="#004ac6" />
              <text class="card-title">核心职责</text>
            </view>
            <view class="bullet-list">
              <view v-for="item in responsibilities" :key="item" class="bullet-item">
                <view class="dot" />
                <text class="editable-line">{{ item }}</text>
              </view>
            </view>
            <view class="ai-suggest">
              <AppIcon name="tips_and_updates" :size="20" color="#004ac6" />
              <view class="suggest-copy">
                <text class="suggest-text">
                  <text class="suggest-strong">AI 建议：</text>
                  职责中未明确提及对具体云服务（如 AWS, Aliyun）的管理经验，这通常是高级职位的关键点，建议补充。
                </text>
                <button class="suggest-link" @tap="onSuggestLink">点击补充</button>
              </view>
            </view>
          </section>

          <view class="skill-grid">
            <section class="glass-card card-block">
              <view class="card-title-wrap section-title small">
                <AppIcon name="star" :size="20" color="#004ac6" filled />
                <text class="card-title">必须具备 (Must-have)</text>
              </view>
              <view class="tag-row">
                <text v-for="skill in job.requiredSkills" :key="skill" class="skill-pill primary">{{ skill }}</text>
                <text class="add-tag">+ 添加</text>
              </view>
            </section>

            <section class="glass-card card-block">
              <view class="card-title-wrap section-title small">
                <AppIcon name="add_circle" :size="20" color="#565e74" />
                <text class="card-title">加分项 (Bonus)</text>
              </view>
              <view class="tag-row">
                <text v-for="skill in job.bonusSkills" :key="skill" class="skill-pill secondary">{{ skill }}</text>
                <text class="add-tag secondary">+ 添加</text>
              </view>
            </section>
          </view>
        </main>

        <aside class="side-stack">
          <section class="glass-card radar-card">
            <view class="card-title-wrap section-title">
              <AppIcon name="radar" :size="20" color="#004ac6" />
              <text class="card-title">岗位能力要求</text>
            </view>
            <view class="radar-box">
              <view class="radar-label top">技术深度</view>
              <view class="radar-label right">系统架构</view>
              <view class="radar-label bottom-right">团队领导力</view>
              <view class="radar-label bottom-left">业务理解</view>
              <view class="radar-label left">沟通协作</view>
              <view class="radar-layer outer" />
              <view class="radar-layer inner" />
              <view class="radar-fill" />
            </view>
            <view class="score-list">
              <view v-for="score in capabilityScores" :key="score.name" class="score-item">
                <view class="score-head">
                  <text>{{ score.name }}</text>
                  <text class="score-value">{{ score.value }}/100</text>
                </view>
                <view class="score-track">
                  <view class="score-bar" :style="{ width: `${score.value}%` }" />
                </view>
              </view>
            </view>
          </section>

          <section class="glass-card card-block persona-card">
            <view class="card-title-wrap section-title">
              <AppIcon name="explore" :size="20" color="#004ac6" />
              <text class="card-title">职业倾向 (Persona)</text>
            </view>
            <view class="persona-list">
              <view v-for="item in personas" :key="item.title" class="persona-item">
                <AppIcon name="check_circle" :size="20" color="#006242" />
                <view>
                  <text class="persona-title">{{ item.title }}</text>
                  <text class="persona-desc">{{ item.desc }}</text>
                </view>
              </view>
            </view>
            <view class="missing-card">
              <view class="missing-head">
                <AppIcon name="info" :size="16" color="#ba1a1a" />
                <text class="missing-title">完善岗位画像</text>
              </view>
              <text class="missing-desc">建议设置工作模式（远程/办公），当前默认为混合办公。</text>
              <button class="missing-action">立即设定</button>
            </view>
          </section>

          <view class="insight-card">
            <image class="insight-image" :src="insightImageUrl" mode="aspectFill" />
            <view class="insight-overlay">
              <text class="insight-title">行业洞察</text>
              <text class="insight-desc">当前市场上此类人才稀缺，建议匹配薪资范围：40k-65k。</text>
            </view>
          </view>
        </aside>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { mockJobProfile } from '@ai-talent-agent/shared';
import { HR_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';
const job = mockJobProfile;

const responsibilities = [
  '负责高性能、高可扩展的微服务架构设计与核心模块开发。',
  '优化前端性能，提升复杂交互场景下的用户体验与渲染效率。',
  '指导初中级工程师，参与代码审查并制定技术标准。',
];

const capabilityScores = [
  { name: '技术深度', value: 90 },
  { name: '沟通协作', value: 75 },
];

const personas = [
  { title: '自我驱动型', desc: '在不确定性中寻找最优技术路径。' },
  { title: '产品思维', desc: '不仅仅是写代码，更关注业务价值实现。' },
];

const insightImageUrl =
  'https://lh3.googleusercontent.com/aida/AP1WRLv-_8DvsBNnbQy-XYiow7GNXIrLIadT3t8oR-TaNee5GV-AjPNKDtADQLlWU5Klk7mT4MduxFuRIur4QQxp_3fa9atfrWfIvXDo1cJ6s14sUZHX7f2IP-6z8gesu5oygqfXDrJBiW7nV0OIlLGUCB5LFn9F9GreDH526CfWm4QZTZTZmbkw9vrZHIzRGPr-2zO9UDYwyHLjwFhmbV6tuOe8FNvUAw4K6WC5jWk3_7XKkA-mLRXnR5d099M';

async function exportProfile() {
  await runAsyncAction(() => simulateDelay(800), {
    loading: '导出画像',
    success: '岗位画像已导出',
  });
}

function goCandidates() {
  uni.navigateTo({ url: '/pages/hr/candidates/index' });
}

function onSuggestLink() {
  showToast('补充职责建议（MVP 占位）');
}
</script>

<style lang="scss" scoped>
.profile-container { display: flex; flex-direction: column; gap: 48rpx; padding-bottom: 48rpx; }

.header-row {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}
.status-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 16rpx;
  color: #004ac6;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}
.page-title {
  display: block;
  color: #0b1c30;
  font-size: 60rpx;
  font-weight: 700;
  line-height: 1.27;
  letter-spacing: -0.01em;
}
.page-desc {
  display: block;
  margin-top: 8rpx;
  color: #434655;
  font-size: 32rpx;
  line-height: 1.5;
}
.header-actions { display: flex; flex-wrap: wrap; gap: 24rpx; }

.profile-grid { display: flex; flex-direction: column; gap: 48rpx; }
.main-stack,
.side-stack { display: flex; flex-direction: column; gap: 48rpx; }

.glass-card {
  border: 2rpx solid #e2e8f0;
  border-radius: 16rpx;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
}
.card-block { padding: 48rpx; }
.card-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24rpx;
  margin-bottom: 32rpx;
}
.card-title-wrap {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.section-title { margin-bottom: 32rpx; }
.section-title.small { margin-bottom: 32rpx; }
.card-title {
  color: #0b1c30;
  font-size: 40rpx;
  font-weight: 600;
  line-height: 1.4;
}
.section-title.small .card-title { font-size: 36rpx; }
.edit-all {
  margin: 0;
  padding: 0;
  color: #004ac6;
  background: transparent;
  font-size: 28rpx;
  font-weight: 600;
  &::after { border: none; }
}

.info-grid { display: grid; gap: 48rpx; }
.info-field { display: flex; flex-direction: column; gap: 8rpx; }
.field-label { color: #434655; font-size: 24rpx; font-weight: 500; line-height: 1.33; }
.editable {
  padding: 16rpx;
  margin-left: -16rpx;
  color: #0b1c30;
  font-size: 32rpx;
  line-height: 1.5;
}
.editable.strong { font-size: 36rpx; font-weight: 600; line-height: 1.33; }

.bullet-list { display: flex; flex-direction: column; gap: 32rpx; }
.bullet-item { display: flex; gap: 32rpx; align-items: flex-start; }
.dot {
  width: 12rpx;
  height: 12rpx;
  margin-top: 14rpx;
  border-radius: 999rpx;
  background: #004ac6;
  flex-shrink: 0;
}
.editable-line {
  flex: 1;
  color: #0b1c30;
  font-size: 32rpx;
  line-height: 1.5;
}

.ai-suggest {
  display: flex;
  gap: 24rpx;
  margin-top: 48rpx;
  padding: 32rpx;
  border: 2rpx solid #dbe1ff;
  border-radius: 16rpx;
  background: rgba(219, 225, 255, 0.3);
  animation: pulse-subtle 3s ease-in-out infinite;
}
@keyframes pulse-subtle {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.7; }
}
.suggest-copy { flex: 1; display: flex; flex-direction: column; gap: 8rpx; }
.suggest-text { color: #003ea8; font-size: 28rpx; line-height: 1.43; }
.suggest-strong { font-weight: 700; }
.suggest-link {
  align-self: flex-start;
  margin: 0;
  padding: 0;
  color: #004ac6;
  background: transparent;
  font-size: 28rpx;
  font-weight: 700;
  &::after { border: none; }
}

.skill-grid { display: grid; gap: 48rpx; }
.tag-row { display: flex; flex-wrap: wrap; gap: 16rpx; }
.skill-pill {
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}
.skill-pill.primary {
  border: 2rpx solid rgba(0, 74, 198, 0.2);
  background: rgba(0, 74, 198, 0.1);
  color: #004ac6;
}
.skill-pill.secondary {
  border: 2rpx solid rgba(86, 94, 116, 0.2);
  background: rgba(86, 94, 116, 0.1);
  color: #565e74;
}
.add-tag {
  border: 2rpx dashed #c3c6d7;
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  color: #434655;
  font-size: 28rpx;
  font-weight: 600;
}
.add-tag.secondary { color: #565e74; }

.radar-card { padding: 48rpx; }
.radar-box {
  position: relative;
  width: 100%;
  max-width: 480rpx;
  aspect-ratio: 1;
  margin: 0 auto 48rpx;
}
.radar-layer,
.radar-fill {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  clip-path: polygon(50% 0%, 90% 32%, 74% 92%, 26% 92%, 10% 32%);
}
.radar-layer {
  border: 2rpx solid #e2e8f0;
  background: transparent;
}
.radar-layer.outer { width: 88%; height: 88%; }
.radar-layer.inner { width: 52%; height: 52%; opacity: 0.5; }
.radar-fill {
  width: 72%;
  height: 76%;
  background: rgba(37, 99, 235, 0.1);
  border: 4rpx solid #2563eb;
}
.radar-label {
  position: absolute;
  color: #434655;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}
.radar-label.top { top: 0; left: 50%; transform: translateX(-50%); text-align: center; width: 100%; }
.radar-label.right { right: 0; top: 40%; }
.radar-label.left { left: 0; top: 40%; }
.radar-label.bottom-right { right: 80rpx; bottom: 16rpx; }
.radar-label.bottom-left { left: 80rpx; bottom: 16rpx; }

.score-list { display: flex; flex-direction: column; gap: 24rpx; }
.score-head {
  display: flex;
  justify-content: space-between;
  color: #0b1c30;
  font-size: 28rpx;
  line-height: 1.43;
}
.score-value { color: #004ac6; font-weight: 700; }
.score-track {
  height: 12rpx;
  margin-top: 12rpx;
  border-radius: 999rpx;
  background: #e5eeff;
  overflow: hidden;
}
.score-bar { height: 100%; border-radius: 999rpx; background: #004ac6; }

.persona-list { display: flex; flex-direction: column; gap: 32rpx; }
.persona-item { display: flex; gap: 24rpx; align-items: flex-start; }
.persona-title {
  display: block;
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}
.persona-desc {
  display: block;
  margin-top: 8rpx;
  color: #434655;
  font-size: 28rpx;
  line-height: 1.43;
}

.missing-card {
  margin-top: 32rpx;
  padding: 24rpx;
  border-left: 8rpx solid #ba1a1a;
  border-radius: 16rpx;
  background: #e5eeff;
}
.missing-head {
  display: flex;
  align-items: center;
  gap: 8rpx;
}
.missing-title { color: #ba1a1a; font-size: 24rpx; font-weight: 700; }
.missing-desc {
  display: block;
  margin-top: 8rpx;
  color: #434655;
  font-size: 28rpx;
  line-height: 1.43;
}
.missing-action {
  margin: 8rpx 0 0;
  padding: 0;
  color: #004ac6;
  background: transparent;
  font-size: 24rpx;
  font-weight: 700;
  text-decoration: underline;
  &::after { border: none; }
}

.insight-card {
  position: relative;
  height: 384rpx;
  overflow: hidden;
  border-radius: 16rpx;
}
.insight-image {
  width: 100%;
  height: 100%;
}
.insight-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 32rpx;
  background: linear-gradient(180deg, rgba(0, 74, 198, 0.05), rgba(0, 74, 198, 0.8));
}
.insight-title { color: #fff; font-size: 36rpx; font-weight: 700; line-height: 1.33; }
.insight-desc { margin-top: 8rpx; color: rgba(255, 255, 255, 0.9); font-size: 28rpx; line-height: 1.43; }

@media (min-width: 768px) {
  .header-row {
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-between;
  }
  .page-title { font-size: 30px; line-height: 38px; }
  .page-desc { font-size: 16px; line-height: 24px; }
  .profile-grid {
    display: grid;
    grid-template-columns: 8fr 4fr;
    gap: 24px;
    align-items: start;
  }
  .main-stack,
  .side-stack { gap: 24px; }
  .card-block,
  .radar-card { padding: 24px; border-radius: 12px; }
  .card-title { font-size: 20px; line-height: 28px; }
  .section-title.small .card-title { font-size: 18px; }
  .field-label { font-size: 12px; }
  .editable { font-size: 16px; }
  .editable.strong { font-size: 18px; }
  .editable-line,
  .suggest-text { font-size: 14px; }
  .skill-pill,
  .add-tag { font-size: 14px; }
  .info-grid,
  .skill-grid { grid-template-columns: repeat(2, 1fr); gap: 24px; }
  .radar-box { max-width: 240px; margin-bottom: 24px; }
  .radar-label { font-size: 12px; }
  .score-head { font-size: 14px; }
  .persona-title,
  .persona-desc,
  .missing-desc { font-size: 14px; }
  .insight-card { height: 192px; }
  .insight-title { font-size: 18px; }
  .insight-desc { font-size: 14px; }
}
</style>
