<template>
  <view class="page export-page">
    <AppTopNav active="HR 工作台" />

    <view class="container export-container">
      <ProgressSteps v-bind="createFlowStepsProps(HR_FLOW, 4)" navigable />

      <section class="export-header">
        <view class="header-copy">
          <text class="page-title">HR 数据导出工作站</text>
          <text class="page-desc">将 AI 筛选结果快速同步至您的现有工作流 (ATS, Excel, 企业微信)</text>
        </view>
        <view class="header-actions">
          <button class="hr-secondary-action hr-secondary-action--surface" @tap="showHistory">
            <AppIcon name="history" :size="18" color="#0b1c30" />
            <text>导出历史</text>
          </button>
          <button class="hr-primary-action" :disabled="exporting" @tap="generateAtsPack">
            <AppIcon name="cloud_upload" :size="18" color="#ffffff" />
            <text>{{ exporting ? '生成中...' : '生成 ATS 导入包' }}</text>
          </button>
        </view>
      </section>

      <StatePanel
        v-if="exportStatus === 'failed'"
        tone="error"
        icon="info"
        icon-color="#ba1a1a"
        title="导出失败"
        description="文件生成过程中断，可能是网络波动或格式暂不支持。请重试或切换导出格式。"
      >
        <button class="hr-primary-action retry-export" @tap="downloadExcel">重试下载</button>
      </StatePanel>

      <view class="export-grid">
        <aside class="left-panel">
          <section class="glass-panel card-block">
            <view class="section-title">
              <AppIcon name="unarchive" :size="20" color="#004ac6" />
              <text>导出格式</text>
            </view>
            <view class="format-list">
              <view
                v-for="format in formats"
                :key="format.name"
                class="format-item"
                :class="{ active: activeFormat === format.name }"
                @tap="selectFormat(format.name)"
              >
                <view class="format-copy">
                  <AppIcon
                    :name="format.icon"
                    :size="20"
                    :color="activeFormat === format.name ? '#004ac6' : '#565e74'"
                  />
                  <text>{{ format.name }}</text>
                </view>
                <view class="radio-dot" :class="{ checked: activeFormat === format.name }" />
              </view>
            </view>
          </section>

          <section class="glass-panel card-block field-panel">
            <view class="card-title-row">
              <view class="section-title">
                <AppIcon name="checklist" :size="20" color="#004ac6" />
                <text>字段预览与筛选</text>
              </view>
              <button class="select-all" @tap="selectAllFields">全选</button>
            </view>
            <scroll-view class="field-scroll" scroll-y>
              <view
                v-for="field in fields"
                :key="field.label"
                class="field-row"
                :class="{ dimmed: field.dimmed }"
              >
                <view class="field-copy">
                  <AppIcon name="drag_indicator" :size="16" color="#737686" />
                  <text>{{ field.label }}</text>
                </view>
                <checkbox
                  :checked="field.checked"
                  :disabled="field.dimmed"
                  color="#004ac6"
                  @tap="toggleField(field.label)"
                />
              </view>
            </scroll-view>
          </section>
        </aside>

        <main class="preview-panel">
          <section class="preview-card">
            <view class="preview-toolbar">
              <view class="preview-tabs">
                <button
                  class="tab-btn"
                  :class="{ active: previewTab === 'data' }"
                  @tap="previewTab = 'data'"
                >
                  数据视图
                </button>
                <button
                  class="tab-btn"
                  :class="{ active: previewTab === 'render' }"
                  @tap="previewTab = 'render'"
                >
                  渲染预览
                </button>
              </view>
              <view class="preview-status">
                <AppIcon name="visibility" :size="16" color="#565e74" />
                <text>正在预览前 10 条数据 (共 156 条)</text>
              </view>
            </view>

            <scroll-view v-if="previewTab === 'data'" scroll-x class="table-scroll">
              <view class="table-canvas">
                <view class="data-table">
                  <view class="data-table-head">
                    <view class="data-table-row">
                      <text
                        v-for="col in columns"
                        :key="col"
                        class="table-cell"
                        :class="`col-${colKey(col)}`"
                      >
                        {{ col }}
                      </text>
                    </view>
                  </view>
                  <view class="data-table-body">
                    <view
                      v-for="(row, index) in tableRows"
                      :key="`${row.index}-${index}`"
                      class="data-table-row"
                      :class="{ stripe: index % 2 === 1, filler: row.filler }"
                    >
                      <text class="table-cell col-index">{{ row.index }}</text>
                      <text class="table-cell col-name">{{ row.name }}</text>
                      <view class="table-cell col-score">
                        <text v-if="row.score" class="score-pill">{{ row.score }}</text>
                        <text v-else class="filler-text">...</text>
                      </view>
                      <text class="table-cell col-highlight">{{ row.highlight }}</text>
                      <text class="table-cell col-salary">{{ row.salary }}</text>
                      <text
                        class="table-cell col-risk"
                        :class="{
                          danger: row.riskTone === 'error',
                          muted: row.riskTone === 'normal' && row.risk === '无',
                        }"
                      >
                        {{ row.risk }}
                      </text>
                    </view>
                  </view>
                </view>
              </view>
            </scroll-view>

            <view v-else class="render-preview">
              <text class="render-title">候选人推荐摘要</text>
              <text>岗位：高级架构师（Cloud & AI）</text>
              <text>推荐：林子杰，匹配分 94。</text>
              <text>理由：大型分布式系统经验、技术栈匹配、求职意向明确。</text>
              <text>风险：英文口语待验证。</text>
              <text>建议面试问题：请说明一次分布式系统一致性问题的解决方案。</text>
            </view>

            <view class="action-grid">
              <button class="action-tile" :disabled="exporting" @tap="downloadExcel">
                <AppIcon name="download" :size="32" color="#004ac6" />
                <text>{{ exporting ? '导出中...' : '下载 Excel' }}</text>
              </button>
              <button class="action-tile" @tap="copyEmailBody">
                <AppIcon name="content_copy" :size="32" color="#565e74" />
                <text>复制邮件正文</text>
              </button>
              <button class="action-tile" @tap="copyWecomSummary">
                <AppIcon name="share" :size="32" color="#565e74" />
                <text>复制协作摘要</text>
              </button>
              <button class="action-tile" @tap="downloadJson">
                <AppIcon name="code" :size="32" color="#565e74" />
                <text>下载 JSON</text>
              </button>
            </view>
          </section>

          <view class="integration-bar">
            <view class="integration-badges">
              <text class="badge fs">FS</text>
              <text class="badge wc">WC</text>
              <text class="badge ats">ATS</text>
            </view>
            <text class="integration-copy">
              <text class="integration-strong">连接器就绪:</text>
              飞书, 企业微信, Moka ATS 已通过 Webhook 授权连接。
            </text>
            <button class="manage-link" @tap="manageIntegration">管理集成</button>
          </view>

          <text class="demo-link" @tap="enableFailDemo">查看导出失败示例</text>
        </main>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { HR_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { copyText, runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';

const formats = [
  { name: 'Excel / CSV 报表', icon: 'table_view' },
  { name: '邮件通知正文', icon: 'mail' },
  { name: '飞书 / 企微摘要', icon: 'chat' },
  { name: '面试评价表 (PDF)', icon: 'description' },
  { name: '标准 JSON 格式', icon: 'terminal' },
];

const activeFormat = ref(formats[0].name);
const previewTab = ref<'data' | 'render'>('data');
const exporting = ref(false);
const exportStatus = ref<'idle' | 'success' | 'failed'>('idle');
const failNextExport = ref(false);

const fields = ref([
  { label: '姓名 / 手机号', checked: true, dimmed: false },
  { label: 'AI 综合评分 (Confidence)', checked: true, dimmed: false },
  { label: '核心技能匹配度', checked: true, dimmed: false },
  { label: '履历关键节点提取', checked: true, dimmed: false },
  { label: '简历原始附件链接', checked: false, dimmed: false },
  { label: 'GitHub/作品集 深度分析', checked: false, dimmed: true },
]);

const columns = ['序号', '人才名称', 'AI 评分', '匹配关键点', '当前薪资 (估)', '风险预警'];

const tableRows = [
  {
    index: '1',
    name: '张三 (Senior Dev)',
    score: '94%',
    highlight: '7年后端架构, 云原生专家',
    salary: '45k/mo',
    risk: '无',
    riskTone: 'normal',
    filler: false,
  },
  {
    index: '2',
    name: '李四 (Product Manager)',
    score: '88%',
    highlight: '50w+ DAU 产品经验',
    salary: '38k/mo',
    risk: '跳槽频繁 (近1年3换)',
    riskTone: 'error',
    filler: false,
  },
  {
    index: '3',
    name: '王五 (UI/UX Designer)',
    score: '91%',
    highlight: 'B端 SaaS 设计, 交互专家',
    salary: '32k/mo',
    risk: '无',
    riskTone: 'normal',
    filler: false,
  },
  {
    index: '4',
    name: '赵六 (Data Scientist)',
    score: '85%',
    highlight: 'NLP 方向, 顶会论文 2 篇',
    salary: '55k/mo',
    risk: '英语口语一般',
    riskTone: 'normal',
    filler: false,
  },
  { index: '5', name: '...', score: '', highlight: '...', salary: '...', risk: '...', riskTone: 'normal', filler: true },
  { index: '6', name: '...', score: '', highlight: '...', salary: '...', risk: '...', riskTone: 'normal', filler: true },
];

const wecomSummary = computed(() => [
  '候选人推荐摘要',
  '岗位：高级架构师（Cloud & AI）',
  '推荐：林子杰，匹配分 94。',
  '理由：大型分布式系统经验、技术栈匹配、求职意向明确。',
  '风险：英文口语待验证。',
  '建议面试问题：请说明一次分布式系统一致性问题的解决方案。',
].join('\n'));

const emailBody = computed(() => [
  '主题：候选人推荐 - 高级架构师（Cloud & AI）',
  '',
  '您好，',
  '',
  '基于 AI 筛选结果，向您推荐以下候选人：',
  '1. 林子杰 - 匹配分 94% - 7年后端架构, 云原生专家',
  '2. 王佳敏 - 匹配分 88% - 分布式系统与学术背景突出',
  '',
  '详细推荐理由、风险点与面试问题请见附件导出包。',
  '',
  'AI Talent Agent',
].join('\n'));

function colKey(label: string) {
  if (label === '序号') return 'index';
  if (label === '人才名称') return 'name';
  if (label === 'AI 评分') return 'score';
  if (label === '匹配关键点') return 'highlight';
  if (label === '当前薪资 (估)') return 'salary';
  return 'risk';
}

function selectFormat(name: string) {
  activeFormat.value = name;
  showToast(`已切换为 ${name}`);
}

function toggleField(label: string) {
  fields.value = fields.value.map((field) => {
    if (field.label !== label || field.dimmed) return field;
    return { ...field, checked: !field.checked };
  });
}

function selectAllFields() {
  fields.value = fields.value.map((field) => ({
    ...field,
    checked: field.dimmed ? field.checked : true,
  }));
  showToast('已全选可用字段', 'success');
}

function showHistory() {
  showToast('暂无历史导出记录');
}

async function generateAtsPack() {
  exporting.value = true;
  await runAsyncAction(() => simulateDelay(1300), {
    loading: '生成 ATS 包',
    success: 'ATS 导入包已生成',
  });
  exporting.value = false;
}

function copyWecomSummary() {
  copyText(wecomSummary.value, '协作摘要已复制');
}

function copyEmailBody() {
  copyText(emailBody.value, '邮件正文已复制');
}

async function runExportAction(successMessage: string) {
  exporting.value = true;
  exportStatus.value = 'idle';
  const shouldFail = failNextExport.value;
  failNextExport.value = false;
  const ok = await runAsyncAction(
    async () => {
      await simulateDelay(1500);
      if (shouldFail) throw new Error('export failed');
    },
    {
      loading: '正在打包导出',
      success: successMessage,
      fail: '导出失败，请重试',
    },
  );
  exporting.value = false;
  exportStatus.value = ok ? 'success' : 'failed';
}

function downloadExcel() {
  runExportAction('Excel 导出包已下载到本地');
}

function downloadJson() {
  runExportAction('JSON 文件已下载到本地');
}

function manageIntegration() {
  showToast('集成管理（MVP 占位）');
}

function enableFailDemo() {
  failNextExport.value = true;
  showToast('下次下载将模拟失败');
}
</script>

<style lang="scss" scoped>
.export-container {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  padding-bottom: 48rpx;
}

.export-header {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
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
  color: #565e74;
  font-size: 32rpx;
  line-height: 1.5;
}

.header-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 24rpx;
}

.export-grid {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.left-panel,
.preview-panel {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.glass-panel,
.preview-card {
  border: 2rpx solid #e2e8f0;
  border-radius: 16rpx;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
}

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

.format-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  margin-top: 32rpx;
}

.format-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 24rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
}

.format-item.active {
  border-color: #004ac6;
  background: rgba(219, 225, 255, 0.1);
}

.format-copy {
  display: flex;
  align-items: center;
  gap: 24rpx;
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}

.radio-dot {
  width: 32rpx;
  height: 32rpx;
  border: 4rpx solid #737686;
  border-radius: 999rpx;
  box-sizing: border-box;
}

.radio-dot.checked {
  border-color: #004ac6;
  background: radial-gradient(circle, #004ac6 45%, transparent 46%);
}

.card-title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 24rpx;
  margin-bottom: 32rpx;
}

.select-all {
  margin: 0;
  padding: 0;
  color: #004ac6;
  background: transparent;
  font-size: 24rpx;
  font-weight: 500;
  &::after { border: none; }
}

.field-panel {
  display: flex;
  flex-direction: column;
  min-height: 480rpx;
}

.field-scroll {
  max-height: 800rpx;
}

.field-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  margin-bottom: 24rpx;
  padding: 24rpx;
  border: 2rpx solid rgba(195, 198, 215, 0.3);
  border-radius: 16rpx;
  background: #eff4ff;
}

