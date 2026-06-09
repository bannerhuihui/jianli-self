<template>
  <view class="page hr-page">
    <AppTopNav active="HR 工作台" />
    <view class="container profile-container">
      <ProgressSteps :steps="hrSteps" :active-index="1" />

      <section class="header-row">
        <view>
          <view class="status-row"><AppIcon name="person-filled" :size="18" color="#004ac6" /><text>AI 岗位画像分析已完成</text></view>
          <text class="page-title">岗位画像确认</text>
          <text class="page-desc">请核对 AI 提取的关键要素，确保画像准确以实现精准匹配。</text>
        </view>
        <view class="header-actions">
          <button class="secondary-action"><AppIcon name="download" :size="18" />导出画像</button>
          <navigator url="/pages/hr/candidates/index" class="primary-action">开始匹配候选人 <AppIcon name="arrowright" :size="18" color="#ffffff" /></navigator>
        </view>
      </section>

      <view class="profile-grid">
        <main class="main-stack">
          <section class="glass-card card-block">
            <view class="card-title-row">
              <text class="card-title"><text class="title-icon"><AppIcon name="staff" :size="16" color="#004ac6" /></text>基本信息</text>
              <button class="edit-all">编辑全部</button>
            </view>
            <view class="info-grid">
              <view><text class="field-label">岗位名称</text><text class="editable">{{ job.title }}</text></view>
              <view><text class="field-label">经验要求</text><text class="editable">{{ job.seniority }}</text></view>
            </view>
          </section>

          <section class="glass-card card-block">
            <text class="card-title"><text class="title-icon"><AppIcon name="compose" :size="16" color="#004ac6" /></text>核心职责</text>
            <view class="bullet-list">
              <view v-for="item in job.responsibilities" :key="item" class="bullet-item">
                <text class="dot" />
                <text class="editable-line">{{ item }}</text>
              </view>
            </view>
            <view class="ai-suggest">
              <AppIcon name="info" :size="18" color="#004ac6" />
              <view class="suggest-copy">
                <text><text class="suggest-strong">AI 建议：</text>职责中未明确提及具体云服务（如 AWS、阿里云）的管理经验，这通常是高级职位关键点，建议补充。</text>
                <button class="suggest-link">点击补充</button>
              </view>
            </view>
          </section>

          <view class="skill-grid">
            <section class="glass-card card-block">
              <text class="card-title small"><text class="title-icon filled"><AppIcon name="star-filled" :size="16" color="#ffffff" /></text>必须具备</text>
              <view class="tag-row"><AppTag v-for="skill in job.requiredSkills" :key="skill" :label="skill" tone="blue" /><text class="add-tag">+ 添加</text></view>
            </section>

            <section class="glass-card card-block">
              <text class="card-title small"><text class="title-icon muted"><AppIcon name="plus" :size="16" color="#565e74" /></text>加分项</text>
              <view class="tag-row"><AppTag v-for="skill in job.bonusSkills" :key="skill" :label="skill" tone="gray" /><text class="add-tag muted">+ 添加</text></view>
            </section>
          </view>
        </main>

        <aside class="side-stack">
          <section class="glass-card radar-card">
            <text class="card-title"><text class="title-icon"><AppIcon name="map" :size="16" color="#004ac6" /></text>岗位能力要求</text>
            <view class="radar-box">
              <view class="radar-label top">技术深度</view>
              <view class="radar-label right">系统架构</view>
              <view class="radar-label bottom-right">团队领导力</view>
              <view class="radar-label bottom-left">业务理解</view>
              <view class="radar-label left">沟通协作</view>
              <view class="radar-layer one" />
              <view class="radar-layer two" />
              <view class="radar-fill" />
            </view>
            <view class="score-list">
              <view v-for="score in capabilityScores" :key="score.name" class="score-item">
                <view class="score-head"><text>{{ score.name }}</text><text class="score-value">{{ score.value }}/100</text></view>
                <view class="score-track"><view class="score-bar" :style="{ width: `${score.value}%` }" /></view>
              </view>
            </view>
          </section>

          <section class="glass-card card-block persona-card">
            <text class="card-title"><text class="title-icon"><AppIcon name="paperplane" :size="16" color="#004ac6" /></text>职业倾向</text>
            <view class="persona-list">
              <view v-for="item in personas" :key="item.title" class="persona-item">
                <AppIcon name="checkmarkempty" :size="18" color="#007d55" />
                <view><text class="persona-title">{{ item.title }}</text><text class="persona-desc">{{ item.desc }}</text></view>
              </view>
            </view>
            <view class="missing-card">
              <text class="missing-title">完善岗位画像</text>
              <text class="missing-desc">{{ job.missingFields[0] }}，当前默认为混合办公。</text>
              <button class="missing-action">立即设定</button>
            </view>
          </section>

          <section class="insight-card">
            <view class="insight-overlay">
              <text class="insight-title">行业洞察</text>
              <text class="insight-desc">当前市场上此类人才稀缺，建议匹配薪资范围：40k-65k，并尽早启动候选人触达。</text>
            </view>
          </section>
        </aside>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { mockJobProfile } from '@ai-talent-agent/shared';
