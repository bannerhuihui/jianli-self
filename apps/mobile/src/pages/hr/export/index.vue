<template>
  <view class="page export-page">
    <AppTopNav active="HR 工作台" />
    <view class="container export-container">
      <ProgressSteps :steps="hrSteps" :active-index="4" />

      <section class="export-header">
        <view><text class="page-title">HR 数据导出工作站</text><text class="page-desc">将 AI 筛选结果快速同步至现有工作流：ATS、Excel、企业微信和邮件。</text></view>
        <view class="header-actions"><button class="secondary-action" @tap="showHistory"><AppIcon name="loop" :size="18" />导出历史</button><button class="primary-action" :disabled="exporting" @tap="generateAtsPack"><AppIcon name="cloud-upload" :size="18" color="#ffffff" />{{ exporting ? '生成中...' : '生成 ATS 导入包' }}</button></view>
      </section>

      <StatePanel
        v-if="exportStatus === 'failed'"
        tone="error"
        icon="info"
        icon-color="#ba1a1a"
        title="导出失败"
        description="文件生成过程中断，可能是网络波动或格式暂不支持。请重试或切换导出格式。"
      >
        <button class="primary-action retry-export" @tap="downloadExport">重试下载</button>
      </StatePanel>

      <view class="export-grid">
        <aside class="left-panel">
          <section class="glass-panel card-block">
            <view class="card-title-row"><text class="section-title"><AppIcon name="person" :size="20" color="#004ac6" />候选人选择</text><button class="select-all">全选</button></view>
            <view class="candidate-select-list">
              <view v-for="candidate in selectedCandidates" :key="candidate.name" class="candidate-select-item">
                <view><text class="select-name">{{ candidate.name }}</text><text class="select-meta">匹配分 {{ candidate.score }} · {{ candidate.title }}</text></view>
                <AppIcon name="checkbox-filled" :size="20" color="#007d55" />
              </view>
            </view>
          </section>

          <section class="glass-panel card-block">
            <text class="section-title"><AppIcon name="download" :size="20" color="#004ac6" />导出格式</text>
            <view class="format-list">
              <view v-for="format in formats" :key="format.name" class="format-item" :class="{ active: format.active }" @tap="selectFormat(format.name)"><view class="format-copy"><view class="format-icon"><AppIcon :name="format.icon" :size="20" :color="format.active ? '#004ac6' : '#565e74'" /></view><text>{{ format.name }}</text></view><AppIcon :name="format.active ? 'circle-filled' : 'circle'" :size="18" :color="format.active ? '#004ac6' : '#737686'" /></view>
            </view>
          </section>
          <section class="glass-panel card-block">
            <view class="card-title-row"><text class="section-title"><AppIcon name="list" :size="20" color="#004ac6" />字段预览与筛选</text><button class="select-all">全选</button></view>
            <view class="field-list"><view v-for="field in fields" :key="field" class="field-row"><AppIcon name="bars" :size="18" color="#737686" /><text>{{ field }}</text><AppIcon name="checkmarkempty" :size="18" color="#007d55" /></view></view>
          </section>
        </aside>

        <main class="preview-panel">
          <section class="preview-card">
            <view class="preview-toolbar"><text class="section-title"><AppIcon name="eye" :size="20" color="#004ac6" />Excel / CSV 预览</text><view class="preview-status"><AppIcon name="eye" :size="16" color="#565e74" /><text>正在预览前 10 条数据</text><AppTag label="MVP 优先" tone="green" /></view></view>
            <scroll-view scroll-x class="table-scroll">
              <view class="table">
                <view class="table-row header"><text v-for="col in columns" :key="col">{{ col }}</text></view>
                <view v-for="row in rows" :key="row.name" class="table-row"><text>{{ row.name }}</text><text>{{ row.score }}</text><text>{{ row.reason }}</text><text>{{ row.risk }}</text><text>{{ row.question }}</text></view>
              </view>
            </scroll-view>
          </section>

          <section class="wecom-card">
            <view class="preview-toolbar"><text class="section-title"><AppIcon name="chatboxes" :size="20" color="#004ac6" />企业微信摘要</text><AppTag label="可复制" tone="blue" /></view>
            <view class="wecom-preview">
              <text class="wecom-title">候选人推荐摘要</text>
              <text>岗位：高级架构师（Cloud & AI）</text>
              <text>推荐：林子杰，匹配分 94。</text>
              <text>理由：大型分布式系统经验、技术栈匹配、求职意向明确。</text>
              <text>风险：英语口语待验证。</text>
              <text>建议面试问题：请说明一次分布式系统一致性问题的解决方案。</text>
            </view>
          </section>
        </main>
      </view>

      <section class="bottom-actions">
        <view class="summary"><text class="summary-label">本次导出</text><text class="summary-value">2 位候选人 · 5 类字段 · 3 个推荐问题</text></view>
        <view class="action-group"><navigator url="/pages/hr/candidates/index" class="secondary-action"><AppIcon name="arrow-left" :size="18" />返回推荐列表</navigator><button class="secondary-action" @tap="copyWecomSummary"><AppIcon name="paperclip" :size="18" />复制企业微信摘要</button><button class="primary-action" :disabled="exporting" @tap="downloadExport"><AppIcon name="download" :size="18" color="#ffffff" />{{ exporting ? '导出中...' : '下载导出包' }}</button></view>
        <text class="demo-link" @tap="enableFailDemo">查看导出失败示例</text>
      </section>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import AppTag from '../../../components/AppTag.vue';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { copyText, runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';

const hrSteps = ['岗位需求', '岗位画像', '候选人推荐', '匹配详情', '导出中心'];
const formats = ref([
  { name: 'Excel / CSV 报表', icon: 'list', active: true },
  { name: '邮件通知正文', icon: 'email', active: false },
  { name: '飞书 / 企微摘要', icon: 'chatboxes', active: false },
  { name: '面试评价表 PDF', icon: 'paperclip', active: false },
  { name: '标准 JSON 格式', icon: 'font', active: false },
]);
const exporting = ref(false);
const exportStatus = ref<'idle' | 'success' | 'failed'>('idle');
const failNextExport = ref(false);

const wecomSummary = computed(() => [
  '候选人推荐摘要',
  '岗位：高级架构师（Cloud & AI）',
  '推荐：林子杰，匹配分 94。',
  '理由：大型分布式系统经验、技术栈匹配、求职意向明确。',
  '风险：英语口语待验证。',
  '建议面试问题：请说明一次分布式系统一致性问题的解决方案。',
].join('\n'));

function selectFormat(name: string) {
  formats.value = formats.value.map((item) => ({ ...item, active: item.name === name }));
  showToast(`已切换为 ${name}`);
}

function showHistory() {
  showToast('暂无历史导出记录');
}

async function generateAtsPack() {
  exporting.value = true;
  await runAsyncAction(
    () => simulateDelay(1300),
    { loading: '生成 ATS 包', success: 'ATS 导入包已生成' },
  );
  exporting.value = false;
}

function copyWecomSummary() {
  copyText(wecomSummary.value, '企业微信摘要已复制');
}

async function downloadExport() {
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
      success: '导出包已下载到本地',
      fail: '导出失败，请重试',
    },
  );
  exporting.value = false;
  exportStatus.value = ok ? 'success' : 'failed';
}

