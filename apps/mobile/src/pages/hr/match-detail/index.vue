<template>
  <view class="page detail-page">
    <AppTopNav active="HR 工作台" />
    <view class="container detail-container">
      <ProgressSteps :steps="hrSteps" :active-index="3" />

      <view class="notice"><AppIcon name="info" :size="18" color="#ffffff" />建议进入面试：候选人与岗位高度匹配，但需验证稳定性与到岗时间。</view>
      <view class="action-bar"><navigator url="/pages/hr/candidates/index" class="back-link"><AppIcon name="arrowleft" :size="18" />返回列表</navigator><view class="top-actions"><button class="secondary-action" @tap="copyReasons"><AppIcon name="paperclip" :size="18" />复制推荐理由</button><button class="secondary-action" @tap="copyQuestions"><AppIcon name="chat" :size="18" />复制面试问题</button><navigator url="/pages/hr/export/index" class="primary-action"><AppIcon name="upload" :size="18" color="#ffffff" />导出推荐包</navigator></view></view>

      <view class="detail-layout">
        <main class="main-stack">
          <section class="score-grid">
            <view class="score-card talent-card"><AppIcon name="star" :size="28" color="#004ac6" /><text class="score-label">综合匹配分</text><view class="ring-score"><text>89</text></view><AppTag label="极高匹配度" tone="green" /></view>
            <view class="breakdown-card talent-card"><text class="section-title"><AppIcon name="chart" :size="20" color="#004ac6" />匹配维度拆解</text><view class="break-list"><view v-for="item in breakdown" :key="item.label" class="break-item"><view class="break-head"><text>{{ item.label }}</text><text>{{ item.value }}%</text></view><view class="bar"><view class="bar-value" :style="{ width: `${item.value}%` }" /></view></view></view></view>
          </section>

          <section class="talent-card card-block"><view class="card-title-row"><text class="section-title"><AppIcon name="checkmarkempty" :size="20" color="#004ac6" />核心推荐理由</text><text class="muted">基于简历内容与岗位 JD 实时分析</text></view><view class="reason-block" v-for="reason in match.reasons" :key="reason"><text class="reason-title">{{ reason }}</text><text class="reason-evidence">证据：候选人在过往项目中体现出相关经验，可进一步在面试中追问细节。</text></view></section>
          <section class="talent-card card-block amber"><text class="section-title"><AppIcon name="info" :size="20" color="#ba1a1a" />风险点与验证建议</text><view v-for="risk in match.risks" :key="risk" class="risk-item">{{ risk }}：建议在面试中确认真实情况和可接受范围。</view></section>
          <section class="talent-card card-block"><text class="section-title"><AppIcon name="link" :size="20" color="#004ac6" />证据来源</text><view class="evidence-source-list"><view v-for="item in evidenceSources" :key="item.title" class="evidence-source"><text class="source-title">{{ item.title }}</text><text class="source-desc">{{ item.desc }}</text></view></view></section>
          <section class="talent-card card-block"><text class="section-title"><AppIcon name="chat" :size="20" color="#004ac6" />建议面试问题</text><view class="question-list"><text v-for="q in match.interviewQuestions" :key="q">{{ q }}</text></view></section>
          <section class="talent-card card-block"><text class="section-title"><AppIcon name="paperclip" :size="20" color="#004ac6" />简历预览</text><view class="resume-preview"><text class="resume-name">{{ match.candidate.name }} · {{ match.candidate.title }}</text><text class="resume-line">核心经历：大型分布式系统架构、云服务平台重构、跨团队技术方案推进。</text><text class="resume-line">关键技能：Kubernetes、Node.js、PostgreSQL、Redis、系统设计。</text><text class="resume-line">AI 提示：该预览用于快速判断，完整简历需在面试前再次确认。</text></view></section>
        </main>

        <aside class="side-stack">
          <section class="talent-card profile-card"><view class="avatar-large">{{ match.candidate.name.slice(0,1) }}</view><text class="candidate-name">{{ match.candidate.name }}</text><text class="candidate-title">{{ match.candidate.title }}</text><view class="profile-meta"><text>{{ match.candidate.location }}</text><text>{{ match.candidate.experienceYears }} 年经验</text><text>{{ match.candidate.education }}</text></view></section>
          <section class="talent-card card-block"><text class="section-title"><AppIcon name="flag" :size="20" color="#004ac6" />下一步动作</text><navigator url="/pages/hr/export/index" class="primary-full">加入导出包</navigator><button class="secondary-full" @tap="scheduleInterview">安排面试</button></section>
        </aside>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { mockMatches } from '@ai-talent-agent/shared';
import AppIcon from '../../../components/AppIcon.vue';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { copyText, showToast } from '../../../utils/feedback';

const hrSteps = ['岗位需求', '岗位画像', '候选人推荐', '匹配详情', '导出中心'];
const match = mockMatches[0];

function copyReasons() {
  const text = [`候选人：${match.candidate.name}`, `匹配分：${match.totalScore}`, '', '推荐理由：', ...match.reasons, '', '风险点：', ...match.risks].join('\n');
  copyText(text, '推荐理由已复制');
}

function copyQuestions() {
  copyText(match.interviewQuestions.join('\n'), '面试问题已复制');
}

function scheduleInterview() {
  showToast('已加入待安排列表', 'success');
}
const breakdown = [
  { label: '核心技能', value: 95 },
  { label: '项目经验', value: 88 },
  { label: '团队契合', value: 82 },
  { label: '薪资预期', value: 90 },
];
const evidenceSources = [
  { title: '原始简历片段', desc: '识别到分布式系统、核心模块升级、千万级 QPS 等经历描述。' },
  { title: 'AI 访谈证据', desc: '候选人在架构取舍、跨团队推进和风险拆解方面有明确表达。' },
  { title: '岗位 JD 对齐', desc: '岗位要求中的微服务架构、Kubernetes 和技术指导能力均有对应证据。' },
];
</script>

