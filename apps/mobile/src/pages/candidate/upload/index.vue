<template>
  <view class="page upload-page">
    <AppTopNav active="求职者流程" />

    <view class="container upload-container">
      <ProgressSteps :steps="candidateSteps" :active-index="0" />

      <view v-if="phase === 'parsing'" class="state-wrap">
        <StatePanel tone="loading" icon="loop" icon-color="#004ac6" title="正在解析简历" description="AI 正在识别教育背景、工作经历、项目成果与技能标签，通常需要 10-20 秒。" />
      </view>

      <view v-else-if="phase === 'error'" class="state-wrap">
        <StatePanel tone="error" icon="info" icon-color="#ba1a1a" title="简历解析失败" description="未能从当前文件中稳定提取结构化信息。可能是扫描件、复杂排版或文件损坏导致。">
          <view class="state-actions">
            <button class="secondary-button action-btn" @tap="phase = 'selected'">重新选择文件</button>
            <button class="primary-button action-btn" @tap="retryParse">重试解析</button>
            <navigator url="/pages/candidate/review/index?source=manual" class="link-button">改用手动录入 →</navigator>
          </view>
        </StatePanel>
      </view>

      <template v-else>
        <view class="upload-grid">
          <view class="left-column">
            <view class="drop-zone" @tap="selectFile">
              <view class="upload-icon"><AppIcon name="cloud-upload" :size="54" color="#004ac6" /></view>
              <text v-if="!fileName" class="upload-title">拖拽文件至此 或 点击浏览</text>
              <text v-else class="upload-title">已选择：{{ fileName }}</text>
              <text class="upload-desc">支持 PDF、Word、Docx 或 JPG 图片格式，最大 20MB</text>

              <view v-if="fileName && uploadProgress > 0" class="progress-card">
                <view class="progress-head"><text>上传完成</text><text>{{ uploadProgress }}%</text></view>
                <view class="progress-track"><view class="progress-value" :style="{ width: `${uploadProgress}%` }" /></view>
              </view>

              <view v-if="!fileName" class="empty-hint">
                <AppIcon name="info" :size="16" color="#565e74" />
                <text>尚未选择文件，请先上传简历</text>
              </view>
            </view>

            <view class="warning-card">
              <text class="warning-icon"><AppIcon name="info" :size="18" color="#ffffff" /></text>
              <view class="warning-copy">
                <text class="warning-title">温馨提示</text>
                <text class="warning-text">检测到复杂排版可能影响解析准确率。建议使用标准单栏或双栏格式，并尽量避免背景图片或复杂表格装饰。</text>
              </view>
            </view>
          </view>

          <view class="glass-panel right-column">
            <view class="panel-content">
              <view class="panel-title-row"><text class="panel-icon"><AppIcon name="search" :size="18" color="#004ac6" /></text><text class="panel-title">AI 深度解析</text></view>
              <text class="panel-desc">我们将通过 AI 技术深度解析您的经历，并生成初步的人才画像。系统会自动识别以下关键信息：</text>
              <view class="check-list">
                <view v-for="item in checks" :key="item" class="check-item"><text class="check-icon"><AppIcon name="checkmarkempty" :size="17" color="#007d55" /></text><text>{{ item }}</text></view>
              </view>
              <view class="quote-box">“AI 助理正在就位，准备为您打造专业档案。”</view>
              <text class="demo-link" @tap="enableFailDemo">查看解析失败示例</text>
            </view>
            <text class="data-mark">{ }</text>
          </view>
        </view>

        <view class="action-area">
          <view class="security"><text class="security-icon"><AppIcon name="locked" :size="17" color="#004ac6" /></text><text>您的简历数据已加密，仅用于画像生成</text></view>
          <view class="action-buttons">
            <navigator url="/pages/index/index" class="secondary-button action-btn">取消</navigator>
            <button class="primary-button action-btn" @tap="startParse">开始解析</button>
          </view>
        </view>
      </template>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import AppIcon from '../../../components/AppIcon.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { showToast, simulateDelay } from '../../../utils/feedback';

const candidateSteps = ['上传简历', '简历校对', 'AI 访谈', '人才画像', '简历生成'];
const checks = ['核心技能与技术栈', '职业发展轨迹分析', '项目成就与影响力'];

type UploadPhase = 'idle' | 'selected' | 'parsing' | 'error';
const phase = ref<UploadPhase>('idle');
const fileName = ref('');
const uploadProgress = ref(0);
const simulateFail = ref(false);

function selectFile() {
  fileName.value = 'Resume_2024.pdf';
  uploadProgress.value = 100;
  phase.value = 'selected';
  showToast('文件已选择', 'success');
}

function enableFailDemo() {
  simulateFail.value = true;
  showToast('下次解析将模拟失败');
}

