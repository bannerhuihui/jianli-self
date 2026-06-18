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
            <text v-if="resumeFile?.fileName" class="file-name-tag">{{ resumeFile.fileName }}</text>
            <view class="tool-button" @tap="openOriginal"><AppIcon name="open_in_new" :size="20" color="#434655" /></view>
          </view>
        </view>

        <!-- 图片原件直接预览 -->
        <scroll-view v-if="previewKind === 'image'" scroll-y class="resume-paper">
          <image :src="fileUrl" mode="widthFix" class="preview-image" />
        </scroll-view>

        <!-- PDF 原件（H5 内嵌预览） -->
        <!-- #ifdef H5 -->
        <view v-else-if="previewKind === 'pdf'" class="resume-paper">
          <iframe :src="fileUrl" class="preview-frame" frameborder="0" />
        </view>
        <!-- #endif -->

        <!-- 回退：展示结构化解析结果 -->
        <scroll-view v-else scroll-y class="resume-paper">
          <view class="paper-content">
            <view v-if="previewKind === 'unsupported'" class="preview-hint">
              <AppIcon name="info" :size="18" color="#004ac6" />
              <text>该格式暂不支持内嵌预览，可点击右上角在新窗口打开原件。以下为 AI 解析结果。</text>
            </view>
            <text class="resume-name">{{ resume.basicInfo.name }}的简历</text>
            <view class="contact-row">
              <text>{{ resume.basicInfo.phone }}</text>
              <text>{{ resume.basicInfo.email }}</text>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">教育背景</text>
              <text v-if="educationLines.length === 0" class="resume-subline">暂无解析结果</text>
              <text v-for="item in educationLines" :key="item" class="resume-subline">{{ item }}</text>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">工作经历</text>
              <text v-if="workLines.length === 0" class="resume-subline">暂无解析结果</text>
              <view v-else class="resume-bullets">
                <text v-for="item in workLines" :key="item">• {{ item }}</text>
              </view>
            </view>

            <view class="resume-section" v-if="projectLines.length > 0">
              <text class="resume-section-title">项目经历</text>
              <view class="resume-bullets">
                <text v-for="item in projectLines" :key="item">• {{ item }}</text>
              </view>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">专业技能</text>
              <text class="resume-subline">{{ skillLines.join(', ') || '暂无解析结果' }}</text>
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
            <view class="quality-head"><text>解析质量评分</text><text class="quality-score">{{ qualityPercent }}%</text></view>
            <view class="quality-track"><view class="quality-value" :style="{ width: `${qualityPercent}%` }" /></view>
            <text class="quality-note">
              {{ warningLines.length > 0 ? `发现 ${warningLines.length} 条解析提示，建议确认后再进入 AI 访谈。` : '解析结果较好，请核对后进入 AI 访谈。' }}
            </text>
          </view>

          <view class="field-card">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="person" :size="20" color="#004ac6" />
                <text class="field-title">基本信息</text>
              </view>
              <view class="confidence-badge high">置信度: {{ confidenceLabel }}</view>
            </view>
            <view class="form-grid">
              <view class="form-field"><text class="field-label">姓名</text><input class="text-input" :value="resume.basicInfo.name" /></view>
              <view class="form-field"><text class="field-label">职位</text><input class="text-input" :value="resume.basicInfo.title" /></view>
              <view class="form-field"><text class="field-label">所在地</text><input class="text-input" :value="resume.basicInfo.location" /></view>
              <view class="form-field"><text class="field-label">联系电话</text><input class="text-input" :value="resume.basicInfo.phone" /></view>
              <view class="form-field full"><text class="field-label">邮箱</text><input class="text-input" :value="resume.basicInfo.email" /></view>
            </view>
          </view>

          <view class="field-card" :class="{ 'low-confidence': educationLines.length === 0 }">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="school" :size="20" color="#004ac6" />
                <text class="field-title">教育经历</text>
              </view>
              <view class="confidence-badge" :class="educationLines.length === 0 ? 'low' : 'high'">
                {{ educationLines.length === 0 ? '置信度: 低 - 建议复核' : '置信度: 高' }}
              </view>
            </view>
            <view class="form-grid">
              <view v-if="educationLines.length === 0" class="form-field full">
                <text class="field-label">教育背景</text>
                <input class="text-input warning" :value="resume.basicInfo.education" placeholder="请补充教育经历" />
              </view>
              <view v-for="(item, index) in educationLines" :key="`${item}-${index}`" class="form-field full">
                <text class="field-label">教育经历 {{ index + 1 }}</text>
                <input class="text-input" :value="item" />
              </view>
            </view>
          </view>

          <view class="field-card">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="work" :size="20" color="#004ac6" />
                <text class="field-title">工作经历</text>
              </view>
              <view class="confidence-badge" :class="workLines.length === 0 ? 'low' : 'high'">
                {{ workLines.length === 0 ? '置信度: 低 - 建议复核' : '置信度: 高' }}
              </view>
            </view>
            <view v-if="workLines.length === 0" class="experience-item">
              <text class="experience-desc">暂未解析到工作经历，请手动补充。</text>
            </view>
            <view v-for="(item, index) in workLines" :key="`${item}-${index}`" class="experience-item">
              <view class="experience-top">
                <text class="experience-title">经历 {{ index + 1 }}</text>
                <AppIcon name="edit" :size="18" color="#737686" />
              </view>
              <text class="experience-desc">{{ item }}</text>
            </view>
            <button class="add-button">+ 添加工作经历</button>
          </view>

          <view class="field-card">
            <view class="field-head">
              <view class="field-title-row">
                <AppIcon name="article" :size="20" color="#004ac6" />
                <text class="field-title">项目经历</text>
              </view>
              <view class="confidence-badge medium">置信度: {{ projectLines.length > 0 ? '中' : '低' }}</view>
            </view>
            <view class="project-list">
              <view v-if="projectLines.length === 0" class="project-item">
                <text class="project-desc">暂未解析到项目经历，可在访谈环节补充。</text>
              </view>
              <view v-for="(project, index) in projectLines" :key="`${project}-${index}`" class="project-item">
                <text class="project-title">{{ project }}</text>
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
              <view v-for="skill in skillLines" :key="skill" class="skill-pill">
                <text>{{ skill }}</text>
                <AppIcon name="close" :size="14" color="#ffffff" />
              </view>
              <text v-if="skillLines.length === 0" class="add-skill">暂无技能标签</text>
              <text v-else class="add-skill">+ 添加</text>
            </view>
          </view>

          <view class="missing-card" v-if="warningLines.length > 0 || missingLines.length > 0">
            <text class="missing-title">解析提示</text>
            <view class="missing-list">
              <text v-for="item in warningLines" :key="`w-${item}`">• {{ item }}</text>
              <text v-for="item in missingLines" :key="`m-${item}`">• 缺失字段：{{ item }}</text>
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
import { computed, onMounted, ref } from 'vue';
import type { ApiStructuredResume, ResumeFileUpload } from '@ai-talent-agent/api';
import { getResumeFileForActiveJourney, getStructuredResumeForActiveJourney } from '@ai-talent-agent/api';
import { mockResume } from '@ai-talent-agent/shared';
import { CANDIDATE_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { showToast } from '../../../utils/feedback';

const resume = ref<ApiStructuredResume>(mockResume as ApiStructuredResume);
const resumeFile = ref<ResumeFileUpload | null>(null);
const loading = ref(true);
const loaded = ref(false);

const IMAGE_EXTS = ['jpg', 'jpeg', 'png', 'webp', 'gif', 'bmp'];

const fileUrl = computed(() => resumeFile.value?.fileUrl ?? '');
const previewKind = computed<'image' | 'pdf' | 'unsupported' | 'fallback'>(() => {
  const file = resumeFile.value;
  if (!file || !file.fileUrl) {
    return 'fallback';
  }
  const type = (file.fileType || '').toLowerCase();
  const url = file.fileUrl.toLowerCase();
  if (type.includes('pdf') || url.includes('.pdf')) {
    return 'pdf';
  }
  if (IMAGE_EXTS.some((ext) => type.includes(ext) || url.includes(`.${ext}`))) {
    return 'image';
  }
  return 'unsupported';
});

function openOriginal() {
  if (!fileUrl.value) {
    showToast('暂无可打开的原始文件');
    return;
  }
  if (typeof window !== 'undefined' && typeof window.open === 'function') {
    window.open(fileUrl.value, '_blank');
    return;
  }
  uni.setClipboardData({
    data: fileUrl.value,
    success: () => showToast('原件链接已复制'),
  });
}

function nonEmpty(items?: string[]) {
  return (items ?? []).filter((item) => item.trim().length > 0);
}

const qualityPercent = computed(() => Math.round((resume.value.parseQualityScore ?? 0) * 100));
const educationLines = computed(() => {
  const fromList = nonEmpty(resume.value.education);
  if (fromList.length > 0) {
    return fromList;
  }
  if (resume.value.basicInfo.education?.trim()) {
    return [resume.value.basicInfo.education];
  }
  return [];
});
const workLines = computed(() => nonEmpty(resume.value.workExperience));
const projectLines = computed(() => nonEmpty(resume.value.projects));
const skillLines = computed(() => nonEmpty(resume.value.skills));
const warningLines = computed(() => nonEmpty(resume.value.warnings));
const missingLines = computed(() => nonEmpty(resume.value.missingFields));
const confidenceLabel = computed(() => {
  const level = resume.value.confidence ?? 'medium';
  if (level === 'high') return '高';
  if (level === 'low') return '低';
  return '中';
});

onMounted(async () => {
  if (loaded.value) {
    return;
  }
  loaded.value = true;
  try {
    resume.value = await getStructuredResumeForActiveJourney();
  } catch {
    showToast('加载解析结果失败，请重新上传');
  } finally {
    loading.value = false;
  }

  getResumeFileForActiveJourney()
    .then((file) => {
      resumeFile.value = file;
    })
    .catch(() => {
      resumeFile.value = null;
    });
});

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
.preview-image { display: block; width: 100%; }
.preview-frame { display: block; width: 100%; height: 100%; min-height: 640rpx; border: none; background: #fff; }
.file-name-tag {
  max-width: 360rpx;
  overflow: hidden;
  color: #434655;
  font-size: 24rpx;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.preview-hint {
  display: flex;
  align-items: flex-start;
  gap: 10rpx;
  margin-bottom: 32rpx;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
  background: #eff4ff;
  color: #004ac6;
  font-size: 24rpx;
  line-height: 1.5;
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
