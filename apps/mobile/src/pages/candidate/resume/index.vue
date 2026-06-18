<template>
  <view class="page candidate-flow-page resume-page">
    <AppTopNav active="求职者流程" />

    <ProcessingOverlay
      v-if="generating"
      title="AI 正在生成简历..."
      :description="`正在优化「${currentVersionMeta.title}」，请稍候。这通常需要 1-2 分钟。`"
      tag="生成简历版本"
    />

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
            <view class="rule-link" @tap="openRules">
              <AppIcon name="info" :size="18" color="#004ac6" />
              <text>查看简历优化规则</text>
            </view>
          </view>
        </aside>

        <section class="preview-panel">
          <view class="preview-toolbar">
            <view class="tabs">
              <text class="tab" :class="{ active: activeTab === 'preview' }" @tap="activeTab = 'preview'">预览界面</text>
              <text class="tab" :class="{ active: activeTab === 'log' }" @tap="activeTab = 'log'">渲染日志</text>
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

          <view v-else-if="activeTab === 'log'" class="render-log">
            <view v-if="!currentVersion" class="log-empty">
              <AppIcon name="info" :size="28" color="#94a3b8" />
              <text>暂无渲染日志，请先生成简历版本。</text>
            </view>
            <template v-else>
              <view class="log-grid">
                <view v-for="row in renderLog" :key="row.label" class="log-row">
                  <text class="log-label">{{ row.label }}</text>
                  <text class="log-value">{{ row.value }}</text>
                </view>
              </view>
              <view class="log-section">
                <text class="log-section-title">生成提示 / 警告</text>
                <view v-if="currentVersion.warnings.length === 0" class="log-line ok">
                  <AppIcon name="check_circle" :size="16" color="#006242" filled />
                  <text>无警告，内容已通过校验。</text>
                </view>
                <view v-for="(w, i) in currentVersion.warnings" :key="i" class="log-line warn">
                  <AppIcon name="info" :size="16" color="#ad6800" />
                  <text>{{ w }}</text>
                </view>
              </view>
              <view class="log-section">
                <text class="log-section-title">引用证据 ID</text>
                <view v-if="currentVersion.usedEvidenceIds.length === 0" class="log-line">
                  <text>本次生成未显式引用证据条目。</text>
                </view>
                <view v-else class="log-ids">
                  <text v-for="id in currentVersion.usedEvidenceIds" :key="id" class="log-id">{{ id }}</text>
                </view>
              </view>
            </template>
          </view>

          <view v-else class="preview-stage">
            <view class="resume-preview content-preview">
              <view class="content-head">
                <text class="content-title">{{ currentVersion?.title || currentVersionMeta.title }}</text>
                <view class="content-meta">
                  <AppTag :label="currentVersionMeta.badge" :tone="currentVersionMeta.tone" />
                  <AppTag :label="confidenceLabel" :tone="confidenceTone" />
                </view>
              </view>

              <view v-if="currentVersion?.warnings?.length" class="content-warnings">
                <view v-for="(warning, idx) in currentVersion.warnings" :key="idx" class="warning-row">
                  <AppIcon name="info" :size="16" color="#ad6800" />
                  <text>{{ warning }}</text>
                </view>
              </view>

              <text class="content-body">{{ currentVersion?.content || '暂无内容' }}</text>

              <view class="preview-footer">
                <text>{{ currentVersionMeta.title }} · AI 生成</text>
                <text class="mode-pill">可复制 / 导出</text>
              </view>
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
          <text class="metric-label">AI 综合评分</text>
          <view class="metric-row"><text class="metric-value">{{ matchScore }}</text></view>
        </view>
        <view class="divider desktop-only" />
        <view class="role-block">
          <text class="metric-label">针对性建议岗位</text>
          <view class="role-tags">
            <AppTag v-for="role in recommendedRoles" :key="role" :label="role" tone="gray" />
            <text v-if="!recommendedRoles.length" class="role-empty">暂无推荐岗位</text>
          </view>
        </view>
        <button class="evidence-link" @tap="openEvidence">
          <text>查看优化证据</text>
          <AppIcon name="arrow_forward" :size="18" color="#004ac6" />
        </button>
      </section>
    </view>

    <view v-if="drawerOpen" class="rz-mask" @tap="closeDrawer">
      <view class="rz-drawer" @tap.stop>
        <view class="rz-head">
          <text class="rz-title">{{ drawerMode === 'rules' ? '简历优化规则' : '优化证据' }}</text>
          <view class="rz-close" @tap="closeDrawer">
            <AppIcon name="close" :size="20" color="#565e74" />
          </view>
        </view>
        <scroll-view scroll-y class="rz-body">
          <template v-if="drawerMode === 'rules'">
            <text class="rz-subtitle">{{ currentRules.title }}</text>
            <text class="rz-hint">不同版本针对不同投递环节做了差异化优化，以下为「{{ currentVersionMeta.title }}」的优化策略：</text>
            <view v-for="(rule, i) in currentRules.rules" :key="i" class="rz-rule">
              <AppIcon name="check_circle" :size="18" color="#004ac6" filled />
              <text>{{ rule }}</text>
            </view>
          </template>

          <template v-else>
            <text class="rz-hint">本版本在生成时引用了以下证据进行优化（来自简历校对与 AI 访谈）：</text>
            <view v-if="usedEvidence.matched.length === 0 && usedEvidence.ids.length === 0" class="rz-empty">
              <AppIcon name="info" :size="28" color="#94a3b8" />
              <text>本版本暂无显式引用的优化证据，优化主要基于简历校对与岗位匹配分析。</text>
            </view>
            <template v-else>
              <view v-for="ev in usedEvidence.matched" :key="ev.id" class="rz-evi">
                <view class="rz-evi-head">
                  <AppIcon
                    :name="ev.source === 'interview' ? 'chat' : 'paperclip'"
                    :size="16"
                    :color="ev.source === 'interview' ? '#004ac6' : '#006242'"
                  />
                  <text>{{ ev.source === 'interview' ? '面试证据' : '简历证据' }} · {{ ev.id }}</text>
                </view>
                <text class="rz-evi-snippet">{{ ev.snippet || '无片段内容' }}</text>
              </view>
              <view v-if="usedEvidence.unmatchedIds.length" class="rz-evi">
                <text class="rz-evi-snippet">其他引用证据 ID：{{ usedEvidence.unmatchedIds.join('、') }}</text>
              </view>
            </template>
          </template>
        </scroll-view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { CANDIDATE_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import type { ResumeVersionKey } from '@ai-talent-agent/shared';
import {
  ApiClientError,
  ensureResumeVersionForActiveJourney,
  getTalentProfileForActiveJourney,
  type ApiTalentProfile,
  type ResumeVersion,
} from '@ai-talent-agent/api';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import AppIcon from '../../../components/AppIcon.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import ProcessingOverlay from '../../../components/ProcessingOverlay.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { copyText, showToast } from '../../../utils/feedback';

const selectedVersion = ref<ResumeVersionKey>('ats');
const generating = ref(false);
const generateError = ref(false);
const exporting = ref(false);

const versionCache = reactive<Record<string, ResumeVersion>>({});
const profile = ref<ApiTalentProfile | null>(null);

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
const currentVersion = computed<ResumeVersion | null>(() => versionCache[selectedVersion.value] ?? null);

const confidenceLabel = computed(() => {
  switch (currentVersion.value?.confidence) {
    case 'high':
      return '高置信度';
    case 'low':
      return '低置信度 · 建议核对';
    default:
      return '中等置信度';
  }
});
const confidenceTone = computed(() => {
  switch (currentVersion.value?.confidence) {
    case 'high':
      return 'green' as const;
    case 'low':
      return 'amber' as const;
    default:
      return 'blue' as const;
  }
});

const matchScore = computed(() => {
  const score = profile.value?.overallScore;
  return typeof score === 'number' ? `${score}%` : '—';
});
const recommendedRoles = computed(() => profile.value?.recommendedRoles ?? []);

const activeTab = ref<'preview' | 'log'>('preview');
const drawerOpen = ref(false);
const drawerMode = ref<'rules' | 'evidence'>('rules');

const OPTIMIZATION_RULES: Record<ResumeVersionKey, { title: string; rules: string[] }> = {
  ats: {
    title: 'ATS 版本 · 机器筛选优化规则',
    rules: [
      '使用标准章节标题（工作经历 / 教育背景 / 技能），确保 ATS 正确解析结构。',
      '关键词与目标岗位 JD 对齐，提升简历在招聘系统中的匹配得分。',
      '采用纯文本排版，避免表格、图片、文本框等导致解析失败的元素。',
      '量化成果（百分比、金额、数量），突出可被检索的硬性指标。',
    ],
  },
  hr: {
    title: 'HR 版本 · 人工阅读优化规则',
    rules: [
      '亮点前置：将与目标岗位最匹配的经历与成果放在首屏。',
      '突出业务影响与结果，便于 HR 在数十秒内做出判断。',
      '适度的视觉层次（加粗、分组、留白），提升人工阅读体验。',
      '控制篇幅在 1-2 页，避免信息过载与重复表述。',
    ],
  },
  platform: {
    title: '平台简介 · 投递专用优化规则',
    rules: [
      '精炼为短简介，突出 3-5 个核心标签与代表性亮点。',
      '适配招聘平台 / 内推渠道的字段长度限制。',
      '强调岗位方向与可投递性，弱化冗长细节。',
    ],
  },
  email: {
    title: '邮件正文 · 自荐优化规则',
    rules: [
      '以自荐口吻组织正文，开头点明目标岗位与核心匹配点。',
      '简短有力，控制在 3-4 段，便于 HR 快速浏览或转发。',
      '结尾给出明确的下一步（附简历、期望沟通方式）。',
    ],
  },
};
const currentRules = computed(() => OPTIMIZATION_RULES[selectedVersion.value]);

const usedEvidence = computed(() => {
  const ids = currentVersion.value?.usedEvidenceIds ?? [];
  const all = profile.value?.evidence ?? [];
  const matched = all.filter((ev) => ids.includes(ev.id));
  const matchedIds = new Set(matched.map((ev) => ev.id));
  const unmatchedIds = ids.filter((id) => !matchedIds.has(id));
  return { ids, matched, unmatchedIds };
});

function formatTime(iso?: string) {
  if (!iso) return '—';
  const date = new Date(iso);
  if (Number.isNaN(date.getTime())) return iso;
  return date.toLocaleString('zh-CN', { hour12: false });
}

const renderLog = computed(() => {
  const version = currentVersion.value;
  if (!version) return [] as Array<{ label: string; value: string }>;
  return [
    { label: '版本', value: `${version.title}（${version.versionKey}）` },
    { label: '生成时间', value: formatTime(version.generatedAt) },
    { label: '内容格式', value: version.contentFormat === 'markdown' ? 'Markdown' : '纯文本' },
    { label: '置信度', value: confidenceLabel.value },
    { label: '正文字数', value: `${version.content.length} 字` },
    { label: '引用证据', value: `${version.usedEvidenceIds.length} 条` },
  ];
});

function openRules() {
  drawerMode.value = 'rules';
  drawerOpen.value = true;
}

function openEvidence() {
  drawerMode.value = 'evidence';
  drawerOpen.value = true;
}

function closeDrawer() {
  drawerOpen.value = false;
}

async function loadVersion(key: ResumeVersionKey, regenerate = false) {
  generateError.value = false;
  generating.value = true;
  try {
    const version = await ensureResumeVersionForActiveJourney(key, { regenerate });
    versionCache[key] = version;
  } catch (error) {
    generateError.value = true;
    const message = error instanceof ApiClientError ? error.message : '简历版本生成失败';
    showToast(message);
  } finally {
    generating.value = false;
  }
}

async function selectVersion(key: ResumeVersionKey) {
  if (selectedVersion.value === key && !generateError.value) return;
  selectedVersion.value = key;
  if (versionCache[key]) {
    generateError.value = false;
    return;
  }
  await loadVersion(key);
}

function copyCurrent() {
  if (!currentVersion.value) {
    showToast('请先等待简历版本生成完成');
    return;
  }
  copyText(currentVersion.value.content, '简历内容已复制');
}

function escapeHtml(text: string) {
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function buildFileName(ext: string) {
  const name = profile.value?.candidate?.name?.trim() || '简历';
  return `${name}_${currentVersionMeta.value.title}.${ext}`;
}

function triggerDownload(blob: Blob, filename: string) {
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
  setTimeout(() => URL.revokeObjectURL(url), 1000);
}

function exportWord() {
  const version = currentVersion.value;
  if (!version) {
    showToast('请先等待简历版本生成完成');
    return;
  }
  if (typeof document === 'undefined') {
    copyText(version.content, '当前环境不支持下载，已复制内容');
    return;
  }
  exporting.value = true;
  try {
    const html = `<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40"><head><meta charset="utf-8"><title>${escapeHtml(version.title)}</title></head><body><pre style="font-family:'微软雅黑','Microsoft YaHei',sans-serif;font-size:12pt;line-height:1.7;white-space:pre-wrap;word-break:break-word;">${escapeHtml(version.content)}</pre></body></html>`;
    const blob = new Blob(['\ufeff', html], { type: 'application/msword' });
    triggerDownload(blob, buildFileName('doc'));
    showToast('Word 文件已开始下载', 'success');
  } finally {
    exporting.value = false;
  }
}

function exportPdf() {
  const version = currentVersion.value;
  if (!version) {
    showToast('请先等待简历版本生成完成');
    return;
  }
  if (typeof window === 'undefined' || typeof document === 'undefined') {
    copyText(version.content, '当前环境不支持导出，已复制内容');
    return;
  }
  const printWindow = window.open('', '_blank');
  if (!printWindow) {
    showToast('请允许浏览器弹出窗口以导出 PDF');
    return;
  }
  printWindow.document.write(
    `<html><head><meta charset="utf-8"><title>${escapeHtml(version.title)}</title><style>@page{margin:18mm;}body{font-family:'微软雅黑','Microsoft YaHei',sans-serif;color:#111;}pre{white-space:pre-wrap;word-break:break-word;font-size:14px;line-height:1.75;margin:0;}</style></head><body><pre>${escapeHtml(version.content)}</pre><script>window.onload=function(){setTimeout(function(){window.print();},200);};<\/script></body></html>`,
  );
  printWindow.document.close();
  showToast('已打开打印窗口，请在弹窗中选择“另存为 PDF”');
}

async function regenerate() {
  await loadVersion(selectedVersion.value, true);
}

onMounted(async () => {
  getTalentProfileForActiveJourney()
    .then((data) => {
      profile.value = data;
    })
    .catch(() => {
      profile.value = null;
    });
  await loadVersion(selectedVersion.value);
});
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

/* Render log */
.render-log { min-height: 760rpx; padding: 48rpx; background: rgba(203,219,245,0.22); display: flex; flex-direction: column; gap: 32rpx; }
.log-empty { display: flex; flex-direction: column; align-items: center; gap: 18rpx; padding: 120rpx 48rpx; color: #94a3b8; font-size: 26rpx; text-align: center; }
.log-grid { display: flex; flex-direction: column; border: 2rpx solid #dbe1ff; border-radius: 16rpx; background: #fff; overflow: hidden; }
.log-row { display: flex; justify-content: space-between; gap: 24rpx; padding: 22rpx 28rpx; border-bottom: 2rpx solid #eef1f8; }
.log-row:last-child { border-bottom: none; }
.log-label { color: #565e74; font-size: 24rpx; }
.log-value { color: #0b1c30; font-size: 24rpx; font-weight: 700; text-align: right; }
.log-section { display: flex; flex-direction: column; gap: 14rpx; }
.log-section-title { color: #0b1c30; font-size: 26rpx; font-weight: 900; }
.log-line { display: flex; align-items: flex-start; gap: 10rpx; color: #565e74; font-size: 24rpx; line-height: 1.6; }
.log-line.ok { color: #006242; }
.log-line.warn { color: #ad6800; }
.log-ids { display: flex; flex-wrap: wrap; gap: 12rpx; }
.log-id { border-radius: 8rpx; padding: 6rpx 16rpx; background: #eff4ff; color: #004ac6; font-size: 22rpx; font-family: monospace; }

/* Optimization drawer */
.rz-mask { position: fixed; inset: 0; z-index: 200; display: flex; justify-content: flex-end; background: rgba(11,28,48,0.45); }
.rz-drawer { display: flex; flex-direction: column; width: 88%; max-width: 720rpx; height: 100%; background: #fff; box-shadow: -16rpx 0 48rpx rgba(15,23,42,0.18); animation: rz-in 0.25s ease; }
@keyframes rz-in { from { transform: translateX(100%); } to { transform: translateX(0); } }
.rz-head { display: flex; align-items: center; justify-content: space-between; gap: 16rpx; padding: 32rpx; border-bottom: 2rpx solid #e2e8f0; }
.rz-title { color: #0b1c30; font-size: 32rpx; font-weight: 900; }
.rz-close { display: flex; align-items: center; justify-content: center; width: 64rpx; height: 64rpx; border-radius: 999rpx; background: #f2f4f6; }
.rz-body { flex: 1; min-height: 0; padding: 32rpx; box-sizing: border-box; }
.rz-subtitle { display: block; color: #004ac6; font-size: 28rpx; font-weight: 900; margin-bottom: 12rpx; }
.rz-hint { display: block; color: #565e74; font-size: 24rpx; line-height: 1.6; margin-bottom: 24rpx; }
.rz-rule { display: flex; align-items: flex-start; gap: 14rpx; margin-bottom: 20rpx; border: 2rpx solid #e2e8f0; border-radius: 16rpx; padding: 24rpx; background: #f8fafc; color: #434655; font-size: 26rpx; line-height: 1.6; }
.rz-empty { display: flex; flex-direction: column; align-items: center; gap: 18rpx; padding: 96rpx 48rpx; color: #94a3b8; font-size: 26rpx; text-align: center; }
.rz-evi { display: flex; flex-direction: column; gap: 16rpx; margin-bottom: 24rpx; border: 2rpx solid #e2e8f0; border-radius: 16rpx; padding: 28rpx; background: #f8fafc; }
.rz-evi-head { display: flex; align-items: center; gap: 10rpx; color: #004ac6; font-size: 24rpx; font-weight: 700; }
.rz-evi-snippet { color: #434655; font-size: 26rpx; line-height: 1.65; }
.content-preview { display: flex; flex-direction: column; gap: 28rpx; padding: 56rpx; color: #111827; }
.content-head { display: flex; flex-direction: column; gap: 16rpx; border-bottom: 2rpx solid #eef1f8; padding-bottom: 24rpx; }
.content-title { color: #0b1c30; font-size: 40rpx; font-weight: 900; line-height: 1.3; }
.content-meta { display: flex; flex-wrap: wrap; gap: 12rpx; }
.content-warnings { display: flex; flex-direction: column; gap: 12rpx; border-radius: 16rpx; padding: 24rpx; background: #fff7e6; }
.warning-row { display: flex; align-items: flex-start; gap: 10rpx; color: #ad6800; font-size: 24rpx; line-height: 1.6; }
.content-body { color: #1f2937; font-size: 26rpx; line-height: 1.85; white-space: pre-wrap; word-break: break-word; }
.role-empty { color: #909399; font-size: 24rpx; }
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
