<template>
  <view class="page review-page">
    <AppTopNav active="求职者流程" />

    <view class="container flow-container">
      <ProgressSteps :steps="candidateSteps" :active-index="1" />
    </view>

    <view class="review-shell">
      <section class="document-panel">
        <view class="panel-toolbar">
          <text class="panel-heading">原始简历文件</text>
          <view class="toolbar-actions">
            <button class="tool-button">+</button>
            <button class="tool-button">-</button>
            <button class="tool-button">↓</button>
          </view>
        </view>

        <scroll-view scroll-y class="resume-paper">
          <view class="paper-content">
            <text class="resume-name">{{ resume.basicInfo.name }}的简历</text>
            <view class="contact-row">
              <text>{{ resume.basicInfo.phone }}</text>
              <text>{{ resume.basicInfo.email }}</text>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">教育背景</text>
              <view class="resume-line-between"><text>{{ resume.basicInfo.education }}</text><text>2016 - 2020</text></view>
              <text class="resume-subline">学士学位</text>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">工作经历</text>
              <view class="resume-line-between"><text>阿里巴巴 - 高级软件工程师</text><text>2020 - 至今</text></view>
              <view class="resume-bullets">
                <text v-for="item in resume.workExperience" :key="item">• {{ item }}</text>
              </view>
            </view>

            <view class="resume-section">
              <text class="resume-section-title">专业技能</text>
              <text class="resume-subline">{{ resume.skills.join(', ') }}</text>
            </view>
          </view>
          <view class="scan-overlay"><view class="scan-line" /></view>
        </scroll-view>
      </section>

      <section class="review-panel">
        <view class="review-header">
          <view>
            <view class="title-row">
              <text class="review-title">智能解析预览</text>
              <AppTag label="AI 已校对完成" tone="green" />
            </view>
            <text class="review-desc">请确认 AI 提取的信息，带红色指示符的字段可能需要手动复核。</text>
          </view>
          <view class="quality-card">
            <view class="quality-head"><text>解析质量评分</text><text class="quality-score">86%</text></view>
            <view class="quality-track"><view class="quality-value" /></view>
            <text class="quality-note">发现 2 个低置信度字段，建议确认后再进入 AI 访谈。</text>
          </view>
        </view>

        <view class="field-card">
          <view class="field-head">
            <text class="field-title">基本信息</text>
            <AppTag label="置信度: 高" tone="green" />
          </view>
          <view class="form-grid">
            <view class="form-field"><text class="field-label">姓名</text><input class="text-input" :value="resume.basicInfo.name" /></view>
            <view class="form-field"><text class="field-label">联系电话</text><input class="text-input" :value="resume.basicInfo.phone" /></view>
            <view class="form-field full"><text class="field-label">邮箱</text><input class="text-input" :value="resume.basicInfo.email" /></view>
          </view>
        </view>

        <view class="field-card low-confidence">
          <view class="field-head">
            <text class="field-title">教育经历</text>
            <AppTag label="置信度: 低 - 建议复核" tone="amber" />
          </view>
          <view class="form-grid">
            <view class="form-field"><text class="field-label">院校</text><input class="text-input warning" value="清华大学" /></view>
            <view class="form-field"><text class="field-label">专业</text><input class="text-input" value="计算机科学与技术" /></view>
            <view class="form-field full"><text class="field-label">学位</text><input class="text-input" value="学士" /></view>
          </view>
        </view>

        <view class="field-card">
          <view class="field-head">
            <text class="field-title">工作经历</text>
            <AppTag label="置信度: 极高" tone="green" />
          </view>
          <view class="experience-item">
            <view class="experience-top"><text class="experience-title">阿里巴巴 - 高级软件工程师</text><button class="edit-button">编辑</button></view>
            <text class="experience-time">2020.07 - 至今 (4年1个月)</text>
            <text class="experience-desc">主导微服务架构升级，支持千万级 QPS 大促系统。</text>
          </view>
          <button class="add-button">+ 添加工作经历</button>
        </view>

        <view class="field-card">
          <view class="field-head">
            <text class="field-title">项目经历</text>
            <AppTag label="置信度: 中" tone="blue" />
          </view>
          <view class="project-list">
            <view v-for="project in resume.projects" :key="project" class="project-item">
              <text class="project-title">{{ project }}</text>
              <text class="project-desc">AI 已识别项目名称，但项目背景、职责边界和量化结果仍建议补充。</text>
            </view>
          </view>
          <button class="add-button">+ 添加项目经历</button>
        </view>

        <view class="field-card">
          <view class="field-head">
            <text class="field-title">技能标签</text>
            <AppTag label="置信度: 高" tone="green" />
          </view>
          <view class="skill-list">
            <text v-for="skill in resume.skills" :key="skill" class="skill-pill">{{ skill }} ×</text>
            <text class="add-skill">+ 添加</text>
          </view>
        </view>

        <view class="missing-card">
          <text class="missing-title">缺失字段提示</text>
          <view class="missing-list">
            <text>• 建议补充最近一次离职原因，用于职业稳定性判断。</text>
            <text>• 建议补充 1-2 个项目的量化结果，提升画像证据强度。</text>
            <text>• 若简历解析不准确，可重新上传或手动录入。</text>
          </view>
        </view>

        <view class="sticky-action">
          <view class="secondary-actions">
            <navigator url="/pages/candidate/upload/index" class="secondary-button small-action">重新上传</navigator>
            <button class="secondary-button small-action">手动录入</button>
            <button class="secondary-button small-action">保存修改</button>
          </view>
          <navigator url="/pages/candidate/interview/index" class="primary-button confirm-button">确认并进入 AI 访谈</navigator>
        </view>
      </section>
    </view>
  </view>