.field-row.dimmed {
  opacity: 0.6;
}

.field-copy {
  display: flex;
  align-items: center;
  gap: 16rpx;
  color: #0b1c30;
  font-size: 28rpx;
  line-height: 1.43;
}

.preview-card {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 1200rpx;
}

.preview-toolbar {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  padding: 32rpx;
  border-bottom: 2rpx solid #c3c6d7;
  background: #eff4ff;
}

.preview-tabs {
  display: flex;
  gap: 32rpx;
}

.tab-btn {
  margin: 0;
  padding: 8rpx 32rpx 16rpx;
  background: transparent;
  color: #565e74;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
  border-bottom: 4rpx solid transparent;
  &::after { border: none; }
}

.tab-btn.active {
  color: #004ac6;
  border-bottom-color: #004ac6;
}

.preview-status {
  display: flex;
  align-items: center;
  gap: 8rpx;
  color: #565e74;
  font-size: 24rpx;
  line-height: 1.33;
}

.table-scroll {
  width: 100%;
  flex: 1;
  background: #fff;
}

.table-canvas {
  min-width: 100%;
  padding: 48rpx;
  box-sizing: border-box;
}

.data-table {
  width: 100%;
  min-width: 1080rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 0;
  overflow: hidden;
}

.data-table-row {
  display: grid;
  grid-template-columns: 72rpx 1.15fr 0.72fr 1.55fr 0.82fr 1fr;
}

