<template>
  <view class="page hr-page">
    <AppTopNav active="HR 工作台" />
    <view class="container candidate-container">
      <ProgressSteps :steps="hrSteps" :active-index="2" />

      <section class="job-summary">
        <view class="summary-head">
          <view>
            <view class="title-row"><text class="job-title">{{ job.title }}</text><AppTag label="招聘中" tone="blue" /></view>
            <view class="job-meta"><text>上海 · 浦东</text><text>50k - 80k · 16薪</text><text>8-10年经验</text><text>统招硕士及以上</text></view>
          </view>
          <view class="job-actions"><button class="secondary-action" @tap="editJd">修改JD</button><button class="primary-action" @tap="reanalyze">重新分析</button></view>
        </view>
        <view class="insight-grid">
          <view class="insight-tile"><text class="tile-label">核心技能要求</text><text class="tile-value">Distributed, K8s, PyTorch</text></view>
          <view class="insight-tile"><text class="tile-label">推荐人选规模</text><text class="tile-value">248 位匹配</text></view>
          <view class="insight-tile"><text class="tile-label">平均匹配度</text><text class="tile-value primary">82%</text></view>
          <view class="insight-tile"><text class="tile-label">最近活跃</text><text class="tile-value">12 位今日上线</text></view>
        </view>
      </section>

      <view class="list-layout">
        <aside class="filter-panel">
          <view class="filter-card">
            <view class="filter-head"><text class="filter-title">筛选条件</text><button class="reset-button" @tap="resetFilters">重置</button></view>
            <view class="filter-group"><text class="filter-label">匹配分区间</text><view class="range-row"><input class="small-input" placeholder="最低" /><text>-</text><input class="small-input" placeholder="最高" /></view></view>
            <view class="filter-group"><text class="filter-label">推荐等级</text><view class="check-list"><text>✓ 卓越匹配 (90%+)</text><text>✓ 高度匹配 (80%-90%)</text><text>□ 中度匹配 (70%-80%)</text></view></view>
            <view class="filter-group"><text class="filter-label">目前状态</text><view class="select-box">不限</view></view>
          </view>
          <view class="ai-tip"><text class="tip-title">AI 寻才建议</text><text class="tip-desc">建议放宽“行业背景”限制。当前 JD 对金融背景要求过高，放宽至互联网头部企业可增加 45% 的优质候选人。</text></view>
        </aside>

        <main class="candidate-list">
          <view class="sort-bar"><text>找到 48 位匹配的候选人</text><text>AI 综合评分 高-低</text></view>
          <view v-for="match in matches" :key="match.candidate.id" class="talent-card">
            <view class="talent-layout">
              <aside class="candidate-side">
                <view class="candidate-main">
                  <view class="avatar">{{ match.candidate.name.slice(0, 1) }}</view>
                  <view class="candidate-info">
                    <view class="candidate-title-row"><text class="candidate-name">{{ match.candidate.name }}</text><AppTag label="在职看机会" tone="green" /></view>
                    <text class="candidate-meta">{{ match.candidate.title }} · {{ match.candidate.location }}</text>
                    <text class="candidate-meta">{{ match.candidate.experienceYears }} 年经验 · {{ match.candidate.education }}</text>
                  </view>
                </view>
                <view class="score-panel">
                  <view class="score-ring"><text>{{ match.totalScore }}</text></view>
                  <view><text class="score-title">{{ match.totalScore >= 90 ? '卓越匹配' : '高度匹配' }}</text><text class="score-caption">Total Match Score</text></view>
                </view>
              </aside>

              <main class="candidate-evidence">
                <view class="breakdown-grid">
                  <view v-for="item in getBreakdownItems(match)" :key="item.label" class="breakdown-tile">
                    <text class="breakdown-label">{{ item.label }}</text>
                    <text class="breakdown-value">{{ item.value }}</text>
                  </view>
                </view>
                <view class="recommend-box">
                  <view class="recommend-title"><AppIcon name="checkmarkempty" :size="18" color="#007d55" /><text>AI 推荐理由</text></view>
                  <view class="reason-list"><text v-for="reason in match.reasons" :key="reason">{{ reason }}</text></view>
                </view>
                <view class="risk-row"><AppTag v-for="risk in match.risks" :key="risk" :label="`待验证：${risk}`" tone="amber" /><AppTag label="高活跃" tone="gray" /><AppTag label="大厂背景" tone="gray" /></view>
              </main>

              <view class="card-actions">
                <navigator url="/pages/hr/match-detail/index" class="detail-link">查看详情</navigator>
                <button class="summary-link" @tap="copySummary(match)"><AppIcon name="paperclip" :size="17" />摘要</button>
                <navigator url="/pages/hr/match-detail/index" class="export-link">查看后加入</navigator>
              </view>
            </view>
          </view>
        </main>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { mockJobProfile, mockMatches } from '@ai-talent-agent/shared';