</template>

<script setup lang="ts">
import { mockResume } from '@ai-talent-agent/shared';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';

const candidateSteps = ['上传简历', '简历校对', 'AI 访谈', '人才画像', '简历生成'];
const resume = mockResume;
</script>

<style lang="scss" scoped>
.review-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.flow-container { padding-bottom: 0; }
.review-shell { display: grid; min-height: calc(100vh - 128rpx); }
.document-panel { display: flex; flex-direction: column; gap: 24rpx; padding: 32rpx; background: #cbdbf5; }
.panel-toolbar { display: flex; align-items: center; justify-content: space-between; gap: 24rpx; }
.panel-heading { color: #0b1c30; font-size: 34rpx; font-weight: 900; }
.toolbar-actions { display: flex; gap: 12rpx; }
.tool-button { display: flex; align-items: center; justify-content: center; width: 64rpx; height: 64rpx; border-radius: 14rpx; background: rgba(255,255,255,0.7); color: #0b1c30; font-size: 28rpx; font-weight: 900; }
.resume-paper { position: relative; flex: 1; min-height: 720rpx; overflow: hidden; border: 2rpx solid #c3c6d7; border-radius: 24rpx; background: #fff; box-shadow: 0 16rpx 40rpx rgba(11,28,48,0.12); }
.paper-content { max-width: 760rpx; margin: 0 auto; padding: 72rpx; color: #334155; }
.resume-name { display: block; margin-bottom: 24rpx; font-size: 48rpx; font-weight: 900; }
.contact-row { display: flex; flex-wrap: wrap; gap: 24rpx; margin-bottom: 48rpx; color: #64748b; font-size: 24rpx; }
.resume-section { display: flex; flex-direction: column; gap: 14rpx; margin-bottom: 40rpx; }
.resume-section-title { border-bottom: 2rpx solid #e2e8f0; padding-bottom: 8rpx; color: #0f172a; font-size: 30rpx; font-weight: 900; }
.resume-line-between { display: flex; justify-content: space-between; gap: 20rpx; color: #334155; font-weight: 800; }
.resume-subline { color: #64748b; font-size: 25rpx; line-height: 1.6; }
.resume-bullets { display: flex; flex-direction: column; gap: 8rpx; color: #475569; font-size: 24rpx; line-height: 1.6; }
.scan-overlay { position: absolute; inset: 0; pointer-events: none; border: 4rpx solid rgba(0,74,198,0.2); border-radius: 24rpx; }
.scan-line { position: absolute; left: 0; right: 0; top: 20%; height: 4rpx; background: linear-gradient(90deg, transparent, #004ac6, transparent); }
.review-panel { display: flex; flex-direction: column; gap: 28rpx; padding: 32rpx; background: #fff; }
.review-header { display: flex; flex-direction: column; gap: 20rpx; }
.title-row { display: flex; flex-wrap: wrap; align-items: center; gap: 16rpx; }
.review-title { color: #0b1c30; font-size: 40rpx; font-weight: 900; }
.review-desc { display: block; margin-top: 12rpx; color: #565e74; font-size: 27rpx; line-height: 1.7; }
.quality-card { border: 2rpx solid #dbe1ff; border-radius: 20rpx; padding: 24rpx; background: rgba(219,225,255,0.22); }
.quality-head { display: flex; justify-content: space-between; color: #0b1c30; font-weight: 900; }
.quality-score { color: #004ac6; font-size: 34rpx; }
.quality-track { height: 12rpx; overflow: hidden; border-radius: 999rpx; margin-top: 14rpx; background: #d3e4fe; }
.quality-value { width: 86%; height: 100%; background: #004ac6; }
.quality-note { display: block; margin-top: 12rpx; color: #565e74; font-size: 24rpx; line-height: 1.5; }
.field-card { display: flex; flex-direction: column; gap: 24rpx; border: 2rpx solid #e2e8f0; border-radius: 24rpx; padding: 32rpx; background: #fff; box-shadow: 0 6rpx 20rpx rgba(0,74,198,0.04); }
.low-confidence { border-left: 8rpx solid #ba1a1a; }
.field-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 16rpx; }
.field-title { color: #565e74; font-size: 24rpx; font-weight: 900; letter-spacing: 2rpx; }
.form-grid { display: grid; gap: 20rpx; }
.form-field { display: flex; flex-direction: column; gap: 8rpx; }
.field-label { color: #565e74; font-size: 24rpx; }
.text-input { min-height: 76rpx; border: 2rpx solid #c3c6d7; border-radius: 16rpx; padding: 0 20rpx; color: #0b1c30; font-size: 28rpx; background: #fff; box-sizing: border-box; }
.text-input.warning { border-color: #ffdad6; background: rgba(255,218,214,0.2); }
.experience-item { border: 2rpx solid rgba(195,198,215,0.45); border-radius: 18rpx; padding: 24rpx; background: #eff4ff; }
.experience-top { display: flex; align-items: center; justify-content: space-between; gap: 16rpx; }
.experience-title { color: #0b1c30; font-weight: 900; }
.edit-button { color: #737686; font-size: 24rpx; background: transparent; }
.experience-time { display: block; margin-top: 10rpx; color: #565e74; font-size: 24rpx; }
.experience-desc { display: block; margin-top: 14rpx; color: #434655; font-size: 25rpx; line-height: 1.6; }
.add-button { min-height: 72rpx; border: 4rpx dashed #c3c6d7; border-radius: 16rpx; color: #565e74; background: #fff; font-weight: 900; }
.project-list { display: flex; flex-direction: column; gap: 16rpx; }
.project-item { border: 2rpx solid rgba(195,198,215,0.45); border-radius: 18rpx; padding: 22rpx; background: #f8f9ff; }
.project-title { display: block; color: #0b1c30; font-weight: 900; }
.project-desc { display: block; margin-top: 8rpx; color: #565e74; font-size: 24rpx; line-height: 1.55; }
.skill-list { display: flex; flex-wrap: wrap; gap: 14rpx; }
.skill-pill { border-radius: 999rpx; padding: 10rpx 18rpx; background: #2563eb; color: #fff; font-size: 24rpx; font-weight: 800; }
.add-skill { border: 2rpx solid #004ac6; border-radius: 999rpx; padding: 8rpx 18rpx; color: #004ac6; font-size: 24rpx; font-weight: 900; }
.missing-card { border: 2rpx solid #fde68a; border-radius: 24rpx; padding: 28rpx; background: #fffbeb; }
.missing-title { color: #92400e; font-size: 28rpx; font-weight: 900; }
.missing-list { display: flex; flex-direction: column; gap: 10rpx; margin-top: 16rpx; color: #92400e; font-size: 25rpx; line-height: 1.6; }
.sticky-action { position: sticky; bottom: 0; display: flex; flex-direction: column; gap: 16rpx; padding: 24rpx 0; background: rgba(255,255,255,0.9); }
.secondary-actions { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12rpx; }
.small-action { min-height: 72rpx; font-size: 24rpx; }
.confirm-button { min-height: 96rpx; }
@media (min-width: 768px) {
  .review-shell { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .document-panel, .review-panel { padding: 64rpx 80rpx; }
  .form-grid { grid-template-columns: repeat(2, 1fr); }
  .form-field.full { grid-column: span 2; }
}
</style>