.data-table-head .table-cell {
  background: #e5eeff;
  color: #565e74;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}

.data-table-head .col-index {
  justify-content: center;
}

.table-cell {
  display: flex;
  align-items: center;
  min-height: 88rpx;
  padding: 24rpx;
  border-right: 2rpx solid #c3c6d7;
  border-bottom: 2rpx solid #c3c6d7;
  color: #434655;
  font-size: 28rpx;
  line-height: 1.43;
  text-align: left;
  box-sizing: border-box;
}

.data-table-row .table-cell:last-child {
  border-right: none;
}

.data-table-body .data-table-row:last-child .table-cell {
  border-bottom: none;
}

.data-table-row.stripe .table-cell {
  background: #f8f9ff;
}

.col-index {
  justify-content: center;
  color: #434655;
}

.col-name {
  font-weight: 600;
  color: #0b1c30;
}

.col-score {
  justify-content: flex-start;
}

.col-risk.muted {
  color: #565e74;
}

.col-risk.danger {
  color: #ba1a1a;
}

.score-pill {
  display: inline-flex;
  align-items: center;
  border-radius: 999rpx;
  padding: 4rpx 16rpx;
  background: rgba(0, 98, 66, 0.1);
  color: #006242;
  font-size: 28rpx;
  font-weight: 700;
  line-height: 1.14;
}

.filler-text {
  color: #737686;
}

.data-table-row.filler .table-cell {
  color: #737686;
}

.render-preview {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  flex: 1;
  padding: 48rpx;
  background: #fff;
  color: #434655;
  font-size: 28rpx;
  line-height: 1.62;
}

.render-title {
  color: #0b1c30;
  font-weight: 600;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 32rpx;
  padding: 48rpx;
  border-top: 2rpx solid #c3c6d7;
  background: #eff4ff;
}

.action-tile {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
  margin: 0;
  min-height: 160rpx;
  padding: 32rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
  &::after { border: none; }
}

.integration-bar {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
  padding: 32rpx;
  border: 2rpx solid rgba(195, 198, 215, 0.3);
  border-radius: 16rpx;
  background: rgba(211, 228, 254, 0.5);
}

.integration-badges {
  display: flex;
  align-items: center;
}

.badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 64rpx;
  height: 64rpx;
  margin-left: -8rpx;
  border: 4rpx solid #fff;
  border-radius: 999rpx;
  color: #fff;
  font-size: 20rpx;
  font-weight: 700;
  line-height: 1;
}

