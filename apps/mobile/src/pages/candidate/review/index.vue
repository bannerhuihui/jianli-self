<template>
  <view class="page candidate-flow-page review-page">
    <AppTopNav active="求职者流程" />

    <view class="container flow-container">
      <ProgressSteps v-bind="createFlowStepsProps(CANDIDATE_FLOW, 1)" navigable />
    </view>

    <view class="review-shell">
      <section class="document-panel">
        <view class="panel-toolbar">
          <text class="panel-heading">原始简历文件</text>
          <view class="toolbar-actions">
            <view class="tool-button"><AppIcon name="zoom_in" :size="20" color="#434655" /></view>
            <view class="tool-button"><AppIcon name="zoom_out" :size="20" color="#434655" /></view>
            <view class="tool-button"><AppIcon name="download" :size="20" color="#434655" /></view>
          </view>
        </view>

        <scroll-view scroll-y class="resume-paper">
          <view class="paper-content">
            <text class="resume-name">{{ resume.basicInfo.name }}的简历</text>
            <view class="contact-row">
              <text>{{ resume.basicInfo.phone }}</text>
              <text>{{ resume.basicInfo.email }}</text>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">教育背景</text>
              <view class="resume-line-between"><text>{{ resume.basicInfo.education }}</text><text>2016 - 2020</text></view>
              <text class="resume-subline">学士学位</text>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">工作经历</text>
              <view class="resume-line-between"><text>阿里巴巴 - 高级软件工程师</text><text>2020 - 至今</text></view>
              <view class="resume-bullets">
                <text v-for="item in resume.workExperience" :key="item">• {{ item }}</text>
              </view>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">专业技能</text>
              <text class="resume-subline">{{ resume.skills.join(', ') }}</text>
            </view>
          </view>
          <view class="scan-overlay">
            <view class="scan-line" />
          </view>
        </scroll-view>
      </section>

      <scroll-view scroll-y class="review-panel">
        <view class="review-inner">
          <view class="review-header">
            <view class="title-row">
              <text class="review-title">智能解析预览</text>
              <view class="status-badge">
                <AppIcon name="check_circle" :size="16" color="#006242" filled />
                <text>AI 已校对完成</text>
              </view>
            </view>
            <text class="review-desc">请确认 AI 提取的信息，带红色指示符的字段可能需要手动复核。</text>
          </view>

          <view class="quality-card">
            <view class="quality-head"><text>解析质量评分</text><text class="quality-score">86%</text></view>
            <view class="quality-track"><view class="quality-value" /></view>
            <text class="quality-note">发现 2 个低置信度字段，建议确认后再进入 AI 访谈。</text>
          </view>

          <view class="field-card">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="person" :size="20" color="#004ac6" />
                <text class="field-title">基本信息</text>
              </view>
              <view class="confidence-badge high">置信度: 高</view>
            </view>
            <view class="form-grid">
              <view class="form-field"><text class="field-label">姓名</text><input class="text-input" :value="resume.basicInfo.name" /></view>
              <view class="form-field"><text class="field-label">联系电话</text><input class="text-input" :value="resume.basicInfo.phone" /></view>
              <view class="form-field full"><text class="field-label">邮箱</text><input class="text-input" :value="resume.basicInfo.email" /></view>
            </view>
          </view>

          <view class="field-card low-confidence">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="school" :size="20" color="#004ac6" />
                <text class="field-title">教育经历</text>
              </view>
              <view class="confidence-badge low">置信度: 低 - 建议复核</view>
            </view>
            <view class="form-grid">
              <view class="form-field"><text class="field-label">院校</text><input class="text-input warning" value="清华大学" /></view>
              <view class="form-field"><text class="field-label">专业</text><input class="text-input" value="计算机科学与技术" /></view>
              <view class="form-field full"><text class="field-label">学位</text><input class="text-input" value="学士" /></view>
            </view>
          </view>

          <view class="field-card">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="work" :size="20" color="#004ac6" />
                <text class="field-title">工作经历</text>
              </view>
              <view class="confidence-badge high">置信度: 极高</view>
            </view>
            <view class="experience-item">
              <view class="experience-top">
                <text class="experience-title">阿里巴巴 - 高级软件工程师</text>
                <AppIcon name="edit" :size="18" color="#737686" />
              </view>
              <text class="experience-time">2020.07 - 至今 (4年1个月)</text>
              <text class="experience-desc">主导微服务架构升级，支持千万级 QPS 大促系统。</text>
            </view>
            <button class="add-button">+ 添加工作经历</button>
          </view>

          <view class="field-card">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="article" :size="20" color="#004ac6" />
                <text class="field-title">项目经历</text>
              </view>
              <view class="confidence-badge medium">置信度: 中</view>
            </view>
            <view class="project-list">
              <view v-for="project in resume.projects" :key="project" class="project-item">
                <text class="project-title">{{ project }}</text>
                <text class="project-desc">AI 已识别项目名称，但项目背景、职责边界和量化结果仍建议补充。</text>
              </view>
            </view>
            <button class="add-button">+ 添加项目经历</button>
          </view>

          <view class="field-card">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="psychology" :size="20" color="#004ac6" />
                <text class="field-title">技能标签</text>
              </view>
              <view class="confidence-badge high">置信度: 高</view>
            </view>
            <view class="skill-list">
              <view v-for="skill in resume.skills" :key="skill" class="skill-pill">
                <text>{{ skill }}</text>
                <AppIcon name="close" :size="14" color="#ffffff" />
              </view>
              <text class="add-skill">+ 添加</text>
            </view>
          </view>

          <view class="missing-card">
            <text class="missing-title">缺失字段提示</text>
            <view class="missing-list">
              <text>• 建议补充最近一次离职原因，用于职业稳定性判断。</text>
              <text>• 建议补充 1-2 个项目的量化结果，提升画像证据强度。</text>
              <text>• 若简历解析不准确，可重新上传或手动录入。</text>
            </view>
          </view>

          <view class="sticky-action">
            <view class="secondary-actions">
              <navigator url="/pages/candidate/upload/index" class="flow-btn flow-btn--secondary flow-btn--compact">重新上传</navigator>
              <button class="flow-btn flow-btn--secondary flow-btn--compact" @tap="onManualEntry">手动录入</button>
              <button class="flow-btn flow-btn--secondary flow-btn--compact" @tap="onSaveEdits">保存修改</button>
            </view>
            <navigator url="/pages/candidate/interview/index" class="flow-btn flow-btn--primary flow-btn--block flow-btn--emphasis">
              <AppIcon name="auto_awesome" :size="20" color="#ffffff" />
              <text>确认并进入 AI 访谈</text>
            </navigator>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { mockResume } from '@ai-talent-agent/shared';
