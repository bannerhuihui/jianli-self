<template>
  <view class="page hr-page">
    <AppTopNav active="HR 工作台" />

    <view class="container hr-container">
      <ProgressSteps v-bind="createFlowStepsProps(HR_FLOW, 0)" navigable />

      <section class="hero-block">
        <text class="page-title">输入岗位需求</text>
        <text class="page-desc">请在下方描述您的岗位需求。AI 将基于您的输入自动解析并生成专业的岗位画像与评估维度。</text>
      </section>

      <view class="job-layout">
        <section class="input-column">
          <view class="input-card">
            <view class="input-head">
              <view class="label-row">
                <AppIcon name="description" :size="20" color="#004ac6" />
                <text class="label-title">需求详细描述</text>
              </view>
              <button class="clear-button" @tap="clearInput">清空内容</button>
            </view>
            <textarea
              class="jd-input"
              :value="jdText"
              placeholder="请输入详细的岗位需求，例如：&#10;1. 岗位名称：高级前端开发工程师&#10;2. 工作年限：5-8年&#10;3. 核心技能：React, Next.js, TypeScript, Tailwind CSS&#10;4. 主要职责：负责 SaaS 平台核心业务开发，优化前端架构...&#10;5. 软素质：良好的跨部门沟通能力，极强的自驱力&#10;6. 职业偏好：有大型互联网公司背景，或者有 0 到 1 创业经验者优先。"
              @input="onJdInput"
            />
            <view class="input-actions">
              <button class="ai-button" @tap="goProfile">
                <text class="ai-button-text">生成岗位画像</text>
                <AppIcon name="auto_awesome" :size="20" color="#ffffff" />
              </button>
            </view>
          </view>

          <view class="template-section">
            <text class="template-title">示例模板</text>
            <view class="template-list">
              <view
                v-for="tpl in templates"
                :key="tpl.key"
                class="template-pill"
                @tap="useTemplate(tpl.key)"
              >
                <AppIcon :name="tpl.icon" :size="18" color="#0b1c30" />
                <text>{{ tpl.label }}</text>
              </view>
            </view>
          </view>
        </section>

        <aside class="guide-column">
          <view class="guide-card">
            <view class="guide-title-row">
              <AppIcon name="tips_and_updates" :size="20" color="#004ac6" />
              <text class="guide-title">撰写小贴士</text>
            </view>
            <view v-for="tip in tips" :key="tip.title" class="tip-item">
              <view class="tip-index">{{ tip.index }}</view>
              <view class="tip-copy">
                <text class="tip-title">{{ tip.title }}</text>
                <text class="tip-desc">{{ tip.desc }}</text>
              </view>
            </view>
          </view>

          <view class="preview-empty">
            <AppIcon name="preview" :size="48" color="#737686" />
            <text class="preview-title">画像预览区域</text>
            <text class="preview-desc">输入需求并点击生成后，这里将显示解析结果。</text>
          </view>
        </aside>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { HR_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppTopNav from '../../../components/AppTopNav.vue';
import AppIcon from '../../../components/AppIcon.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import { runAsyncAction, simulateDelay } from '../../../utils/feedback';

const templateTexts: Record<string, string> = {
  frontend: `岗位名称：高级前端工程师
工作年限：5年以上
核心技能：React, TypeScript, Next.js, Webpack, Tailwind CSS
岗位职责：
1. 负责核心 SaaS 产品的业务功能开发与维护。
2. 参与前端技术架构演进及性能优化。
3. 制定前端开发规范，进行 Code Review。
软素质：沟通能力强，具备良好的团队协作意识。`,
  pm: `岗位名称：资深产品经理
工作年限：3-5年
行业背景：互联网教育或 SaaS 领域优先
岗位职责：
1. 调研用户需求，独立完成产品方案设计与原型绘制。
2. 协调研发、UI、测试，确保产品高质量上线。
3. 分析业务数据，持续迭代优化产品功能。
核心优势：逻辑清晰，对数据敏感，抗压能力强。`,
  backend: `岗位名称：Java 开发专家
工作年限：8年以上
核心技能：Java, Spring Boot, Spring Cloud, Redis, MySQL, Kubernetes
岗位职责：
1. 负责高并发系统架构设计及核心代码编写。
2. 解决线上系统突发问题，保证系统高可用。
3. 指导中级开发人员，提升团队技术实力。`,
  hr: `岗位名称：人力资源经理
工作年限：5-10年
核心技能：人才招募、组织发展、薪酬绩效
岗位职责：
1. 负责公司高端人才的招聘及渠道维护。
2. 完善绩效评估体系，推动企业文化建设。
3. 建立人才梯队，协助各部门解决管理问题。`,
};

const jdText = ref(`岗位名称：高级架构师（Cloud & AI）
工作年限：5-8 年
核心技能：Node.js、Kubernetes、PostgreSQL、分布式系统
主要职责：负责高性能微服务架构设计，推动核心模块升级，指导初中级工程师
软素质：良好的跨团队沟通能力，极强的自驱力`);

const templates = [
  { key: 'frontend', label: '高级前端工程师', icon: 'code' },
  { key: 'pm', label: '资深产品经理', icon: 'inventory_2' },
  { key: 'backend', label: 'Java 开发专家', icon: 'terminal' },
  { key: 'hr', label: '人力资源经理', icon: 'groups' },
];

const tips = [
  { index: 1, title: '结构化输入', desc: '尽可能包含年限、技能、职责、软素质等明确维度。' },
  { index: 2, title: '强调关键特质', desc: '如“有跨团队协作经验”或“熟悉高并发架构”。' },
  { index: 3, title: '不限格式', desc: '您可以直接粘贴现有的 JD 文本，AI 会自动为您整理。' },
];

function onJdInput(event: Event) {
  const detail = (event as unknown as { detail?: { value?: string } }).detail;
  jdText.value = String(detail?.value ?? '');
}

function clearInput() {
  jdText.value = '';
}

function useTemplate(key: string) {
  jdText.value = templateTexts[key] || '';
}

async function goProfile() {
  await runAsyncAction(() => simulateDelay(900), {
    loading: '生成岗位画像',
    success: '岗位画像已生成',
  });
  uni.navigateTo({ url: '/pages/hr/profile/index' });
}
</script>

<style lang="scss" scoped>
.hr-container { display: flex; flex-direction: column; gap: 40rpx; padding-bottom: 48rpx; }
.hero-block { max-width: 896px; margin-bottom: 16rpx; }
.page-title { display: block; color: #0b1c30; font-size: 52rpx; font-weight: 600; line-height: 1.2; }
.page-desc { display: block; margin-top: 16rpx; color: #434655; font-size: 32rpx; line-height: 1.5; }

.job-layout { display: flex; flex-direction: column; gap: 48rpx; align-items: stretch; }
.input-column,
.guide-column { display: flex; flex-direction: column; gap: 48rpx; }

.input-card {
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  padding: 48rpx;
  background: #fff;
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
}
.input-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20rpx;
  margin-bottom: 32rpx;
}
.label-row { display: flex; align-items: center; gap: 16rpx; }
.label-title { color: #434655; font-size: 28rpx; font-weight: 600; line-height: 1.14; }
.clear-button {
  margin: 0;
  padding: 0;
  color: #565e74;
  background: transparent;
  font-size: 28rpx;
  font-weight: 500;
  &::after { border: none; }
}
.jd-input {
  width: 100%;
  min-height: 768rpx;
  border: none;
  border-radius: 16rpx;
  padding: 32rpx;
  box-sizing: border-box;
  background: #eff4ff;
  color: #0b1c30;
  font-size: 32rpx;
  line-height: 1.5;
}
.input-actions { display: flex; justify-content: flex-end; margin-top: 64rpx; }
.ai-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
  margin: 0;
  min-height: 96rpx;
  padding: 0 64rpx;
  border: none;
  border-radius: 16rpx;
  background: #004ac6;
  color: #fff;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1;
  box-sizing: border-box;
  box-shadow: 0 16rpx 40rpx rgba(0, 74, 198, 0.2);
  &::after { border: none; }
}
.ai-button-text {
  line-height: 1;
}

.template-section { padding: 0 8rpx; }
.template-title { color: #434655; font-size: 28rpx; font-weight: 600; line-height: 1.14; }
.template-list { display: flex; flex-wrap: wrap; gap: 24rpx; margin-top: 32rpx; }
.template-pill {
  display: flex;
  align-items: center;
  gap: 16rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 999rpx;
  padding: 16rpx 32rpx;
  background: #eff4ff;
  color: #0b1c30;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}

.guide-card {
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  padding: 48rpx;
  background: #e5eeff;
}
.guide-title-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
  margin-bottom: 32rpx;
}
.guide-title { color: #0b1c30; font-size: 40rpx; font-weight: 600; line-height: 1.4; }
.tip-item { display: flex; gap: 24rpx; margin-bottom: 32rpx; }
.tip-item:last-child { margin-bottom: 0; }
.tip-index {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 48rpx;
  height: 48rpx;
  border-radius: 999rpx;
  background: #004ac6;
  color: #fff;
  font-size: 24rpx;
  font-weight: 600;
}
.tip-copy { flex: 1; display: flex; flex-direction: column; gap: 8rpx; }
.tip-title { color: #0b1c30; font-size: 28rpx; font-weight: 600; line-height: 1.14; }
.tip-desc { color: #434655; font-size: 28rpx; line-height: 1.43; }

.preview-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 360rpx;
  padding: 96rpx 48rpx;
  border: 2rpx dashed #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  color: #434655;
  text-align: center;
  opacity: 0.6;
}
.preview-title { margin-top: 32rpx; font-size: 28rpx; font-weight: 600; line-height: 1.14; }
.preview-desc { margin-top: 8rpx; font-size: 28rpx; line-height: 1.43; }

@media (min-width: 768px) {
  .hero-block { margin-bottom: 40px; }
  .page-title { font-size: 36px; line-height: 1.2; }
  .page-desc { font-size: 16px; line-height: 24px; }
  .job-layout {
    display: grid;
    grid-template-columns: 8fr 4fr;
    gap: 24px;
    align-items: start;
  }
  .input-column,
  .guide-column { gap: 24px; }
  .input-card { padding: 24px; border-radius: 12px; }
  .label-title { font-size: 14px; }
  .clear-button { font-size: 14px; }
  .jd-input { min-height: 384px; font-size: 16px; padding: 16px; border-radius: 8px; }
  .input-actions { margin-top: 32px; }
  .ai-button { min-height: 48px; padding: 0 32px; font-size: 14px; border-radius: 12px; }
  .template-title { font-size: 14px; }
  .template-list { gap: 12px; margin-top: 16px; }
  .template-pill { font-size: 12px; padding: 8px 16px; }
  .guide-card { padding: 24px; border-radius: 12px; }
  .guide-title { font-size: 20px; line-height: 28px; }
  .tip-index { width: 24px; height: 24px; font-size: 12px; }
  .tip-title { font-size: 14px; }
  .tip-desc { font-size: 14px; }
  .preview-empty { min-height: 180px; padding: 48px 24px; border-radius: 12px; }
  .preview-title,
  .preview-desc { font-size: 14px; }
}
</style>
