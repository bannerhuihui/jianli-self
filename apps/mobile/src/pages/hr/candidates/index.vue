<template>
  <view class="page hr-page">
    <AppTopNav active="HR 工作台" />

    <view class="container candidate-container">
      <ProgressSteps v-bind="createFlowStepsProps(HR_FLOW, 2)" navigable />

      <section class="job-summary">
        <view class="summary-head">
          <view class="summary-copy">
            <view class="title-row">
              <text class="job-title">高级架构师 (Cloud & AI)</text>
              <text class="status-pill">招聘中</text>
            </view>
            <view class="job-meta">
              <view class="meta-item">
                <AppIcon name="location_on" :size="18" color="#434655" />
                <text>上海 · 浦东</text>
              </view>
              <view class="meta-item">
                <AppIcon name="payments" :size="18" color="#434655" />
                <text>50k - 80k · 16薪</text>
              </view>
              <view class="meta-item">
                <AppIcon name="work" :size="18" color="#434655" />
                <text>8-10年经验</text>
              </view>
              <view class="meta-item">
                <AppIcon name="school" :size="18" color="#434655" />
                <text>统招硕士及以上</text>
              </view>
            </view>
          </view>
          <view class="job-actions">
            <button class="hr-secondary-action" @tap="editJd">
              <AppIcon name="edit" :size="20" color="#565e74" />
              <text>修改JD</text>
            </button>
            <button class="hr-primary-action" @tap="reanalyze">
              <AppIcon name="auto_awesome" :size="20" color="#ffffff" />
              <text>重新分析</text>
            </button>
          </view>
        </view>

        <view class="insight-grid">
          <view class="insight-tile">
            <text class="tile-label">核心技能要求</text>
            <text class="tile-value">Distributed, K8s, PyTorch</text>
          </view>
          <view class="insight-tile">
            <text class="tile-label">推荐人选规模</text>
            <text class="tile-value">248 位匹配</text>
          </view>
          <view class="insight-tile">
            <text class="tile-label">平均匹配度</text>
            <text class="tile-value primary">82%</text>
          </view>
          <view class="insight-tile">
            <text class="tile-label">最近活跃</text>
            <text class="tile-value">12 位今日上线</text>
          </view>
        </view>
      </section>

      <view class="list-layout">
        <aside class="filter-panel">
          <view class="filter-card">
            <view class="filter-head">
              <text class="filter-title">筛选条件</text>
              <button class="reset-button" @tap="resetFilters">重置</button>
            </view>

            <view class="filter-group">
              <text class="filter-label">匹配分区间</text>
              <view class="range-row">
                <input class="small-input" type="number" placeholder="最低" />
                <text class="range-sep">-</text>
                <input class="small-input" type="number" placeholder="最高" />
              </view>
            </view>

            <view class="filter-group">
              <text class="filter-label">推荐等级</text>
              <label class="check-item">
                <checkbox :checked="filterLevels.excellent" color="#004ac6" @tap="toggleLevel('excellent')" />
                <text>卓越匹配 (90%+)</text>
              </label>
              <label class="check-item">
                <checkbox :checked="filterLevels.high" color="#004ac6" @tap="toggleLevel('high')" />
                <text>高度匹配 (80%-90%)</text>
              </label>
              <label class="check-item">
                <checkbox :checked="filterLevels.medium" color="#004ac6" @tap="toggleLevel('medium')" />
                <text>中度匹配 (70%-80%)</text>
              </label>
            </view>

            <view class="filter-group">
              <text class="filter-label">目前状态</text>
              <picker :range="statusOptions" @change="onStatusChange">
                <view class="select-box">{{ statusOptions[statusIndex] }}</view>
              </picker>
            </view>
          </view>

          <view class="ai-tip">
            <view class="tip-head">
              <AppIcon name="tips_and_updates" :size="20" color="#004ac6" />
              <text class="tip-title">AI 寻才建议</text>
            </view>
            <text class="tip-desc">
              建议放宽“行业背景”限制。当前JD对金融背景要求过高，放宽至互联网头部企业可增加45%的优质候选人。
            </text>
          </view>
        </aside>

        <main class="candidate-list">
          <view class="sort-bar">
            <text class="sort-count">找到 48 位匹配的候选人</text>
            <view class="sort-control">
              <text class="sort-label">排序依据:</text>
              <picker :range="sortOptions" @change="onSortChange">
                <view class="sort-picker">{{ sortOptions[sortIndex] }}</view>
              </picker>
            </view>
          </view>

          <article
            v-for="match in matches"
            :key="match.candidate.id"
            class="talent-card"
          >
            <view class="talent-layout">
              <aside class="candidate-side">
                <view class="candidate-main">
                  <image
                    class="avatar"
                    :src="getExtra(match.candidate.id).avatar"
                    mode="aspectFill"
                  />
                  <view class="candidate-info">
                    <text class="candidate-name">{{ match.candidate.name }}</text>
                    <text class="candidate-role">
                      现任：{{ getExtra(match.candidate.id).company }} · {{ match.candidate.title }}
                    </text>
                    <text class="candidate-edu">
                      经验：{{ match.candidate.experienceYears }}年 · {{ formatEducation(match.candidate.education) }}
                    </text>
                  </view>
                </view>

                <view class="score-panel">
                  <view class="score-ring-wrap">
                    <svg class="score-ring-svg" viewBox="0 0 36 36">
                      <path
                        class="ring-track"
                        d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                        fill="none"
                        stroke-width="3"
                      />
                      <path
                        class="ring-fill"
                        d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831"
                        fill="none"
                        stroke-width="3"
                        stroke-linecap="round"
                        :stroke-dasharray="`${match.totalScore}, 100`"
                      />
                    </svg>
                    <text class="score-number">{{ match.totalScore }}</text>
                  </view>
                  <view>
                    <text class="score-title">{{ getMatchLevel(match.totalScore) }}</text>
                    <text class="score-caption">Total Match Score</text>
                  </view>
                </view>
              </aside>

              <view class="candidate-evidence">
                <view class="breakdown-grid">
                  <view
                    v-for="item in getBreakdownItems(match)"
                    :key="item.label"
                    class="breakdown-tile"
                  >
                    <text class="breakdown-label">{{ item.label }}</text>
                    <text class="breakdown-value">{{ item.value }}</text>
                  </view>
                </view>

                <view class="recommend-box">
                  <view class="recommend-title">
                    <AppIcon name="verified" :size="18" color="#006242" />
                    <text>AI 推荐理由</text>
                  </view>
                  <view class="reason-list">
                    <text
                      v-for="reason in getExtra(match.candidate.id).reasons"
                      :key="reason"
                      class="reason-item"
                    >
                      · {{ reason }}
                    </text>
                  </view>
                </view>

                <view class="tag-row">
                  <text
                    v-for="risk in match.risks"
                    :key="risk"
                    class="risk-tag"
                  >
                    <AppIcon name="warning" :size="14" color="#93000a" />
                    待验证：{{ risk }}
                  </text>
                  <text
                    v-for="tag in getExtra(match.candidate.id).tags"
                    :key="tag"
                    class="neutral-tag"
                  >
                    {{ tag }}
                  </text>
                </view>
              </view>

              <view class="card-actions">
                <button class="detail-btn" @tap="goDetail(match)">查看详情</button>
                <button class="outline-btn" @tap="copySummary(match)">
                  <AppIcon name="content_copy" :size="18" color="#565e74" />
                  <text>摘要</text>
                </button>
                <button class="outline-btn" @tap="exportCandidate(match)">
                  <AppIcon name="add_box" :size="18" color="#565e74" />
                  <text>导出</text>
                </button>
              </view>
            </view>
          </article>

          <view class="pagination">
            <button class="page-btn">
              <AppIcon name="chevron_left" :size="20" color="#434655" />
            </button>
            <button class="page-btn active">1</button>
            <button class="page-btn">2</button>
            <button class="page-btn">3</button>
            <text class="page-ellipsis">...</text>
            <button class="page-btn">12</button>
            <button class="page-btn">
              <AppIcon name="chevron_right" :size="20" color="#434655" />
            </button>
          </view>
        </main>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { mockMatches, mockMatchExtras } from '@ai-talent-agent/shared';