import AppIcon from '../../../components/AppIcon.vue';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';

const hrSteps = ['岗位需求', '岗位画像', '候选人推荐', '匹配详情', '导出中心'];
const job = mockJobProfile;
const capabilityScores = [
  { name: '技术深度', value: 90 },
  { name: '系统架构', value: 86 },
  { name: '团队领导力', value: 78 },
  { name: '沟通协作', value: 75 },
];
const personas = [
  { title: '自我驱动型', desc: '能够在不确定性中寻找最优技术路径。' },
  { title: '产品思维', desc: '不仅关注交付代码，也关注业务价值实现。' },
];
</script>

<style lang="scss" scoped>
.hr-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.profile-container { display: flex; flex-direction: column; gap: 32rpx; }
.header-row { display: flex; flex-direction: column; gap: 24rpx; }
.status-row { display: flex; align-items: center; gap: 12rpx; color: #004ac6; font-size: 26rpx; font-weight: 900; margin-bottom: 12rpx; }
.page-title { display: block; color: #0b1c30; font-size: 52rpx; font-weight: 900; line-height: 1.2; }
.page-desc { display: block; margin-top: 10rpx; color: #565e74; font-size: 28rpx; line-height: 1.7; }
.header-actions { display: flex; flex-wrap: wrap; gap: 16rpx; }
.secondary-action, .primary-action { display: flex; align-items: center; justify-content: center; min-height: 76rpx; border-radius: 16rpx; padding: 0 32rpx; font-size: 26rpx; font-weight: 900; }
.secondary-action { border: 2rpx solid #737686; color: #0b1c30; background: #fff; }
.primary-action { background: #004ac6; color: #fff; }
.profile-grid { display: grid; gap: 32rpx; }
.main-stack, .side-stack { display: flex; flex-direction: column; gap: 28rpx; }
.glass-card { border: 2rpx solid #e2e8f0; border-radius: 24rpx; background: rgba(255,255,255,0.84); box-shadow: 0 8rpx 30rpx rgba(0,74,198,0.05); }
.card-block { padding: 32rpx; }
.card-title-row { display: flex; align-items: center; justify-content: space-between; gap: 20rpx; margin-bottom: 24rpx; }
.card-title { display: flex; align-items: center; gap: 12rpx; color: #0b1c30; font-size: 34rpx; font-weight: 900; }
.card-title.small { font-size: 30rpx; margin-bottom: 24rpx; }
.title-icon { display: inline-flex; align-items: center; justify-content: center; width: 42rpx; height: 42rpx; border-radius: 10rpx; background: rgba(0,74,198,0.1); color: #004ac6; font-size: 22rpx; font-weight: 900; }
.title-icon.filled { background: #004ac6; color: #fff; }
.title-icon.muted { background: rgba(86,94,116,0.12); color: #565e74; }
.edit-all { color: #004ac6; background: transparent; font-weight: 900; }
.info-grid { display: grid; gap: 24rpx; }
.field-label { display: block; color: #737686; font-size: 24rpx; margin-bottom: 8rpx; }
.editable, .editable-line { display: block; border-radius: 12rpx; padding: 14rpx; color: #0b1c30; background: #f8f9ff; font-size: 28rpx; font-weight: 800; }
.editable-line { flex: 1; background: transparent; font-weight: 700; }
.bullet-list { display: flex; flex-direction: column; gap: 20rpx; margin-top: 22rpx; }
.bullet-item { display: flex; gap: 18rpx; color: #434655; font-size: 27rpx; line-height: 1.6; }
.dot { width: 12rpx; height: 12rpx; border-radius: 999rpx; margin-top: 16rpx; background: #004ac6; flex-shrink: 0; }
.ai-suggest { display: flex; gap: 18rpx; margin-top: 28rpx; border: 2rpx solid #dbe1ff; border-radius: 18rpx; padding: 24rpx; background: rgba(219,225,255,0.28); color: #3f465c; font-size: 25rpx; line-height: 1.7; }
.suggest-copy { flex: 1; display: flex; flex-direction: column; gap: 10rpx; }
.suggest-strong { color: #004ac6; font-weight: 900; }
.suggest-link { align-self: flex-start; color: #004ac6; background: transparent; font-size: 24rpx; font-weight: 900; padding: 0; }
.skill-grid { display: grid; gap: 28rpx; }
.tag-row { display: flex; flex-wrap: wrap; gap: 12rpx; }
.add-tag { border: 2rpx dashed #c3c6d7; border-radius: 999rpx; padding: 8rpx 18rpx; color: #737686; font-size: 24rpx; font-weight: 800; }
.add-tag.muted { color: #565e74; }
.radar-card { padding: 32rpx; }
.radar-box { position: relative; width: 420rpx; max-width: 100%; aspect-ratio: 1; margin: 36rpx auto; }
.radar-layer, .radar-fill { position: absolute; left: 50%; top: 50%; transform: translate(-50%,-50%); clip-path: polygon(50% 0%,90% 32%,74% 92%,26% 92%,10% 32%); }
.radar-layer { border: 2rpx solid #e2e8f0; }
.one { width: 70%; height: 70%; } .two { width: 42%; height: 42%; }
.radar-fill { width: 62%; height: 66%; background: rgba(37,99,235,0.12); border: 4rpx solid #2563eb; }
.radar-label { position: absolute; color: #565e74; font-size: 21rpx; font-weight: 800; }
.radar-label.top { top: 0; left: 50%; transform: translateX(-50%); }
.radar-label.right { right: 0; top: 42%; }
.radar-label.left { left: 0; top: 42%; }
.radar-label.bottom-right { right: 28rpx; bottom: 20rpx; }
.radar-label.bottom-left { left: 28rpx; bottom: 20rpx; }
.score-list { display: flex; flex-direction: column; gap: 18rpx; }
.score-head { display: flex; justify-content: space-between; color: #434655; font-size: 25rpx; }
.score-value { color: #004ac6; font-weight: 900; }
.score-track { height: 10rpx; overflow: hidden; border-radius: 999rpx; background: #e5eeff; margin-top: 8rpx; }
.score-bar { height: 100%; border-radius: 999rpx; background: #004ac6; }
.persona-card { padding: 32rpx; }
.persona-list { display: flex; flex-direction: column; gap: 22rpx; margin-top: 24rpx; }
.persona-item { display: flex; gap: 16rpx; align-items: flex-start; }
.persona-title { display: block; color: #0b1c30; font-size: 27rpx; font-weight: 900; }
.persona-desc { display: block; margin-top: 4rpx; color: #565e74; font-size: 24rpx; line-height: 1.6; }
.missing-card { margin-top: 28rpx; border-left: 8rpx solid #ba1a1a; border-radius: 16rpx; padding: 22rpx; background: #fff7ed; }
.missing-title { display: block; color: #ba1a1a; font-size: 24rpx; font-weight: 900; }
.missing-desc { display: block; margin-top: 8rpx; color: #565e74; font-size: 24rpx; line-height: 1.55; }
.missing-action { margin-top: 8rpx; padding: 0; color: #004ac6; background: transparent; font-size: 24rpx; font-weight: 900; }
.insight-card { min-height: 320rpx; overflow: hidden; border-radius: 24rpx; background: linear-gradient(135deg, #dbeafe 0%, #004ac6 100%); box-shadow: 0 12rpx 36rpx rgba(0,74,198,0.16); }
.insight-overlay { display: flex; flex-direction: column; justify-content: flex-end; min-height: 320rpx; padding: 32rpx; background: linear-gradient(180deg, rgba(0,74,198,0.05), rgba(0,74,198,0.78)); }
.insight-title { color: #fff; font-size: 32rpx; font-weight: 900; }
.insight-desc { margin-top: 10rpx; color: rgba(255,255,255,0.88); font-size: 25rpx; line-height: 1.6; }
@media (min-width: 768px) { .header-row { flex-direction: row; justify-content: space-between; align-items: flex-end; } .profile-grid { grid-template-columns: 8fr 4fr; } .info-grid, .skill-grid { grid-template-columns: repeat(2,1fr); } }
</style>
