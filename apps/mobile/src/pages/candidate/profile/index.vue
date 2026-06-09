<template>
  <view class="page profile-page">
    <AppTopNav active="求职者流程" />

    <view class="container profile-container">
      <ProgressSteps :steps="candidateSteps" :active-index="3" />

      <view class="overview-card">
        <view class="radar-panel">
          <view class="radar-box">
            <view class="radar-shape layer-one" />
            <view class="radar-shape layer-two" />
            <view class="radar-shape layer-three" />
            <view class="radar-area" />
            <view class="score-center">
              <text class="center-label">综合得分</text>
              <text class="center-score">{{ profile.overallScore }}</text>
            </view>
          </view>
          <view class="legend-grid">
            <view v-for="item in profile.capabilities" :key="item.key" class="legend-item"><text class="legend-dot" />{{ item.name }}</view>
          </view>
        </view>

        <view class="summary-panel">
          <view class="summary-section">
            <text class="summary-title">综合评估摘要</text>
            <text class="summary-text">{{ profile.summary }}</text>
          </view>
          <view class="insight-grid">
            <view class="insight-block strengths">
              <text class="insight-title">核心优势</text>
              <view v-for="item in profile.strengths" :key="item" class="insight-item"><text class="insight-mark">✓</text><text>{{ item }}</text></view>
            </view>
            <view class="insight-block risks">
              <text class="insight-title">潜在风险</text>
              <view v-for="item in profile.risks" :key="item" class="insight-item"><text class="insight-mark">!</text><text>{{ item }}</text></view>
            </view>
          </view>
        </view>
      </view>

      <view class="dimension-grid">
        <view
          v-for="item in profile.capabilities"
          :key="item.key"
          class="dimension-card"
          :class="{ risk: item.key === 'careerStability' }"
        >
          <view class="dimension-head">
            <text class="dimension-name">{{ item.name }}</text>
            <ConfidenceBadge :confidence="item.confidence" />
          </view>
          <view class="dimension-score-row"><text class="dimension-score">{{ item.score.toFixed(1) }}</text><text class="dimension-unit">/ 5.0</text></view>
          <text class="dimension-reason">{{ item.reason }}</text>
          <button class="evidence-button">{{ item.key === 'careerStability' ? '风险溯源' : '查看证据' }}</button>
        </view>
      </view>

      <view class="bottom-grid">
        <view class="bottom-card">
          <text class="bottom-title">职业偏好与岗位推荐</text>
          <view class="preference-grid">
            <view class="preference-box">
              <text class="preference-label">动力驱动点</text>
              <view class="tag-row"><AppTag v-for="item in profile.preferences" :key="item" :label="item" tone="gray" /></view>
            </view>
            <view class="preference-box">
              <text class="preference-label">推荐岗位方向</text>
              <view class="tag-row"><AppTag v-for="item in profile.recommendedRoles" :key="item" :label="item" tone="blue" /></view>
            </view>
          </view>
          <view class="ai-advice"><text class="advice-strong">AI 专家建议：</text>候选人更倾向技术深耕与架构方向。建议安排核心中间件、云原生架构或大规模数据平台级项目，能最大限度发挥其技术潜能。</view>
        </view>

        <view class="bottom-card">
          <text class="bottom-title">证据链入口 / 溯源中心</text>
          <view class="evidence-list">
            <view v-for="item in evidenceEntries" :key="item.title" class="evidence-entry">
              <view class="entry-icon"><AppIcon :name="item.icon" :size="24" color="#004ac6" /></view>
              <view class="entry-copy">
                <text class="entry-title">{{ item.title }}</text>
                <text class="entry-desc">{{ item.desc }}</text>
              </view>
              <text class="entry-arrow">›</text>
            </view>
          </view>
        </view>
      </view>

      <navigator url="/pages/candidate/resume/index" class="primary-button next-button">生成简历版本</navigator>
    </view>
  </view>
</template>

<script setup lang="ts">
import { mockTalentProfile } from '@ai-talent-agent/shared';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import AppIcon from '../../../components/AppIcon.vue';
import ConfidenceBadge from '../../../components/ConfidenceBadge.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';

const candidateSteps = ['上传简历', '简历校对', 'AI 访谈', '人才画像', '简历生成'];
const profile = mockTalentProfile;
const evidenceEntries = [
  { icon: 'paperclip', title: '原始简历溯源', desc: '基于简历语境提取的 14 个核心能力证明' },
  { icon: 'chat', title: '面试语料库分析', desc: 'AI 深度识别的逻辑思维与实时沟通关键片段' },
  { icon: 'bars', title: '外部公开数据交叉比对', desc: '公开项目、技术影响力和行业背景的多维度分析' },
];
</script>