import type { MatchResult } from '@ai-talent-agent/domain';
import { HR_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { copyText, runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';

const matches = mockMatches;

const statusOptions = ['不限', '离职状态', '在职看机会', '在职稳定'];
const sortOptions = ['AI 综合评分 (高-低)', '入库时间', '薪资要求 (低-高)'];
const statusIndex = ref(0);
const sortIndex = ref(0);

const filterLevels = reactive({
  excellent: true,
  high: true,
  medium: false,
});

function getExtra(id: string) {
  return (
    mockMatchExtras[id] ?? {
      avatar: '',
      company: '—',
      reasons: [],
      tags: [],
    }
  );
}

function formatEducation(education: string) {
  const parts = education.split(' · ');
  if (parts.length >= 2) return `${parts[1]} (${parts[0]})`;
  return education;
}

function getMatchLevel(score: number) {
  if (score >= 90) return '卓越匹配';
  if (score >= 80) return '高度匹配';
  return '中度匹配';
}

function getBreakdownItems(match: MatchResult) {
  return [
    { label: '项目经验', value: match.breakdown.experience },
    { label: '专业技能', value: match.breakdown.skills },
    { label: '学历背景', value: match.breakdown.profile },
    { label: '职涯稳定性', value: match.breakdown.careerPreference },
  ];
}

function toggleLevel(key: keyof typeof filterLevels) {
  filterLevels[key] = !filterLevels[key];
}

function onStatusChange(event: { detail: { value: string } }) {
  statusIndex.value = Number(event.detail.value);
}

function onSortChange(event: { detail: { value: string } }) {
  sortIndex.value = Number(event.detail.value);
}

function editJd() {
  uni.navigateTo({ url: '/pages/hr/job/index' });
}

async function reanalyze() {
  await runAsyncAction(() => simulateDelay(1200), {
    loading: '重新分析中',
    success: '岗位画像已更新',
  });
}

function resetFilters() {
  filterLevels.excellent = true;
  filterLevels.high = true;
  filterLevels.medium = false;
  statusIndex.value = 0;
  showToast('筛选条件已重置', 'success');
}

function goDetail(match: MatchResult) {
  uni.navigateTo({
    url: `/pages/hr/match-detail/index?id=${encodeURIComponent(match.candidate.id)}`,
  });
}

function copySummary(match: MatchResult) {
  const extra = getExtra(match.candidate.id);
  const text = [
    `${match.candidate.name} · ${extra.company} · ${match.candidate.title}`,
    `匹配分：${match.totalScore}（${getMatchLevel(match.totalScore)}）`,
    `理由：${extra.reasons.join('；')}`,
    `风险：${match.risks.map((r) => `待验证：${r}`).join('；')}`,
  ].join('\n');
  copyText(text, '候选人摘要已复制');
}

async function exportCandidate(match: MatchResult) {
  await runAsyncAction(() => simulateDelay(800), {
    loading: '加入导出包',
    success: `${match.candidate.name} 已加入导出包`,
  });
}
</script>

<style lang="scss" scoped>
.candidate-container {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  padding-bottom: 48rpx;
}

.job-summary {
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  padding: 48rpx;
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
}

.summary-head {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
  margin-bottom: 48rpx;
}

.title-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 24rpx;
  margin-bottom: 16rpx;
}

.job-title {
  color: #0b1c30;
  font-size: 48rpx;
  font-weight: 600;
  line-height: 1.33;
}

.status-pill {
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  background: rgba(0, 74, 198, 0.1);
  color: #004ac6;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}

.job-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 32rpx;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
  color: #434655;
  font-size: 28rpx;
  line-height: 1.43;
}

.job-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.insight-grid {
  display: grid;
  gap: 48rpx;
  border-top: 2rpx solid #c3c6d7;
  padding-top: 48rpx;
}

.insight-tile {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.tile-label {
  color: #434655;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}

.tile-value {
  color: #0b1c30;
  font-size: 36rpx;
  font-weight: 600;
  line-height: 1.33;
}

.tile-value.primary { color: #004ac6; }

.list-layout {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.filter-panel {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.filter-card {
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  padding: 32rpx;
}

.filter-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.filter-title {
  font-size: 36rpx;
  font-weight: 600;
  line-height: 1.33;
}

.reset-button {
  margin: 0;
  padding: 0;
  color: #004ac6;
  background: transparent;
  font-size: 24rpx;
  font-weight: 500;
  &::after { border: none; }
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  margin-bottom: 32rpx;
}

.filter-label {
  color: #434655;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1.43;
}

.range-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.range-sep { color: #434655; }

.small-input,
.select-box {
  flex: 1;
  min-height: 64rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 8rpx;
  padding: 0 16rpx;
  background: #fff;
  color: #0b1c30;
  font-size: 28rpx;
  line-height: 64rpx;
  box-sizing: border-box;
}

.check-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  color: #0b1c30;
  font-size: 28rpx;
  line-height: 1.43;
}

.ai-tip {
  border: 2rpx solid rgba(0, 74, 198, 0.2);
  border-radius: 16rpx;
  padding: 32rpx;
  background: rgba(0, 74, 198, 0.05);
}

.tip-head {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 16rpx;
}

.tip-title {
  color: #004ac6;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1.43;
}

.tip-desc {
  color: #3f465c;
  font-size: 28rpx;
  line-height: 1.43;
}

.candidate-list {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.sort-bar {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  padding: 16rpx 32rpx;
  background: #eff4ff;
}

.sort-count {
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1.43;
}

.sort-control {
  display: flex;
  align-items: center;
  gap: 32rpx;
}

.sort-label {
  color: #434655;
  font-size: 24rpx;
  line-height: 1.33;
}

.sort-picker {
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1.43;
}

.talent-card {
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  padding: 48rpx;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.talent-layout {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
}

.candidate-side {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.candidate-main {
  display: flex;
  gap: 32rpx;
  align-items: flex-start;
}

.avatar {
  width: 128rpx;
  height: 128rpx;
  border-radius: 16rpx;
  border: 2rpx solid #c3c6d7;
  flex-shrink: 0;
}

.candidate-info {
  flex: 1;
  min-width: 0;
}

.candidate-name {
  display: block;
  color: #0b1c30;
  font-size: 36rpx;
  font-weight: 600;
  line-height: 1.33;
}

.candidate-role,
.candidate-edu {
  display: block;
  margin-top: 8rpx;
  font-size: 28rpx;
  line-height: 1.43;
}

.candidate-role { color: #434655; }
.candidate-edu { color: #565e74; font-size: 24rpx; }

.score-panel {
  display: flex;
  align-items: center;
  gap: 32rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  padding: 24rpx;
  background: #fff;
}

.score-ring-wrap {
  position: relative;
  width: 128rpx;
  height: 128rpx;
  flex-shrink: 0;
}

.score-ring-svg {
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}

.ring-track { stroke: #c3c6d7; }
.ring-fill { stroke: #004ac6; }

.score-number {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #004ac6;
  font-size: 36rpx;
  font-weight: 700;
  line-height: 1;
}

.score-title {
  display: block;
  color: #004ac6;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1.43;
}

.score-caption {
  display: block;
  margin-top: 4rpx;
  color: #434655;
  font-size: 20rpx;
  text-transform: uppercase;
  line-height: 1.2;
}

.candidate-evidence {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.breakdown-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 32rpx;
}

.breakdown-tile {
  border: 2rpx solid #c3c6d7;
  border-radius: 8rpx;
  padding: 16rpx;
  background: #fff;
  text-align: center;
}

.breakdown-label {
  display: block;
  color: #434655;
  font-size: 24rpx;
  line-height: 1.33;
}

.breakdown-value {
  display: block;
  margin-top: 8rpx;
  color: #0b1c30;
  font-size: 32rpx;
  font-weight: 700;
  line-height: 1.25;
}

.recommend-box {
  border-left: 8rpx solid #006242;
  border-radius: 0 16rpx 16rpx 0;
  padding: 24rpx;
  background: rgba(0, 98, 66, 0.05);
}

.recommend-title {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-bottom: 8rpx;
  color: #006242;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1.43;
}

.reason-list {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.reason-item {
  color: #0b1c30;
  font-size: 28rpx;
  line-height: 1.43;
}

.tag-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.risk-tag,
.neutral-tag {
  display: inline-flex;
  align-items: center;
  gap: 8rpx;
  border-radius: 8rpx;
  padding: 4rpx 16rpx;
  font-size: 24rpx;
  line-height: 1.33;
}

.risk-tag {
  background: #ffdad6;
  color: #93000a;
}

.neutral-tag {
  background: #d3e4fe;
  color: #434655;
}

.card-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
}

.detail-btn,
.outline-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8rpx;
  margin: 0;
  min-height: 72rpx;
  padding: 0 24rpx;
  border-radius: 16rpx;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1;
  box-sizing: border-box;
  &::after { border: none; }
}

.detail-btn {
  flex: 1;
  border: none;
  background: #004ac6;
  color: #fff;
}

.outline-btn {
  flex: 1;
  border: 2rpx solid #737686;
  color: #565e74;
  background: #fff;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16rpx;
  padding-top: 64rpx;
}

.page-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 64rpx;
  height: 64rpx;
  margin: 0;
  border: 2rpx solid #c3c6d7;
  border-radius: 8rpx;
  background: #fff;
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 500;
  line-height: 1;
  &::after { border: none; }
}

.page-btn.active {
  border-color: #004ac6;
  background: #004ac6;
  color: #fff;
}

.page-ellipsis {
  padding: 0 8rpx;
  color: #434655;
  font-size: 28rpx;
}

@media (min-width: 768px) {
  .summary-head {
    flex-direction: row;
    justify-content: space-between;
    align-items: flex-start;
  }

  .job-title { font-size: 24px; line-height: 32px; }
  .status-pill { font-size: 12px; }
  .meta-item { font-size: 14px; }
  .insight-grid { grid-template-columns: repeat(4, 1fr); gap: 24px; }
  .tile-label { font-size: 12px; }
  .tile-value { font-size: 18px; }

  .list-layout {
    flex-direction: row;
    align-items: flex-start;
    gap: 24px;
  }

  .filter-panel { width: 256px; flex-shrink: 0; gap: 24px; }
  .filter-card { padding: 16px; border-radius: 12px; }
  .filter-title { font-size: 18px; }
  .filter-label,
  .check-item,
  .tip-desc { font-size: 14px; }
  .small-input,
  .select-box { min-height: 32px; line-height: 32px; font-size: 14px; }

  .sort-bar {
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    padding: 8px 16px;
  }

  .sort-count,
  .sort-picker { font-size: 14px; }

  .talent-card {
    padding: 24px;
    border-radius: 12px;
  }

  .talent-card:hover {
    border-color: #004ac6;
    box-shadow: 0 4px 12px rgba(15, 23, 42, 0.05);
  }

  .talent-layout {
    flex-direction: row;
    gap: 24px;
  }

  .candidate-side {
    width: 256px;
    flex-shrink: 0;
    border-right: 2rpx solid #c3c6d7;
    padding-right: 24px;
  }

  .avatar { width: 64px; height: 64px; border-radius: 8px; }
  .candidate-name { font-size: 18px; }
  .candidate-role { font-size: 14px; }
  .candidate-edu { font-size: 12px; }

  .score-ring-wrap { width: 64px; height: 64px; }
  .score-number { font-size: 18px; }
  .score-title { font-size: 14px; }
  .score-caption { font-size: 10px; }

  .breakdown-grid { grid-template-columns: repeat(4, 1fr); gap: 16px; }
  .breakdown-label { font-size: 12px; }
  .breakdown-value { font-size: 16px; }

  .recommend-title,
  .reason-item { font-size: 14px; }

  .risk-tag,
  .neutral-tag { font-size: 12px; }

  .candidate-evidence { flex: 1; min-width: 0; gap: 16px; }

  .card-actions {
    width: 128px;
    flex-shrink: 0;
    flex-direction: column;
  }

  .detail-btn,
  .outline-btn {
    width: 100%;
    flex: none;
    min-height: 40px;
    font-size: 14px;
    border-radius: 8px;
  }

  .page-btn {
    width: 32px;
    height: 32px;
    font-size: 14px;
  }
}
</style>