<style lang="scss" scoped>
.detail-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.detail-container { display: flex; flex-direction: column; gap: 28rpx; }
.notice { display: flex; align-items: center; gap: 12rpx; border-radius: 20rpx; padding: 24rpx; background: #2563eb; color: #fff; font-weight: 900; }
.action-bar { display: flex; flex-direction: column; gap: 18rpx; }
.back-link { display: flex; align-items: center; gap: 8rpx; color: #565e74; font-weight: 900; }
.top-actions { display: flex; flex-wrap: wrap; gap: 14rpx; }
.secondary-action, .primary-action { display: flex; align-items: center; justify-content: center; gap: 8rpx; min-height: 72rpx; border-radius: 14rpx; padding: 0 24rpx; font-weight: 900; }
.secondary-action { border: 2rpx solid #737686; color: #565e74; background: #fff; }
.primary-action { background: linear-gradient(135deg,#004ac6,#2563eb); color: #fff; }
.detail-layout { display: grid; gap: 28rpx; }
.main-stack, .side-stack { display: flex; flex-direction: column; gap: 24rpx; }
.score-grid { display: grid; gap: 24rpx; }
.talent-card { border: 2rpx solid #e2e8f0; border-radius: 24rpx; background: #fff; box-shadow: 0 6rpx 22rpx rgba(15,23,42,0.04); }
.score-card { display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 20rpx; padding: 32rpx; }
.score-label { color: #737686; font-size: 22rpx; font-weight: 900; letter-spacing: 2rpx; }
.ring-score { display: flex; align-items: center; justify-content: center; width: 180rpx; height: 180rpx; border: 14rpx solid #004ac6; border-radius: 999rpx; color: #0b1c30; font-size: 52rpx; font-weight: 900; }
.breakdown-card, .card-block, .profile-card { padding: 32rpx; }
.section-title { display: flex; align-items: center; gap: 10rpx; color: #0b1c30; font-size: 32rpx; font-weight: 900; }
.card-title-row { display: flex; justify-content: space-between; gap: 20rpx; align-items: center; margin-bottom: 24rpx; }
.muted { color: #737686; font-size: 24rpx; }
.break-list { display: grid; gap: 22rpx; margin-top: 24rpx; }
.break-head { display: flex; justify-content: space-between; color: #0b1c30; font-size: 25rpx; font-weight: 900; }
.bar { height: 14rpx; overflow: hidden; border-radius: 999rpx; background: #d3e4fe; margin-top: 10rpx; }
.bar-value { height: 100%; border-radius: 999rpx; background: #004ac6; }
.reason-block { border-left: 6rpx solid #004ac6; padding: 20rpx 24rpx; margin-top: 18rpx; background: #eff4ff; border-radius: 0 16rpx 16rpx 0; }
.reason-title { display: block; color: #0b1c30; font-weight: 900; }
.reason-evidence { display: block; margin-top: 8rpx; color: #565e74; font-size: 25rpx; line-height: 1.6; }
.amber { background: #fffbeb; border-color: #fde68a; }
.risk-item { margin-top: 18rpx; color: #92400e; font-size: 26rpx; line-height: 1.6; }
.evidence-source-list { display: grid; gap: 16rpx; margin-top: 20rpx; }
.evidence-source { border-left: 6rpx solid #004ac6; border-radius: 0 16rpx 16rpx 0; padding: 18rpx 22rpx; background: #eff4ff; }
.source-title { display: block; color: #0b1c30; font-weight: 900; }
.source-desc { display: block; margin-top: 8rpx; color: #565e74; font-size: 25rpx; line-height: 1.55; }
.question-list { display: flex; flex-direction: column; gap: 16rpx; margin-top: 20rpx; color: #434655; font-size: 26rpx; line-height: 1.6; }
.resume-preview { display: flex; flex-direction: column; gap: 14rpx; margin-top: 20rpx; border: 2rpx solid #c3c6d7; border-radius: 18rpx; padding: 24rpx; background: #f8f9ff; }
.resume-name { color: #0b1c30; font-size: 30rpx; font-weight: 900; }
.resume-line { color: #565e74; font-size: 25rpx; line-height: 1.6; }
.profile-card { display: flex; flex-direction: column; align-items: center; gap: 14rpx; }
.avatar-large { display: flex; align-items: center; justify-content: center; width: 136rpx; height: 136rpx; border-radius: 999rpx; background: #dbe1ff; color: #004ac6; font-size: 46rpx; font-weight: 900; }
.candidate-name { font-size: 36rpx; font-weight: 900; }
.candidate-title { color: #565e74; }
.profile-meta { display: flex; flex-direction: column; align-items: center; gap: 8rpx; color: #737686; font-size: 24rpx; }
.primary-full, .secondary-full { display: flex; align-items: center; justify-content: center; min-height: 80rpx; border-radius: 16rpx; margin-top: 18rpx; font-weight: 900; }
.primary-full { background: #004ac6; color: #fff; }
.secondary-full { border: 2rpx solid #c3c6d7; color: #565e74; background: #fff; }
@media (min-width: 768px) { .action-bar { flex-direction: row; justify-content: space-between; align-items: center; } .detail-layout { grid-template-columns: 8fr 4fr; } .score-grid { grid-template-columns: 4fr 8fr; } }
</style>