<style lang="scss" scoped>
.profile-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.profile-container { display: flex; flex-direction: column; gap: 32rpx; }
.overview-card { display: grid; gap: 32rpx; border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 32rpx; background: #fff; box-shadow: 0 8rpx 40rpx rgba(0, 74, 198, 0.08); }
.radar-panel { display: flex; flex-direction: column; align-items: center; gap: 28rpx; }
.radar-box { position: relative; width: 520rpx; max-width: 100%; aspect-ratio: 1; }
.radar-shape, .radar-area { position: absolute; left: 50%; top: 50%; transform: translate(-50%, -50%) rotate(22.5deg); clip-path: polygon(50% 0%, 85% 15%, 100% 50%, 85% 85%, 50% 100%, 15% 85%, 0% 50%, 15% 15%); }
.radar-shape { border: 2rpx solid #e2e8f0; background: transparent; }
.layer-one { width: 92%; height: 92%; }
.layer-two { width: 68%; height: 68%; }
.layer-three { width: 44%; height: 44%; }
.radar-area { width: 78%; height: 84%; background: rgba(0, 74, 198, 0.15); border: 4rpx solid #004ac6; }
.score-center { position: absolute; left: 50%; top: 50%; transform: translate(-50%, -50%); display: flex; flex-direction: column; align-items: center; justify-content: center; min-width: 160rpx; padding: 18rpx 24rpx; border: 2rpx solid rgba(0, 74, 198, 0.2); border-radius: 16rpx; background: rgba(255,255,255,0.88); }
.center-label { color: #737686; font-size: 20rpx; font-weight: 900; letter-spacing: 2rpx; }
.center-score { color: #004ac6; font-size: 58rpx; font-weight: 900; }
.legend-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12rpx 32rpx; width: 100%; color: #737686; font-size: 22rpx; }
.legend-item { display: flex; align-items: center; gap: 10rpx; }
.legend-dot { width: 12rpx; height: 12rpx; border-radius: 999rpx; background: #004ac6; }
.summary-panel { display: flex; flex-direction: column; gap: 32rpx; }
.summary-section { display: flex; flex-direction: column; gap: 18rpx; }
.summary-title { color: #0b1c30; font-size: 34rpx; font-weight: 900; }
.summary-text { border-radius: 16rpx; padding: 28rpx; background: rgba(242, 244, 246, 0.72); color: #434655; font-size: 28rpx; line-height: 1.75; }
.insight-grid { display: grid; gap: 24rpx; }
.insight-block { display: flex; flex-direction: column; gap: 18rpx; }
.insight-title { font-size: 30rpx; font-weight: 900; }
.strengths .insight-title { color: #006242; }
.risks .insight-title { color: #b45309; }
.insight-item { display: flex; align-items: flex-start; gap: 14rpx; border-radius: 16rpx; padding: 22rpx; font-size: 26rpx; line-height: 1.55; }
.strengths .insight-item { background: rgba(0,98,66,0.05); border: 2rpx solid rgba(0,98,66,0.1); }
.risks .insight-item { background: #fffbeb; border: 2rpx solid #fde68a; color: #92400e; }
.insight-mark { font-weight: 900; }
.dimension-grid { display: grid; gap: 24rpx; }
.dimension-card { display: flex; flex-direction: column; gap: 20rpx; border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 28rpx; background: #fff; box-shadow: 0 4rpx 18rpx rgba(0, 74, 198, 0.04); }
.dimension-card.risk { background: rgba(255,251,235,0.72); border-color: #fcd34d; }
.dimension-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 16rpx; }
.dimension-name { color: #0b1c30; font-size: 30rpx; font-weight: 900; }
.risk .dimension-name { color: #b45309; }
.dimension-score-row { display: flex; align-items: baseline; gap: 8rpx; }
.dimension-score { color: #004ac6; font-size: 58rpx; font-weight: 900; }
.risk .dimension-score { color: #b45309; }
.dimension-unit { color: #737686; font-size: 22rpx; }
.dimension-reason { min-height: 76rpx; color: #565e74; font-size: 24rpx; line-height: 1.6; }
.risk .dimension-reason { color: rgba(146,64,14,0.86); }
.evidence-button { display: flex; align-items: center; justify-content: center; height: 72rpx; border-radius: 16rpx; background: #eff4ff; color: #004ac6; font-size: 24rpx; font-weight: 900; }
.risk .evidence-button { background: #fef3c7; color: #b45309; }
.bottom-grid { display: grid; gap: 24rpx; }
.bottom-card { display: flex; flex-direction: column; gap: 28rpx; border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 32rpx; background: #fff; }
.bottom-title { color: #0b1c30; font-size: 32rpx; font-weight: 900; }
.preference-grid { display: grid; gap: 20rpx; }
.preference-box { border-radius: 16rpx; padding: 24rpx; background: #f2f4f6; }
.preference-label { display: block; margin-bottom: 18rpx; color: #737686; font-size: 22rpx; font-weight: 900; letter-spacing: 2rpx; }
.tag-row { display: flex; flex-wrap: wrap; gap: 12rpx; }
.ai-advice { border-left: 8rpx solid #004ac6; border-radius: 0 16rpx 16rpx 0; padding: 24rpx; background: rgba(0,74,198,0.05); color: #434655; font-size: 26rpx; line-height: 1.7; }
.advice-strong { color: #004ac6; font-weight: 900; }
.evidence-list { display: flex; flex-direction: column; gap: 18rpx; }
.evidence-entry { display: flex; align-items: center; gap: 20rpx; border: 2rpx solid #c3c6d7; border-radius: 16rpx; padding: 22rpx; }
.entry-icon { display: flex; align-items: center; justify-content: center; width: 72rpx; height: 72rpx; border-radius: 999rpx; background: rgba(0,74,198,0.1); color: #004ac6; font-weight: 900; }
.entry-copy { flex: 1; display: flex; flex-direction: column; gap: 6rpx; }
.entry-title { color: #0b1c30; font-size: 26rpx; font-weight: 900; }
.entry-desc { color: #737686; font-size: 22rpx; line-height: 1.5; }
.entry-arrow { color: #737686; font-size: 44rpx; }
.next-button { margin-top: 8rpx; }
@media (min-width: 768px) {
  .overview-card { grid-template-columns: 4fr 8fr; }
  .radar-panel { border-right: 2rpx solid #c3c6d7; padding-right: 40rpx; }
  .summary-panel { padding-left: 8rpx; }
  .insight-grid { grid-template-columns: repeat(2, 1fr); }
  .dimension-grid { grid-template-columns: repeat(4, 1fr); }
  .bottom-grid { grid-template-columns: repeat(2, 1fr); }
  .preference-grid { grid-template-columns: repeat(2, 1fr); }
  .next-button { align-self: flex-end; min-width: 360rpx; }
}
</style>