.badge:first-child {
  margin-left: 0;
}

.badge.fs { background: #3b82f6; }
.badge.wc { background: #22c55e; }
.badge.ats { background: #f97316; }

.integration-copy {
  color: #565e74;
  font-size: 28rpx;
  line-height: 1.43;
}

.integration-strong {
  color: #0b1c30;
  font-weight: 600;
}

.manage-link {
  align-self: flex-start;
  margin: 0;
  padding: 0;
  background: transparent;
  color: #004ac6;
  font-size: 24rpx;
  font-weight: 500;
  text-decoration: underline;
  &::after { border: none; }
}

.retry-export {
  margin-top: 16rpx;
}

.demo-link {
  align-self: flex-end;
  color: #737686;
  font-size: 24rpx;
  text-decoration: underline;
}

@media (min-width: 768px) {
  .export-header {
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-end;
  }

  .page-title {
    font-size: 30px;
    line-height: 38px;
  }

  .page-desc {
    font-size: 16px;
    line-height: 24px;
  }

  .export-grid {
    flex-direction: row;
    align-items: flex-start;
    gap: 24px;
  }

  .left-panel {
    width: 33.333%;
    flex-shrink: 0;
    gap: 24px;
  }

  .preview-panel {
    flex: 1;
    min-width: 0;
    gap: 24px;
  }

  .card-block {
    padding: 24px;
    border-radius: 16px;
  }

  .section-title {
    font-size: 18px;
  }

  .format-copy,
  .field-copy,
  .tab-btn,
  .action-tile,
  .integration-copy {
    font-size: 14px;
  }

  .preview-toolbar {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 16px;
  }

  .preview-card {
    min-height: 600px;
    border-radius: 16px;
  }

  .table-canvas {
    padding: 24px;
  }

  .data-table {
    min-width: 100%;
  }

  .data-table-row {
    grid-template-columns: 48px 1.2fr 88px 1.6fr 104px 1.1fr;
  }

  .table-cell {
    min-height: 44px;
    padding: 12px;
    font-size: 14px;
  }

  .data-table-head .table-cell {
    font-size: 12px;
  }

  .score-pill {
    font-size: 14px;
    padding: 2px 8px;
  }

  .action-grid {
    grid-template-columns: repeat(4, 1fr);
    padding: 24px;
    gap: 16px;
  }

  .action-tile {
    min-height: 96px;
    border-radius: 12px;
  }

  .integration-bar {
    flex-direction: row;
    align-items: center;
    padding: 16px;
    border-radius: 12px;
  }

  .manage-link {
    margin-left: auto;
    align-self: center;
  }

  .field-scroll {
    max-height: 400px;
  }
}
</style>
