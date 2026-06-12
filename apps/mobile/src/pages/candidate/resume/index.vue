<template>
  <view class="page candidate-flow-page resume-page">
    <AppTopNav active="求职者流程" />

    <view class="container resume-container">
      <ProgressSteps v-bind="createFlowStepsProps(CANDIDATE_FLOW, 4)" navigable />

      <section class="resume-header">
        <view>
          <view class="breadcrumb">
            <text>我的简历</text>
            <AppIcon name="chevron_right" :size="16" color="#565e74" />
            <text class="current">导出生成</text>
          </view>
          <text class="page-title">生成您的专属简历版本</text>
          <text class="page-desc">基于 AI 深度分析，为您提供多维度的求职解决方案。</text>
        </view>
        <view class="export-actions">
          <button class="flow-btn flow-btn--secondary" :disabled="exporting" @tap="copyCurrent">
            <AppIcon name="content_copy" :size="20" color="#434655" />
            <text>复制内容</text>
          </button>
          <button class="flow-btn flow-btn--secondary" :disabled="exporting" @tap="exportWord">
            <AppIcon name="description" :size="20" color="#434655" />
            <text>导出 Word</text>
          </button>
          <button class="flow-btn flow-btn--primary" :disabled="exporting" @tap="exportPdf">
            <AppIcon name="download" :size="20" color="#ffffff" />
            <text>{{ exporting ? '导出中...' : '导出 PDF' }}</text>
          </button>
        </view>
      </section>

      <view class="resume-grid">
        <aside class="version-column">
          <view
            v-for="version in versions"
            :key="version.key"
            class="version-card"
            :class="{ active: selectedVersion === version.key }"
            @tap="selectVersion(version.key)"
          >
            <view class="version-top">
              <view class="version-icon">
                <AppIcon :name="version.icon" :size="24" color="#004ac6" :filled="version.filled" />
              </view>
              <AppTag :label="version.badge" :tone="version.tone" />
            </view>
            <text class="version-title">{{ version.title }}</text>
            <text class="version-desc">{{ version.desc }}</text>
            <view v-if="selectedVersion === version.key" class="selected-row">
              <text>当前选中</text>
              <AppIcon name="check_circle" :size="18" color="#004ac6" filled />
            </view>
          </view>

          <view class="export-note">
            <text class="note-title">关于导出</text>
            <text class="note-desc">所有版本均由 AI 针对您的目标岗位进行深度优化，确保在不同筛选环节中展现最佳状态。</text>
            <view class="rule-link">
              <AppIcon name="info" :size="18" color="#004ac6" />
              <text>查看简历优化规则</text>
            </view>
          </view>
        </aside>

        <section class="preview-panel">
          <view class="preview-toolbar">
            <view class="tabs">
              <text class="tab active">预览界面</text>
              <text class="tab">渲染日志</text>
            </view>
            <view class="zoom desktop-only">
              <AppIcon name="zoom_out" :size="18" color="#565e74" />
              <text>85%</text>
              <AppIcon name="zoom_in" :size="18" color="#565e74" />
              <AppIcon name="fullscreen" :size="18" color="#565e74" />
            </view>
          </view>

          <view v-if="generateError" class="preview-error">
            <StatePanel tone="error" icon="info" icon-color="#ba1a1a" title="生成失败" description="当前版本暂时无法渲染，请重试或切换其他版本。">
              <button class="flow-btn flow-btn--primary retry-btn" @tap="regenerate">重新生成</button>
            </StatePanel>
          </view>

          <view v-else-if="generating" class="preview-loading">
            <StatePanel tone="loading" icon="loop" icon-color="#004ac6" title="正在生成简历版本" :description="`正在优化 ${currentVersionMeta.title}...`" />
          </view>

          <view v-else class="preview-stage">
            <view v-if="selectedVersion === 'ats'" class="ats-preview resume-preview">
              <view class="preview-head">
                <text class="ats-name">张伟 (Felix)</text>
                <text class="ats-meta">高级全栈工程师 | 北京, 中国 | felix.zhang@example.com</text>
              </view>
              <view class="ats-section">
                <text class="ats-section-title">核心总结</text>
                <text class="ats-text">拥有 8 年以上构建可扩展 SaaS 架构的经验。精通 React、Node.js 和分布式系统，在大型科技公司有成功领导跨职能团队的记录。</text>
              </view>
              <view class="ats-section">
                <text class="ats-section-title">工作经历</text>
                <view class="ats-job"><view class="job-row"><text>科技巨头解决方案 - 资深负责人</text><text>2020 - 至今</text></view></view>
                <view class="ats-list">
                  <text>• 使用 Golang 构建了每分钟处理 100 万次以上请求的微服务架构。</text>
                  <text>• 领导分布在 3 个时区的 15 名开发人员组成的跨职能团队。</text>
                  <text>• 通过系统重构将核心服务的延迟降低了 35%。</text>
                </view>
              </view>
              <view class="ats-section">
                <text class="ats-section-title">专业技能</text>
                <text class="ats-text">JavaScript, TypeScript, Python, Go, Kubernetes, AWS, SQL, NoSQL.</text>
              </view>
              <view class="preview-footer"><text>ATS 优化版本 v2.4</text><text class="mode-pill">文本优先模式</text></view>
            </view>

            <view v-else-if="selectedVersion === 'hr'" class="hr-preview resume-preview">
              <view class="hr-sidebar">
                <view class="hr-avatar">张</view>
                <text class="hr-name">张伟</text>
                <text class="hr-title">高级全栈工程师</text>
                <view class="hr-block">
                  <text class="hr-block-title">联系方式</text>
                  <text>felix.z@ai.com</text>
                  <text>138-0000-0000</text>
                </view>
                <view class="radar-mini"><view class="radar-fill" /></view>
              </view>
              <view class="hr-content">
                <text class="hr-section-title">个人总结</text>
                <text class="hr-quote">“致力于通过人工智能与前沿工程实践解决复杂商业问题，拥有卓越的技术前瞻性与团队领导力。”</text>
                <text class="hr-section-title">核心项目</text>
                <view class="project-line"><text class="project-title">云端分布式架构重构</text><text class="project-desc">负责公司核心系统的微服务转型，提升了 40% 的吞吐效率。</text></view>
                <view class="project-line muted"><text class="project-title">AI 智能人才评估系统</text><text class="project-desc">主导开发基于 NLP 的简历解析引擎，准确率达到 98.5%。</text></view>
                <text class="created-by">Created by AI Talent Agent</text>
              </view>
            </view>

            <view v-else-if="selectedVersion === 'platform'" class="platform-preview resume-preview">
              <text class="platform-name">张伟 | 高级架构师 · 8 年经验</text>
              <text class="platform-meta">上海 · 清华大学 · 计算机科学与技术</text>
              <view class="platform-section">
                <text class="platform-label">一句话亮点</text>
                <text class="platform-text">擅长分布式系统与云原生架构，具备从 0 到 1 推动复杂技术方案落地的经验。</text>
              </view>
              <view class="platform-section">
                <text class="platform-label">核心技能</text>
                <view class="platform-tags">
                  <AppTag label="Kubernetes" tone="gray" />
                  <AppTag label="Node.js" tone="gray" />
                  <AppTag label="PostgreSQL" tone="gray" />
                  <AppTag label="系统设计" tone="gray" />
                </view>
              </view>
              <view class="platform-section">
                <text class="platform-label">适合岗位</text>
                <text class="platform-text">后端架构师 · 技术专家 · 云原生平台负责人</text>
              </view>
              <view class="preview-footer"><text>平台简介版本 v1.0</text><text class="mode-pill">招聘平台专用</text></view>
            </view>

            <view v-else class="email-preview resume-preview">
              <text class="email-subject">主题：候选人自荐 - 张伟（高级架构师）</text>
              <text class="email-line">您好，</text>
              <text class="email-line">基于目标岗位匹配分析，我整理了以下核心信息供您快速了解：</text>
              <text class="email-line">• 8 年互联网架构经验，主导微服务转型与核心系统升级</text>
              <text class="email-line">• 技术栈覆盖 Kubernetes、Node.js、PostgreSQL 与系统设计</text>
              <text class="email-line">• 综合匹配度 94.8%，适合架构师 / 技术专家方向</text>
              <text class="email-line">完整简历见附件，期待进一步沟通。谢谢！</text>
              <text class="email-sign">张伟</text>
              <view class="preview-footer"><text>邮件正文版本</text><text class="mode-pill">可直接发送</text></view>
            </view>
          </view>

          <view class="preview-controls">
            <text>第一页</text>
            <view class="page-pill"><text>‹</text><text>1 / 1</text><text>›</text></view>
            <text>最后一页</text>
          </view>
        </section>
      </view>

      <section class="evidence-strip">
        <view class="match-block">
          <text class="metric-label">AI 岗位匹配度预测</text>
          <view class="metric-row"><text class="metric-value">94.8%</text><AppTag label="极高匹配" tone="green" /></view>
        </view>
        <view class="divider desktop-only" />
        <view class="role-block">
          <text class="metric-label">针对性建议岗位</text>
          <view class="role-tags"><AppTag label="后端架构师" tone="gray" /><AppTag label="技术专家" tone="gray" /></view>
        </view>
        <button class="evidence-link" @tap="showEvidence">
          <text>查看优化证据</text>
          <AppIcon name="arrow_forward" :size="18" color="#004ac6" />
        </button>
      </section>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { CANDIDATE_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import { mockResumeVersionTexts, type ResumeVersionKey } from '@ai-talent-agent/shared';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import AppIcon from '../../../components/AppIcon.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { copyText, runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';

const selectedVersion = ref<ResumeVersionKey>('ats');
const generating = ref(false);
const generateError = ref(false);
const exporting = ref(false);

const versions = [
  {
    key: 'ats' as const,
    title: 'ATS 版本',
    badge: '高通过率',
    tone: 'green' as const,
    icon: 'terminal',
    filled: true,
    desc: '针对招聘系统算法优化，结构化文本优先，确保解析无误。',
  },
  {
    key: 'hr' as const,
    title: 'HR 版本',
    badge: '视觉优先',
    tone: 'blue' as const,
    icon: 'palette',
    filled: true,
    desc: '视觉美观、重点突出，适合 HR 人工快速阅读。',
  },
  {
    key: 'platform' as const,
    title: '平台简介',
    badge: '投递专用',
    tone: 'gray' as const,
    icon: 'language',
    filled: false,
    desc: '适合招聘网站、内推平台和猎头渠道的短简介版本。',
  },
  {
    key: 'email' as const,
    title: '邮件正文',
    badge: '可直接发送',
    tone: 'amber' as const,
    icon: 'mail',
    filled: false,
    desc: '用于邮件自荐或 HR 转发的正文模板，突出匹配亮点。',
  },
];

const currentVersionMeta = computed(() => versions.find((v) => v.key === selectedVersion.value) || versions[0]);

async function selectVersion(key: ResumeVersionKey) {
  if (selectedVersion.value === key) return;
  generateError.value = false;
  selectedVersion.value = key;
  generating.value = true;
  await simulateDelay(600);
  generating.value = false;
}

function copyCurrent() {
  copyText(mockResumeVersionTexts[selectedVersion.value], '简历内容已复制');
}

async function exportWord() {
  exporting.value = true;
  await runAsyncAction(
    () => simulateDelay(1200),
    { success: `${currentVersionMeta.value.title} Word 已生成`, fail: 'Word 导出失败，请重试' },
  );
  exporting.value = false;
}

async function exportPdf() {
  exporting.value = true;
  const ok = await runAsyncAction(
    () => simulateDelay(1500),
    { loading: '正在导出 PDF', success: `${currentVersionMeta.value.title} PDF 已保存`, fail: 'PDF 导出失败，请重试' },
  );
  exporting.value = false;
  if (!ok) generateError.value = true;
}

async function regenerate() {
  generateError.value = false;
  generating.value = true;
  const ok = await runAsyncAction(
    () => simulateDelay(1000),
    { success: '简历版本已重新生成' },
  );
  generating.value = false;
  if (!ok) generateError.value = true;
}

function showEvidence() {
  showToast('证据链：简历校对 + AI 访谈 + 岗位匹配分析');
}
</script>

<style lang="scss" scoped>
.resume-container { display: flex; flex-direction: column; gap: 40rpx; }
.resume-header { display: flex; flex-direction: column; gap: 28rpx; }
.breadcrumb { display: flex; align-items: center; gap: 12rpx; margin-bottom: 12rpx; color: #565e74; font-size: 24rpx; }
.current { color: #004ac6; }
.page-title { display: block; color: #0b1c30; font-size: 52rpx; font-weight: 900; line-height: 1.2; }
.page-desc { display: block; margin-top: 14rpx; color: #565e74; font-size: 28rpx; line-height: 1.7; }
.export-actions { display: flex; flex-wrap: wrap; gap: 16rpx; }
.resume-grid { display: grid; gap: 32rpx; }
.version-column { display: flex; flex-direction: column; gap: 24rpx; }
.version-card { position: relative; display: flex; flex-direction: column; gap: 18rpx; border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 32rpx; background: #fff; }
.version-card.active { border-color: #004ac6; background: rgba(219,225,255,0.28); box-shadow: 0 8rpx 28rpx rgba(0,74,198,0.1); }
.version-top { display: flex; justify-content: space-between; gap: 18rpx; align-items: flex-start; }
.version-icon { display: flex; align-items: center; justify-content: center; width: 72rpx; height: 72rpx; border-radius: 16rpx; background: #e5eeff; color: #004ac6; font-size: 24rpx; font-weight: 900; }
.version-title { color: #0b1c30; font-size: 34rpx; font-weight: 900; }
.version-desc { color: #565e74; font-size: 26rpx; line-height: 1.65; }
.selected-row { display: flex; align-items: center; gap: 8rpx; color: #004ac6; font-size: 26rpx; font-weight: 600; }
.rule-link { display: flex; align-items: center; gap: 12rpx; }
.export-note { border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 32rpx; background: #eff4ff; }
.note-title { display: block; color: #0b1c30; font-weight: 900; margin-bottom: 18rpx; }
.note-desc { display: block; color: #565e74; font-size: 26rpx; line-height: 1.65; }
.rule-link { margin-top: 22rpx; color: #004ac6; font-size: 24rpx; font-weight: 600; }
.preview-panel { overflow: hidden; display: flex; flex-direction: column; border: 2rpx solid #c3c6d7; border-radius: 24rpx; background: #fff; box-shadow: 0 6rpx 22rpx rgba(0,74,198,0.04); }
.preview-toolbar { display: flex; align-items: center; justify-content: space-between; gap: 20rpx; min-height: 96rpx; padding: 0 32rpx; border-bottom: 2rpx solid #c3c6d7; background: #fff; }
.tabs { display: flex; gap: 36rpx; }
.tab { color: #565e74; font-size: 26rpx; font-weight: 900; }
.tab.active { color: #004ac6; }
.zoom { display: flex; align-items: center; gap: 16rpx; color: #565e74; font-weight: 600; }
.preview-loading, .preview-error { min-height: 760rpx; display: flex; align-items: center; justify-content: center; padding: 48rpx; background: rgba(203,219,245,0.22); }
.retry-btn { margin-top: 12rpx; min-height: 72rpx; padding: 0 32rpx; }
.preview-stage { overflow-x: auto; min-height: 760rpx; padding: 48rpx; background: rgba(203,219,245,0.22); }
.platform-preview, .email-preview { display: flex; flex-direction: column; gap: 24rpx; padding: 56rpx; color: #111827; }
.platform-name { font-size: 38rpx; font-weight: 900; }
.platform-meta { color: #565e74; font-size: 24rpx; }
.platform-section { display: flex; flex-direction: column; gap: 12rpx; }
.platform-label { color: #004ac6; font-size: 24rpx; font-weight: 900; }
.platform-text { color: #374151; font-size: 24rpx; line-height: 1.65; }
.platform-tags { display: flex; flex-wrap: wrap; gap: 12rpx; }
.email-subject { color: #0b1c30; font-size: 28rpx; font-weight: 900; }
.email-line { color: #374151; font-size: 24rpx; line-height: 1.7; }
.email-sign { margin-top: 24rpx; color: #0b1c30; font-weight: 900; }
.resume-preview { width: 680rpx; min-height: 940rpx; margin: 0 auto; background: #fff; box-shadow: 0 18rpx 42rpx rgba(11,28,48,0.14); }
.ats-preview { display: flex; flex-direction: column; gap: 36rpx; padding: 56rpx; color: #111827; }
.preview-head { display: flex; flex-direction: column; gap: 8rpx; }
.ats-name { color: #000; font-size: 42rpx; font-weight: 900; letter-spacing: -1rpx; text-transform: uppercase; }
.ats-meta { color: #4b5563; font-size: 22rpx; }
.ats-section { display: flex; flex-direction: column; gap: 10rpx; }
.ats-section-title { border-bottom: 2rpx solid #111; color: #111; font-size: 24rpx; font-weight: 900; text-transform: uppercase; }
.ats-text, .ats-list { color: #374151; font-size: 22rpx; line-height: 1.6; }
.ats-list { display: flex; flex-direction: column; gap: 8rpx; }
.job-row { display: flex; justify-content: space-between; gap: 20rpx; color: #111; font-size: 22rpx; font-weight: 900; }
.preview-footer { margin-top: auto; display: flex; align-items: center; justify-content: space-between; gap: 16rpx; border-top: 2rpx solid #f3f4f6; padding-top: 24rpx; color: #9ca3af; font-size: 20rpx; }
.mode-pill { border-radius: 8rpx; padding: 4rpx 12rpx; background: #f3f4f6; color: #4b5563; font-weight: 900; }
.hr-preview { display: grid; grid-template-columns: 1fr 2fr; overflow: hidden; }
.hr-sidebar { display: flex; flex-direction: column; align-items: center; padding: 40rpx 28rpx; background: #0b1c30; color: #fff; }
.hr-avatar { display: flex; align-items: center; justify-content: center; width: 140rpx; height: 140rpx; margin-bottom: 28rpx; border-radius: 999rpx; background: #dbe1ff; color: #004ac6; font-size: 42rpx; font-weight: 900; }
.hr-name { font-size: 36rpx; font-weight: 900; }
.hr-title { color: #b4c5ff; font-size: 22rpx; margin-bottom: 48rpx; }
.hr-block { align-self: stretch; display: flex; flex-direction: column; gap: 12rpx; color: rgba(255,255,255,0.86); font-size: 20rpx; }
.hr-block-title { color: #b4c5ff; font-size: 20rpx; font-weight: 900; letter-spacing: 2rpx; }
.radar-mini { position: relative; width: 180rpx; height: 180rpx; margin-top: 48rpx; border: 2rpx solid rgba(255,255,255,0.24); border-radius: 999rpx; }
.radar-fill { position: absolute; inset: 28rpx; background: rgba(0,74,198,0.45); clip-path: polygon(50% 0%, 100% 38%, 82% 100%, 18% 100%, 0% 38%); }
.hr-content { display: flex; flex-direction: column; padding: 56rpx; }
.hr-section-title { align-self: flex-start; border-bottom: 4rpx solid #dbe1ff; padding-right: 28rpx; margin-bottom: 22rpx; color: #004ac6; font-size: 28rpx; font-weight: 900; }
.hr-quote { margin-bottom: 48rpx; color: #565e74; font-size: 24rpx; line-height: 1.7; font-style: italic; }
.project-line { position: relative; display: flex; flex-direction: column; gap: 8rpx; margin-bottom: 34rpx; border-left: 4rpx solid #c3c6d7; padding-left: 24rpx; }
.project-title { color: #0b1c30; font-size: 24rpx; font-weight: 900; }
.project-desc { color: #565e74; font-size: 22rpx; line-height: 1.55; }
.project-line.muted { opacity: 0.78; }
.created-by { margin-top: auto; align-self: flex-end; color: #737686; font-size: 20rpx; font-style: italic; }
.preview-controls { display: flex; align-items: center; justify-content: center; gap: 32rpx; min-height: 96rpx; border-top: 2rpx solid #c3c6d7; background: #fff; color: #565e74; font-size: 24rpx; font-weight: 800; }
.page-pill { display: flex; align-items: center; gap: 28rpx; border-radius: 999rpx; padding: 10rpx 24rpx; background: #e5eeff; color: #0b1c30; }
.evidence-strip { display: flex; flex-direction: column; gap: 24rpx; border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 32rpx; background: #fff; box-shadow: 0 6rpx 20rpx rgba(0,74,198,0.04); }
.metric-label { color: #565e74; font-size: 24rpx; }
.metric-row { display: flex; align-items: center; gap: 14rpx; margin-top: 8rpx; }
.metric-value { color: #004ac6; font-size: 48rpx; font-weight: 900; }
.divider { width: 2rpx; height: 96rpx; background: #c3c6d7; }
.role-tags { display: flex; flex-wrap: wrap; gap: 12rpx; margin-top: 12rpx; }
.evidence-link {
  display: flex;
  align-items: center;
  gap: 8rpx;
  align-self: flex-start;
  color: #004ac6;
  background: transparent;
  font-weight: 600;
}
@media (min-width: 768px) {
  .resume-header { flex-direction: row; justify-content: space-between; align-items: flex-end; }
  .resume-grid { grid-template-columns: 4fr 8fr; }
  .evidence-strip { flex-direction: row; align-items: center; justify-content: space-between; }
  .match-block, .role-block { flex: 1; }
}
</style>
