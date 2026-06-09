<template>
  <view class="page hr-page">
    <AppTopNav active="HR 工作台" />
    <view class="container hr-container">
      <ProgressSteps :steps="hrSteps" :active-index="0" />

      <section class="hero-block">
        <text class="page-title">输入岗位需求</text>
        <text class="page-desc">请在下方描述您的岗位需求。AI 将基于输入自动解析并生成专业岗位画像与评估维度。</text>
      </section>

      <view class="job-layout">
        <section class="input-column">
          <view class="input-card">
            <view class="input-head">
              <text class="label-title">需求详细描述</text>
              <button class="clear-button">清空内容</button>
            </view>
            <textarea class="jd-input" :value="sampleJd" placeholder="请输入详细岗位需求，例如岗位名称、年限、技能、职责、软素质等" />
            <view class="input-actions">
              <navigator url="/pages/hr/profile/index" class="ai-button">生成岗位画像</navigator>
            </view>
          </view>

          <view class="template-section">
            <text class="template-title">示例模板</text>
            <view class="template-list">
              <text v-for="tpl in templates" :key="tpl" class="template-pill">{{ tpl }}</text>
            </view>
          </view>
        </section>

        <aside class="guide-column">
          <view class="guide-card">
            <text class="guide-title">撰写小贴士</text>
            <view v-for="tip in tips" :key="tip.title" class="tip-item">
              <view class="tip-index">{{ tip.index }}</view>
              <view class="tip-copy"><text class="tip-title">{{ tip.title }}</text><text class="tip-desc">{{ tip.desc }}</text></view>
            </view>
          </view>
          <view class="preview-empty">
            <text class="preview-icon"><AppIcon name="list" :size="46" color="#737686" /></text>
            <text class="preview-title">画像预览区域</text>
            <text class="preview-desc">生成后将在这里展示岗位画像摘要。</text>
          </view>
        </aside>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import AppTopNav from '../../../components/AppTopNav.vue';
import AppIcon from '../../../components/AppIcon.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';

const hrSteps = ['岗位需求', '岗位画像', '候选人推荐', '匹配详情', '导出中心'];
const sampleJd = `岗位名称：高级架构师（Cloud & AI）
工作年限：5-8 年
核心技能：Node.js、Kubernetes、PostgreSQL、分布式系统
主要职责：负责高性能微服务架构设计，推动核心模块升级，指导初中级工程师
软素质：良好的跨团队沟通能力，极强的自驱力`;
const templates = ['高级前端工程师', '资深产品经理', 'Java 开发专家', '人力资源经理'];
const tips = [
  { index: 1, title: '结构化输入', desc: '尽可能包含年限、技能、职责、软素质等明确维度。' },
  { index: 2, title: '强调关键特质', desc: '如跨团队协作经验、熟悉高并发架构、业务理解能力。' },
  { index: 3, title: '不限格式', desc: '可以直接粘贴现有 JD 文本，AI 会自动整理。' },
];
</script>

<style lang="scss" scoped>
.hr-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.hr-container { display: flex; flex-direction: column; gap: 40rpx; }
.hero-block { max-width: 980rpx; }
.page-title { display: block; color: #0b1c30; font-size: 52rpx; font-weight: 900; line-height: 1.2; }
.page-desc { display: block; margin-top: 14rpx; color: #565e74; font-size: 28rpx; line-height: 1.7; }
.job-layout { display: grid; gap: 32rpx; align-items: start; }
.input-column, .guide-column { display: flex; flex-direction: column; gap: 28rpx; }
.input-card, .guide-card, .preview-empty { border: 2rpx solid #c3c6d7; border-radius: 24rpx; background: #fff; box-shadow: 0 6rpx 22rpx rgba(0,74,198,0.05); }
.input-card { padding: 32rpx; }
.input-head { display: flex; align-items: center; justify-content: space-between; gap: 20rpx; margin-bottom: 22rpx; }
.label-title { color: #565e74; font-size: 26rpx; font-weight: 900; }
.clear-button { color: #565e74; background: transparent; font-size: 24rpx; }
.jd-input { width: 100%; min-height: 560rpx; border: 0; border-radius: 18rpx; padding: 26rpx; box-sizing: border-box; background: #eff4ff; color: #0b1c30; font-size: 28rpx; line-height: 1.65; }
.input-actions { display: flex; justify-content: flex-end; margin-top: 28rpx; }
.ai-button { display: flex; align-items: center; justify-content: center; min-height: 88rpx; border-radius: 18rpx; padding: 0 42rpx; background: #004ac6; color: #fff; font-size: 28rpx; font-weight: 900; box-shadow: 0 12rpx 28rpx rgba(0,74,198,0.18); }
.template-title { color: #565e74; font-size: 26rpx; font-weight: 900; }
.template-list { display: flex; flex-wrap: wrap; gap: 16rpx; margin-top: 18rpx; }
.template-pill { border: 2rpx solid #c3c6d7; border-radius: 999rpx; padding: 14rpx 22rpx; background: #eff4ff; color: #0b1c30; font-size: 24rpx; font-weight: 800; }
.guide-card { padding: 32rpx; }
.guide-title { display: block; margin-bottom: 28rpx; color: #0b1c30; font-size: 34rpx; font-weight: 900; }
.tip-item { display: flex; gap: 18rpx; margin-bottom: 28rpx; }
.tip-index { display: flex; align-items: center; justify-content: center; width: 44rpx; height: 44rpx; border-radius: 999rpx; background: #004ac6; color: #fff; font-weight: 900; }
.tip-copy { flex: 1; display: flex; flex-direction: column; gap: 8rpx; }
.tip-title { color: #0b1c30; font-weight: 900; }
.tip-desc { color: #565e74; font-size: 25rpx; line-height: 1.55; }
.preview-empty { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 300rpx; padding: 40rpx; border-style: dashed; color: #737686; text-align: center; }
.preview-icon { font-size: 64rpx; opacity: 0.6; }
.preview-title { margin-top: 16rpx; font-weight: 900; }
.preview-desc { margin-top: 8rpx; font-size: 24rpx; }
@media (min-width: 768px) { .job-layout { grid-template-columns: 8fr 4fr; } }
</style>