import { CANDIDATE_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { showToast } from '../../../utils/feedback';

const resume = mockResume;

function onManualEntry() {
  showToast('手动录入（MVP 占位）');
}

function onSaveEdits() {
  showToast('修改已保存', 'success');
}
</script>

<style lang="scss" scoped>
.flow-container { padding-bottom: 24rpx; }

.review-shell {
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 200rpx);
  overflow: hidden;
}

/* Left: document preview */
.document-panel {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
  padding: 32rpx;
  background: #cbdbf5;
  overflow: hidden;
}
.panel-toolbar { display: flex; align-items: center; justify-content: space-between; gap: 24rpx; }
.panel-heading { color: #0b1c30; font-size: 40rpx; font-weight: 600; line-height: 1.4; }
.toolbar-actions { display: flex; gap: 16rpx; }
.tool-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 72rpx;
  height: 72rpx;
  border-radius: 16rpx;
  transition: background 0.2s ease;
}
.tool-button:hover { background: #eff4ff; }

.resume-paper {
  position: relative;
  flex: 1;
  min-height: 640rpx;
  overflow: hidden;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  box-shadow: 0 16rpx 40rpx rgba(15, 23, 42, 0.1);
}
.paper-content { max-width: 760rpx; margin: 0 auto; padding: 96rpx 48rpx; color: #334155; }
.resume-name { display: block; margin-bottom: 32rpx; font-size: 56rpx; font-weight: 700; line-height: 1.2; }
.contact-row { display: flex; flex-wrap: wrap; gap: 32rpx; margin-bottom: 64rpx; color: #334155; font-size: 28rpx; opacity: 0.7; }
.resume-section { display: flex; flex-direction: column; gap: 16rpx; margin-bottom: 48rpx; }
.resume-section-title {
  border-bottom: 2rpx solid #e2e8f0;
  padding-bottom: 8rpx;
  color: #0f172a;
  font-size: 36rpx;
  font-weight: 700;
  line-height: 1.33;
}
.resume-line-between { display: flex; justify-content: space-between; gap: 20rpx; color: #334155; font-size: 28rpx; font-weight: 600; }
.resume-subline { color: #64748b; font-size: 28rpx; line-height: 1.6; font-style: italic; }
.resume-bullets { display: flex; flex-direction: column; gap: 8rpx; color: #475569; font-size: 28rpx; line-height: 1.6; }

.scan-overlay {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
  border: 4rpx solid rgba(0, 74, 198, 0.2);
  border-radius: 16rpx;
}
.scan-line {
  position: absolute;
  left: 0;
  right: 0;
  height: 4rpx;
  background: linear-gradient(90deg, transparent, #004ac6, transparent);
  animation: scan 3s linear infinite;
}
@keyframes scan {
  0% { top: 0; opacity: 0; }
  10% { opacity: 1; }
  90% { opacity: 1; }
  100% { top: 100%; opacity: 0; }
}

/* Right: form panel */
.review-panel { flex: 1; background: #fff; }
.review-inner {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  max-width: 1344rpx;
  margin: 0 auto;
  padding: 32rpx;
}
.review-header { display: flex; flex-direction: column; gap: 16rpx; }
.title-row { display: flex; flex-wrap: wrap; align-items: center; gap: 24rpx; }
.review-title { color: #0b1c30; font-size: 48rpx; font-weight: 600; line-height: 1.33; }
.status-badge {
  display: flex;
  align-items: center;
  gap: 8rpx;
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  background: rgba(0, 98, 66, 0.1);
  color: #006242;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}
.review-desc { color: #434655; font-size: 32rpx; font-weight: 400; line-height: 1.5; }

.quality-card {
  border: 2rpx solid #e2e8f0;
  border-radius: 16rpx;
  padding: 32rpx;
  background: #fff;
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
}
.quality-head { display: flex; justify-content: space-between; color: #0b1c30; font-size: 28rpx; font-weight: 600; }
.quality-score { color: #004ac6; font-size: 34rpx; font-weight: 700; }
.quality-track { height: 8rpx; overflow: hidden; border-radius: 999rpx; margin-top: 16rpx; background: #e5eeff; }
.quality-value { width: 86%; height: 100%; background: #004ac6; }
.quality-note { display: block; margin-top: 16rpx; color: #434655; font-size: 24rpx; line-height: 1.43; }

.field-card {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
  border: 2rpx solid #e2e8f0;
  border-radius: 16rpx;
  padding: 48rpx;
  background: #fff;
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
  transition: box-shadow 0.2s ease;
}
.field-card:hover { box-shadow: 0 8rpx 24rpx rgba(15, 23, 42, 0.08); }
.low-confidence { border-left: 8rpx solid #ba1a1a; }
.field-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 16rpx; }
.field-title-row { display: flex; align-items: center; gap: 16rpx; }
.field-title {
  color: #565e74;
  font-size: 28rpx;
  font-weight: 600;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}
.confidence-badge {
  flex-shrink: 0;
  border-radius: 8rpx;
  padding: 4rpx 16rpx;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}
.confidence-badge.high { background: rgba(0, 98, 66, 0.1); color: #006242; }
.confidence-badge.medium { background: rgba(0, 74, 198, 0.1); color: #004ac6; }
.confidence-badge.low { background: rgba(186, 26, 26, 0.1); color: #ba1a1a; }

.form-grid { display: grid; gap: 32rpx; }
.form-field { display: flex; flex-direction: column; gap: 8rpx; }
.field-label { color: #434655; font-size: 24rpx; font-weight: 500; line-height: 1.33; }
.text-input {
  min-height: 80rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  padding: 0 24rpx;
  color: #0b1c30;
  font-size: 32rpx;
  font-weight: 400;
  background: #fff;
  box-sizing: border-box;
}
.text-input.warning {
  border-color: #ffdad6;
  background: rgba(255, 218, 214, 0.1);
}

.experience-item {
  border: 2rpx solid rgba(195, 198, 215, 0.3);
  border-radius: 16rpx;
  padding: 32rpx;
  background: #eff4ff;
}
.experience-top { display: flex; align-items: center; justify-content: space-between; gap: 16rpx; margin-bottom: 16rpx; }
.experience-title { color: #0b1c30; font-size: 28rpx; font-weight: 600; line-height: 1.14; }
.experience-time { display: block; color: #434655; font-size: 28rpx; line-height: 1.43; }
.experience-desc { display: block; margin-top: 24rpx; color: #434655; font-size: 28rpx; line-height: 1.43; }

.add-button {
  min-height: 80rpx;
  border: 4rpx dashed #c3c6d7;
  border-radius: 16rpx;
  color: #434655;
  background: #fff;
  font-size: 28rpx;
  font-weight: 600;
}

.project-list { display: flex; flex-direction: column; gap: 24rpx; }
.project-item {
  border: 2rpx solid rgba(195, 198, 215, 0.3);
  border-radius: 16rpx;
  padding: 28rpx;
  background: #f8f9ff;
}
.project-title { display: block; color: #0b1c30; font-size: 28rpx; font-weight: 600; }
.project-desc { display: block; margin-top: 12rpx; color: #434655; font-size: 28rpx; line-height: 1.43; }

.skill-list { display: flex; flex-wrap: wrap; gap: 16rpx; }
.skill-pill {
  display: flex;
  align-items: center;
  gap: 8rpx;
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  background: #2563eb;
  color: #fff;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}
.add-skill {
  border: 2rpx solid #004ac6;
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  color: #004ac6;
  font-size: 24rpx;
  font-weight: 500;
}

.missing-card {
  border: 2rpx solid #fde68a;
  border-radius: 16rpx;
  padding: 32rpx;
  background: #fffbeb;
}
.missing-title { color: #92400e; font-size: 28rpx; font-weight: 600; }
.missing-list { display: flex; flex-direction: column; gap: 12rpx; margin-top: 16rpx; color: #92400e; font-size: 28rpx; line-height: 1.43; }

.sticky-action {
  position: sticky;
  bottom: 0;
  display: flex;
  flex-direction: column;
  gap: 24rpx;
  padding: 32rpx 0 48rpx;
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(12px);
}
.secondary-actions { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16rpx; }

@media (min-width: 768px) {
  .review-shell { flex-direction: row; min-height: calc(100vh - 160px); }
  .document-panel, .review-panel { width: 50%; height: calc(100vh - 160px); }
  .document-panel { padding: 40px; gap: 12px; }
  .panel-heading { font-size: 20px; line-height: 28px; }
  .tool-button { width: 40px; height: 40px; border-radius: 8px; }
  .paper-content { padding: 48px; }
  .resume-name { font-size: 30px; margin-bottom: 16px; }
  .contact-row { font-size: 14px; margin-bottom: 32px; gap: 16px; }
  .resume-section-title { font-size: 18px; }
  .resume-line-between, .resume-subline, .resume-bullets { font-size: 14px; }
  .review-inner { padding: 40px; gap: 24px; }
  .review-title { font-size: 24px; line-height: 32px; }
  .status-badge { font-size: 14px; padding: 4px 12px; }
  .review-desc { font-size: 16px; line-height: 24px; }
  .field-card { padding: 24px; border-radius: 12px; gap: 16px; }
  .field-title { font-size: 14px; }
  .confidence-badge { font-size: 12px; }
  .field-label { font-size: 12px; }
  .text-input { min-height: 40px; font-size: 16px; border-radius: 8px; }
  .form-grid { grid-template-columns: repeat(2, 1fr); gap: 16px; }
  .form-field.full { grid-column: span 2; }
}
</style>