async function startParse() {
  if (!fileName.value) {
    showToast('请先选择简历文件');
    return;
  }
  phase.value = 'parsing';
  await simulateDelay(1600);
  if (simulateFail.value) {
    phase.value = 'error';
    simulateFail.value = false;
    return;
  }
  uni.navigateTo({ url: '/pages/candidate/review/index' });
}

function retryParse() {
  startParse();
}
</script>

<style lang="scss" scoped>
.upload-page { background: #f8f9ff; }
.upload-container { display: flex; flex-direction: column; gap: 48rpx; }
.state-wrap { padding: 24rpx 0; }
.state-actions { display: flex; flex-direction: column; gap: 16rpx; width: 100%; margin-top: 12rpx; }
.link-button { color: #004ac6; font-size: 26rpx; font-weight: 900; text-align: center; }
.upload-grid { display: grid; gap: 32rpx; }
.left-column { display: flex; flex-direction: column; gap: 32rpx; }
.drop-zone { position: relative; display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 520rpx; padding: 48rpx; border: 4rpx dashed #c3c6d7; border-radius: 24rpx; background: #fff; text-align: center; }
.upload-icon { display: flex; align-items: center; justify-content: center; width: 144rpx; height: 144rpx; margin-bottom: 36rpx; border-radius: 999rpx; background: #dbe1ff; color: #004ac6; font-size: 64rpx; font-weight: 900; }
.upload-title { color: #0b1c30; font-size: 34rpx; font-weight: 900; }
.upload-desc { margin-top: 16rpx; color: #434655; font-size: 26rpx; line-height: 1.6; }
.empty-hint { display: flex; align-items: center; gap: 10rpx; margin-top: 28rpx; color: #565e74; font-size: 24rpx; }
.progress-card { width: 100%; max-width: 520rpx; margin-top: 44rpx; }
.progress-head { display: flex; justify-content: space-between; margin-bottom: 12rpx; color: #004ac6; font-size: 24rpx; font-weight: 800; }
.progress-track { height: 14rpx; overflow: hidden; border-radius: 999rpx; background: #e5eeff; }
.progress-value { height: 100%; background: #004ac6; transition: width 0.3s ease; }
.warning-card { display: flex; gap: 24rpx; align-items: flex-start; border: 2rpx solid rgba(186, 26, 26, 0.2); border-radius: 24rpx; padding: 32rpx; background: rgba(255, 218, 214, 0.22); }
.warning-icon { display: flex; align-items: center; justify-content: center; width: 48rpx; height: 48rpx; border-radius: 999rpx; background: #ba1a1a; color: #fff; font-weight: 900; }
.warning-copy { flex: 1; display: flex; flex-direction: column; gap: 8rpx; }
.warning-title { color: #93000a; font-weight: 900; }
.warning-text { color: #93000a; font-size: 26rpx; line-height: 1.7; }
.glass-panel { position: relative; overflow: hidden; border: 2rpx solid #e2e8f0; border-radius: 24rpx; padding: 48rpx; background: rgba(255,255,255,0.82); box-shadow: 0 8rpx 40rpx rgba(0, 74, 198, 0.08); }
.panel-content { position: relative; z-index: 1; display: flex; flex-direction: column; gap: 28rpx; }
.panel-title-row { display: flex; align-items: center; gap: 16rpx; }
.panel-icon { color: #004ac6; font-weight: 900; }
.panel-title { color: #0b1c30; font-size: 34rpx; font-weight: 900; }
.panel-desc { color: #434655; font-size: 28rpx; line-height: 1.7; }
.check-list { display: flex; flex-direction: column; gap: 24rpx; }
.check-item { display: flex; align-items: center; gap: 16rpx; color: #0b1c30; font-weight: 800; }
.check-icon { color: #007d55; font-weight: 900; }
.quote-box { border: 2rpx solid rgba(0, 74, 198, 0.1); border-radius: 16rpx; padding: 24rpx; background: rgba(0, 74, 198, 0.05); color: #004ac6; font-size: 24rpx; font-weight: 800; }
.demo-link { color: #737686; font-size: 24rpx; text-decoration: underline; }
.data-mark { position: absolute; right: -40rpx; bottom: -30rpx; color: #004ac6; opacity: 0.08; font-size: 220rpx; font-weight: 900; }
.action-area { display: flex; flex-direction: column; gap: 28rpx; border-top: 2rpx solid #c3c6d7; padding-top: 40rpx; }
.security { display: flex; align-items: center; gap: 14rpx; color: #565e74; font-size: 25rpx; }
.security-icon { color: #004ac6; }
.action-buttons { display: flex; gap: 20rpx; }
.action-btn { flex: 1; }
@media (min-width: 768px) {
  .upload-grid { grid-template-columns: 2fr 1fr; }
  .action-area { flex-direction: row; align-items: center; justify-content: space-between; }
  .action-buttons { min-width: 520rpx; }
  .state-actions { flex-direction: row; flex-wrap: wrap; justify-content: center; }
  .state-actions .action-btn { flex: none; min-width: 220rpx; }
}
</style>