import type { MatchResult } from '@ai-talent-agent/domain';
import AppIcon from '../../../components/AppIcon.vue';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { copyText, runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';

const hrSteps = ['岗位需求', '岗位画像', '候选人推荐', '匹配详情', '导出中心'];
const job = mockJobProfile;
const matches = mockMatches;

function editJd() {
  showToast('即将跳转 JD 编辑（MVP 占位）');
}

async function reanalyze() {
  await runAsyncAction(
    () => simulateDelay(1200),
    { loading: '重新分析中', success: '岗位画像已更新' },
  );
}

function resetFilters() {
  showToast('筛选条件已重置', 'success');
}

function copySummary(match: MatchResult) {
  const text = [
    `${match.candidate.name} · ${match.candidate.title}`,
    `匹配分：${match.totalScore}`,
    `理由：${match.reasons.join('；')}`,
    `风险：${match.risks.join('；')}`,
  ].join('\n');
  copyText(text, '候选人摘要已复制');
}

const getBreakdownItems = (match: MatchResult) => [
  { label: '项目经验', value: match.breakdown.experience },
  { label: '专业技能', value: match.breakdown.skills },
  { label: '画像契合', value: match.breakdown.profile },
  { label: '职业偏好', value: match.breakdown.careerPreference },
];
</script>

<style lang="scss" scoped>
.hr-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.candidate-container { display: flex; flex-direction: column; gap: 32rpx; }
.job-summary, .filter-card, .talent-card { border: 2rpx solid #c3c6d7; border-radius: 24rpx; background: #fff; box-shadow: 0 6rpx 22rpx rgba(15,23,42,0.04); }
.job-summary { padding: 32rpx; }
.summary-head { display: flex; flex-direction: column; gap: 24rpx; }
.title-row { display: flex; flex-wrap: wrap; align-items: center; gap: 16rpx; }
.job-title { color: #0b1c30; font-size: 38rpx; font-weight: 900; }
.job-meta { display: flex; flex-wrap: wrap; gap: 20rpx; margin-top: 16rpx; color: #565e74; font-size: 24rpx; }
.job-actions { display: flex; gap: 14rpx; }
.secondary-action, .primary-action { min-height: 72rpx; border-radius: 14rpx; padding: 0 24rpx; font-weight: 900; }
.secondary-action { border: 2rpx solid #737686; color: #565e74; background: #fff; }
.primary-action { background: #004ac6; color: #fff; }
.insight-grid { display: grid; gap: 20rpx; border-top: 2rpx solid #c3c6d7; margin-top: 28rpx; padding-top: 28rpx; }
.insight-tile { display: flex; flex-direction: column; gap: 8rpx; }
.tile-label { color: #737686; font-size: 23rpx; }
.tile-value { color: #0b1c30; font-size: 30rpx; font-weight: 900; }
.tile-value.primary { color: #004ac6; }
.list-layout { display: grid; gap: 28rpx; align-items: start; }
.filter-panel { display: flex; flex-direction: column; gap: 24rpx; }
.filter-card { padding: 28rpx; }
.filter-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24rpx; }
.filter-title { font-size: 32rpx; font-weight: 900; }
.reset-button { color: #004ac6; background: transparent; font-size: 24rpx; font-weight: 900; }
.filter-group { display: flex; flex-direction: column; gap: 12rpx; margin-bottom: 24rpx; }
.filter-label { color: #565e74; font-size: 24rpx; font-weight: 900; }
.range-row { display: flex; align-items: center; gap: 12rpx; }
.small-input, .select-box { min-height: 64rpx; border: 2rpx solid #c3c6d7; border-radius: 10rpx; padding: 0 14rpx; background: #fff; color: #0b1c30; box-sizing: border-box; }
.check-list { display: flex; flex-direction: column; gap: 12rpx; color: #434655; font-size: 25rpx; }
.ai-tip { border: 2rpx solid rgba(0,74,198,0.2); border-radius: 24rpx; padding: 28rpx; background: rgba(0,74,198,0.05); }
.tip-title { display: block; color: #004ac6; font-weight: 900; margin-bottom: 12rpx; }
.tip-desc { color: #3f465c; font-size: 25rpx; line-height: 1.65; }
.candidate-list { display: flex; flex-direction: column; gap: 20rpx; }
.sort-bar { display: flex; justify-content: space-between; gap: 20rpx; border: 2rpx solid #c3c6d7; border-radius: 16rpx; padding: 20rpx 24rpx; background: #eff4ff; color: #0b1c30; font-size: 25rpx; font-weight: 900; }
.talent-card { padding: 28rpx; }
.talent-layout { display: grid; gap: 28rpx; }
.candidate-side { display: flex; flex-direction: column; gap: 24rpx; }
.candidate-main { display: flex; gap: 20rpx; align-items: flex-start; }
.avatar { display: flex; align-items: center; justify-content: center; width: 96rpx; height: 96rpx; border-radius: 24rpx; background: #dbe1ff; color: #004ac6; font-size: 34rpx; font-weight: 900; }
.candidate-info { flex: 1; min-width: 0; }
.candidate-title-row { display: flex; flex-wrap: wrap; align-items: center; gap: 12rpx; }
.candidate-name { font-size: 32rpx; font-weight: 900; }
.candidate-meta { display: block; margin-top: 8rpx; color: #565e74; font-size: 24rpx; line-height: 1.5; }
.score-panel { display: flex; align-items: center; gap: 20rpx; border: 2rpx solid #e2e8f0; border-radius: 18rpx; padding: 18rpx; background: #f8f9ff; }
.score-ring { display: flex; align-items: center; justify-content: center; width: 96rpx; height: 96rpx; border: 8rpx solid #004ac6; border-radius: 999rpx; color: #004ac6; font-size: 34rpx; font-weight: 900; }
.score-title { display: block; color: #004ac6; font-size: 26rpx; font-weight: 900; }
.score-caption { display: block; margin-top: 4rpx; color: #737686; font-size: 19rpx; text-transform: uppercase; }
.candidate-evidence { display: flex; flex-direction: column; gap: 20rpx; }
.breakdown-grid { display: grid; grid-template-columns: repeat(2,1fr); gap: 16rpx; }
.breakdown-tile { border: 2rpx solid #e2e8f0; border-radius: 14rpx; padding: 16rpx; background: #fff; text-align: center; }
.breakdown-label { display: block; color: #737686; font-size: 22rpx; }
.breakdown-value { display: block; margin-top: 6rpx; color: #0b1c30; font-size: 30rpx; font-weight: 900; }
.recommend-box { border-left: 8rpx solid #007d55; border-radius: 0 16rpx 16rpx 0; padding: 20rpx; background: rgba(0,125,85,0.06); }
.recommend-title { display: flex; align-items: center; gap: 10rpx; color: #007d55; font-size: 25rpx; font-weight: 900; margin-bottom: 12rpx; }
.reason-list { display: flex; flex-direction: column; gap: 8rpx; color: #0b1c30; font-size: 24rpx; line-height: 1.55; }
.risk-row, .card-actions { display: flex; flex-wrap: wrap; gap: 12rpx; }
.card-actions { align-content: flex-start; }
.detail-link, .export-link, .summary-link { display: flex; align-items: center; justify-content: center; gap: 8rpx; min-height: 68rpx; border-radius: 14rpx; padding: 0 22rpx; font-size: 24rpx; font-weight: 900; }
.detail-link { background: #004ac6; color: #fff; }
.summary-link { border: 2rpx solid #737686; color: #565e74; background: #fff; }
.export-link { border: 2rpx solid #004ac6; color: #004ac6; }
@media (min-width: 768px) { .summary-head { flex-direction: row; justify-content: space-between; } .insight-grid { grid-template-columns: repeat(4,1fr); } .list-layout { grid-template-columns: 300rpx minmax(0,1fr); } .talent-layout { grid-template-columns: 300rpx minmax(0,1fr) 160rpx; } .candidate-side { border-right: 2rpx solid #e2e8f0; padding-right: 24rpx; } .breakdown-grid { grid-template-columns: repeat(4,1fr); } .card-actions { flex-direction: column; } }
</style>