function enableFailDemo() {
  failNextExport.value = true;
  showToast('下次下载将模拟失败');
}
const fields = ['候选人姓名', '当前职位', '匹配分', '推荐理由', '风险点', '建议面试问题', '联系方式'];
const columns = ['姓名', '匹配分', '推荐理由', '风险点', '面试问题'];
const rows = [
  { name: '林子杰', score: 94, reason: '大型分布式系统经验', risk: '英语口语待验证', question: '说明一致性问题解决方案' },
  { name: '王佳敏', score: 88, reason: '系统设计能力突出', risk: '到岗时间待确认', question: '确认岗位方向偏好' },
];
const selectedCandidates = [
  { name: '林子杰', score: 94, title: '资深架构师' },
  { name: '王佳敏', score: 88, title: '高级技术专家' },
];
</script>

<style lang="scss" scoped>
.export-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.export-container { display: flex; flex-direction: column; gap: 32rpx; }
.export-header { display: flex; flex-direction: column; gap: 24rpx; }
.page-title { display: block; color: #0b1c30; font-size: 52rpx; font-weight: 900; }
.page-desc { display: block; margin-top: 10rpx; color: #565e74; font-size: 28rpx; line-height: 1.7; }
.header-actions, .action-group { display: flex; flex-wrap: wrap; gap: 14rpx; }
.secondary-action, .primary-action { display: flex; align-items: center; justify-content: center; gap: 10rpx; min-height: 76rpx; border-radius: 16rpx; padding: 0 28rpx; font-size: 26rpx; font-weight: 900; }
.secondary-action { border: 2rpx solid #c3c6d7; background: #fff; color: #0b1c30; }
.primary-action { background: #004ac6; color: #fff; box-shadow: 0 10rpx 24rpx rgba(0,74,198,0.15); }
.export-grid { display: grid; gap: 28rpx; align-items: start; }
.left-panel { display: flex; flex-direction: column; gap: 24rpx; }
.glass-panel, .preview-card, .wecom-card, .bottom-actions { border: 2rpx solid #e2e8f0; border-radius: 24rpx; background: rgba(255,255,255,0.84); box-shadow: 0 8rpx 28rpx rgba(15,23,42,0.04); }
.card-block { padding: 32rpx; }
.section-title { display: flex; align-items: center; gap: 10rpx; color: #0b1c30; font-size: 32rpx; font-weight: 900; }
.candidate-select-list { display: flex; flex-direction: column; gap: 14rpx; margin-top: 24rpx; }
.candidate-select-item { display: flex; align-items: center; justify-content: space-between; gap: 16rpx; border: 2rpx solid #c3c6d7; border-radius: 16rpx; padding: 18rpx; background: #eff4ff; }
.select-name { display: block; color: #0b1c30; font-weight: 900; }
.select-meta { display: block; margin-top: 6rpx; color: #565e74; font-size: 23rpx; }
.format-list { display: flex; flex-direction: column; gap: 14rpx; margin-top: 24rpx; }
.format-item { display: flex; justify-content: space-between; align-items: center; border: 2rpx solid #c3c6d7; border-radius: 16rpx; padding: 20rpx; color: #0b1c30; }
.format-item.active { border-color: #004ac6; background: rgba(219,225,255,0.18); }
.format-copy { display: flex; align-items: center; gap: 16rpx; font-weight: 900; }
.format-icon { display: flex; align-items: center; justify-content: center; width: 44rpx; height: 44rpx; border-radius: 10rpx; background: #eff4ff; }
.card-title-row, .preview-toolbar { display: flex; justify-content: space-between; align-items: center; gap: 20rpx; }
.select-all { color: #004ac6; background: transparent; font-size: 24rpx; font-weight: 900; }
.field-list { display: flex; flex-direction: column; gap: 14rpx; margin-top: 24rpx; }
.field-row { display: grid; grid-template-columns: auto minmax(0,1fr) auto; align-items: center; gap: 14rpx; border: 2rpx solid rgba(195,198,215,0.35); border-radius: 14rpx; padding: 18rpx; background: #eff4ff; color: #0b1c30; font-size: 25rpx; }
.preview-panel { display: flex; flex-direction: column; gap: 24rpx; }
.preview-card, .wecom-card { padding: 32rpx; }
.preview-status { display: flex; flex-wrap: wrap; align-items: center; gap: 10rpx; color: #565e74; font-size: 23rpx; }
.table-scroll { width: 100%; margin-top: 24rpx; }
.table { min-width: 1120rpx; border: 2rpx solid #c3c6d7; border-radius: 16rpx; overflow: hidden; }
.table-row { display: grid; grid-template-columns: 160rpx 120rpx 300rpx 220rpx 320rpx; }
.table-row text { padding: 20rpx; border-right: 2rpx solid #e2e8f0; border-bottom: 2rpx solid #e2e8f0; color: #434655; font-size: 24rpx; line-height: 1.45; }
.table-row.header text { background: #eff4ff; color: #0b1c30; font-weight: 900; }
.wecom-preview { display: flex; flex-direction: column; gap: 14rpx; margin-top: 24rpx; border-radius: 18rpx; padding: 28rpx; background: #eff4ff; color: #434655; font-size: 26rpx; line-height: 1.65; }
.wecom-title { color: #0b1c30; font-weight: 900; }
.bottom-actions { display: flex; flex-direction: column; gap: 22rpx; padding: 28rpx; }
.summary-label { display: block; color: #737686; font-size: 24rpx; }
.summary-value { display: block; margin-top: 8rpx; color: #0b1c30; font-size: 30rpx; font-weight: 900; }
.retry-export { margin-top: 12rpx; }
.demo-link { color: #737686; font-size: 24rpx; text-decoration: underline; text-align: right; }
@media (min-width: 768px) { .export-header, .bottom-actions { flex-direction: row; justify-content: space-between; align-items: flex-end; } .export-grid { grid-template-columns: 4fr 8fr; } }
</style>
