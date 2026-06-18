<template>
  <view class="page candidate-flow-page upload-page">
    <AppTopNav active="求职者流程" />

    <view class="container upload-container">
      <ProgressSteps v-bind="createFlowStepsProps(CANDIDATE_FLOW, 0)" navigable />

      <view v-if="phase === 'parsing'" class="parsing-overlay">
        <view class="parsing-modal">
          <view class="scan-card">
            <view class="scan-line" />
            <view class="scan-skeleton">
              <view class="scan-bar w-75" />
              <view class="scan-bar w-full" />
              <view class="scan-bar w-50" />
              <view class="scan-bar w-85" />
              <view class="scan-bar w-66" />
            </view>
          </view>
          <text class="parsing-title">AI 正在深度解析中...</text>
          <text class="parsing-desc">我们正在扫描您的简历细节，提取核心技能与项目经验。这通常需要 1-2 分钟，请耐心等待。</text>
          <view class="parsing-track"><view class="parsing-bar" /></view>
          <text class="parsing-tag">处理数据资产</text>
        </view>
      </view>

      <view v-else-if="phase === 'error'" class="state-wrap">
        <StatePanel tone="error" icon="warning" icon-color="#ba1a1a" title="简历解析失败" :description="errorMessage">
          <view class="state-actions">
            <button class="flow-btn flow-btn--secondary" @tap="phase = 'selected'">重新选择文件</button>
            <button class="flow-btn flow-btn--primary" @tap="retryParse">重试解析</button>
            <navigator url="/pages/candidate/review/index?source=manual" class="flow-btn flow-btn--link">改用手动录入 →</navigator>
          </view>
        </StatePanel>
      </view>

      <template v-else>
        <view class="upload-grid">
          <view class="left-column">
            <view class="drop-zone" :class="{ active: fileName }" @tap="selectFile">
              <template v-if="!fileName">
                <view class="upload-icon-wrap">
                  <AppIcon name="upload_file" :size="40" color="#004ac6" filled />
                </view>
                <text class="upload-title">拖拽文件至此 或 <text class="upload-link">点击浏览</text></text>
                <text class="upload-desc">支持 PDF、Word、Docx 或 JPG 图片格式，最大 20MB</text>
              </template>

              <template v-else>
                <view class="selected-file">
                  <view class="file-icon">
                    <AppIcon name="description" :size="40" color="#004ac6" filled />
                  </view>
                  <view class="file-info">
                    <text class="file-name">{{ fileName }}</text>
                    <view class="file-status">
                      <AppIcon name="check_circle" :size="18" color="#006242" filled />
                      <text>文件已就绪，点击下方「开始解析」</text>
                    </view>
                  </view>
                </view>

                <view v-if="uploadProgress > 0" class="progress-card">
                  <view class="progress-head">
                    <text>正在上传: {{ fileName }}</text>
                    <text>{{ uploadProgress }}%</text>
                  </view>
                  <view class="progress-track"><view class="progress-value" :style="{ width: `${uploadProgress}%` }" /></view>
                </view>

                <button class="flow-btn flow-btn--link reselect-btn" @tap.stop="selectFile">
                  <AppIcon name="autorenew" :size="16" color="#004ac6" />
                  <text>重新选择文件</text>
                </button>
              </template>
            </view>

            <view class="warning-card">
              <AppIcon name="warning" :size="24" color="#ba1a1a" filled />
              <view class="warning-copy">
                <text class="warning-title">温馨提示</text>
                <text class="warning-text">检测到复杂排版可能影响解析准确率。建议使用标准单栏或双栏格式，并尽量避免背景图片或复杂表格装饰。</text>
              </view>
            </view>
          </view>

          <view class="glass-panel right-column">
            <view class="panel-content">
              <view class="panel-title-row">
                <AppIcon name="psychology" :size="24" color="#004ac6" />
                <text class="panel-title">AI 深度解析</text>
              </view>
              <text class="panel-desc">我们将通过 AI 技术深度解析您的经历，并生成初步的人才画像。系统会自动识别以下关键信息：</text>
              <view class="check-list">
                <view v-for="item in checks" :key="item" class="check-item">
                  <AppIcon name="check_circle" :size="18" color="#006242" />
                  <text>{{ item }}</text>
                </view>
              </view>
              <view class="quote-box">“AI 助理正在就位，准备为您打造专业档案。”</view>
            </view>
          </view>
        </view>

        <view class="action-area">
          <view class="security">
            <AppIcon name="security" :size="18" color="#565e74" />
            <text>您的简历数据已加密，仅用于画像生成</text>
          </view>
          <view class="action-buttons">
            <navigator url="/pages/index/index" class="flow-btn flow-btn--secondary">取消</navigator>
            <button class="flow-btn flow-btn--primary" :disabled="!filePath" @tap="startParse">
              <AppIcon name="auto_awesome" :size="18" color="#ffffff" />
              <text>{{ filePath ? '开始解析' : '请先选择文件' }}</text>
            </button>
          </view>
        </view>
      </template>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ApiClientError, uploadAndParseResume } from '@ai-talent-agent/api';
import { CANDIDATE_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppTopNav from '../../../components/AppTopNav.vue';
import AppIcon from '../../../components/AppIcon.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { showToast } from '../../../utils/feedback';

const checks = ['核心技能与技术栈', '职业发展轨迹分析', '项目成就与影响力'];

type UploadPhase = 'idle' | 'selected' | 'parsing' | 'error';
const phase = ref<UploadPhase>('idle');
const fileName = ref('');
const filePath = ref('');
const uploadProgress = ref(0);
const parsing = ref(false);
const errorMessage = ref('未能从当前文件中稳定提取结构化信息。可能是扫描件、复杂排版或文件损坏导致。');

function selectFile() {
  uni.chooseFile({
    count: 1,
    type: 'all',
    extension: ['.pdf', '.doc', '.docx'],
    success: (res) => {
      const files = Array.isArray(res.tempFiles) ? res.tempFiles : [res.tempFiles];
      const file = files[0] as { name?: string; path?: string } | undefined;
      if (!file || !file.path) {
        showToast('未选择文件');
        return;
      }
      fileName.value = file.name || 'resume.pdf';
      filePath.value = file.path;
      uploadProgress.value = 0;
      phase.value = 'selected';
      showToast('文件已选择', 'success');
    },
    fail: () => {
      showToast('选择文件失败');
    },
  });
}

async function startParse() {
  if (!filePath.value) {
    showToast('请先选择简历文件');
    return;
  }

  phase.value = 'parsing';
  parsing.value = true;
  uploadProgress.value = 0;

  try {
    await uploadAndParseResume(filePath.value, {
      onUploadProgress: (percent) => {
        uploadProgress.value = percent;
      },
    });
    uni.navigateTo({ url: '/pages/candidate/review/index' });
  } catch (error) {
    phase.value = 'error';
    if (error instanceof ApiClientError) {
      errorMessage.value = error.message;
    }
  } finally {
    parsing.value = false;
  }
}

function retryParse() {
  startParse();
}
</script>

<style lang="scss" scoped>
.upload-container { display: flex; flex-direction: column; gap: 48rpx; padding-top: 48rpx; padding-bottom: 80rpx; }

/* Parsing overlay — ai_talent_agent_2 */
.parsing-overlay {
  position: fixed;
  inset: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32rpx;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(12px);
}
.parsing-modal {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  max-width: 720rpx;
  padding: 80rpx 64rpx;
  border-radius: 32rpx;
  background: #fff;
  box-shadow: 0 24rpx 64rpx rgba(15, 23, 42, 0.2);
  text-align: center;
}
.scan-card {
  position: relative;
  overflow: hidden;
  width: 192rpx;
  height: 256rpx;
  margin-bottom: 64rpx;
  border: 4rpx solid #dbe1ff;
  border-radius: 16rpx;
}
.scan-line {
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  height: 4rpx;
  background: linear-gradient(90deg, transparent, #2563eb, transparent);
  animation: scan 3s linear infinite;
}
@keyframes scan {
  0% { transform: translateY(0); }
  100% { transform: translateY(256rpx); }
}
.scan-skeleton {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
  padding: 16rpx;
  opacity: 0.3;
}
.scan-bar { height: 8rpx; border-radius: 999rpx; background: #c3c6d7; }
.scan-bar.w-75 { width: 75%; }
.scan-bar.w-full { width: 100%; }
.scan-bar.w-50 { width: 50%; }
.scan-bar.w-85 { width: 83%; }
.scan-bar.w-66 { width: 66%; }
.parsing-title { color: #0b1c30; font-size: 40rpx; font-weight: 600; line-height: 1.4; }
.parsing-desc { margin-top: 16rpx; color: #434655; font-size: 32rpx; line-height: 1.5; }
.parsing-track { width: 100%; height: 6rpx; margin-top: 48rpx; overflow: hidden; border-radius: 999rpx; background: #e5eeff; }
.parsing-bar { width: 65%; height: 100%; background: #004ac6; animation: pulse 1.6s ease-in-out infinite alternate; }
@keyframes pulse { from { width: 35%; } to { width: 85%; } }
.parsing-tag { margin-top: 32rpx; color: #004ac6; font-size: 24rpx; font-weight: 500; letter-spacing: 0.12em; text-transform: uppercase; }

.state-wrap { padding: 24rpx 0; }
.state-actions { display: flex; flex-direction: column; gap: 16rpx; width: 100%; margin-top: 12rpx; }

.upload-grid { display: grid; gap: 48rpx; width: 100%; max-width: 1152px; margin: 0 auto; }
.left-column { display: flex; flex-direction: column; gap: 48rpx; }

.drop-zone {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 520rpx;
  padding: 96rpx 48rpx;
  border: 4rpx dashed #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  text-align: center;
  transition: border-color 0.2s ease, background 0.2s ease;
}
.drop-zone:hover, .drop-zone.active {
  border-color: #004ac6;
  background: #e5eeff;
}
.upload-icon-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 160rpx;
  height: 160rpx;
  margin-bottom: 48rpx;
  border-radius: 999rpx;
  background: #dbe1ff;
  transition: transform 0.2s ease;
}
.drop-zone:hover .upload-icon-wrap { transform: scale(1.1); }
.upload-title { color: #0b1c30; font-size: 40rpx; font-weight: 600; line-height: 1.4; }
.upload-link { color: #004ac6; }
.upload-desc { margin-top: 16rpx; color: #434655; font-size: 32rpx; font-weight: 400; line-height: 1.5; }
.progress-card { width: 100%; max-width: 520rpx; margin-top: 64rpx; }
.progress-head {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16rpx;
  color: #004ac6;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}
.progress-head text:last-child { font-size: 24rpx; font-weight: 500; }
.progress-track { height: 8rpx; overflow: hidden; border-radius: 999rpx; background: #e5eeff; }
.progress-value { height: 100%; background: #004ac6; transition: width 0.3s ease; }

.selected-file {
  display: flex;
  align-items: center;
  gap: 24rpx;
  width: 100%;
  max-width: 560rpx;
  padding: 32rpx;
  border: 2rpx solid #004ac6;
  border-radius: 20rpx;
  background: #fff;
  box-shadow: 0 8rpx 24rpx rgba(0, 74, 198, 0.1);
  text-align: left;
}
.file-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 96rpx;
  height: 96rpx;
  border-radius: 16rpx;
  background: #dbe1ff;
}
.file-info { flex: 1; display: flex; flex-direction: column; gap: 10rpx; min-width: 0; }
.file-name {
  color: #0b1c30;
  font-size: 32rpx;
  font-weight: 600;
  line-height: 1.3;
  word-break: break-all;
}
.file-status { display: flex; align-items: center; gap: 8rpx; color: #006242; font-size: 24rpx; line-height: 1.3; }
.reselect-btn { margin-top: 32rpx; gap: 8rpx; }

.warning-card {
  display: flex;
  gap: 32rpx;
  align-items: flex-start;
  border: 2rpx solid rgba(186, 26, 26, 0.2);
  border-radius: 16rpx;
  padding: 48rpx;
  background: rgba(255, 218, 214, 0.2);
}
.warning-copy { flex: 1; display: flex; flex-direction: column; gap: 8rpx; }
.warning-title { color: #93000a; font-size: 28rpx; font-weight: 600; line-height: 1.14; }
.warning-text { color: #93000a; font-size: 28rpx; font-weight: 400; line-height: 1.43; }

.glass-panel {
  position: relative;
  overflow: hidden;
  height: 100%;
  border: 2rpx solid #e2e8f0;
  border-radius: 16rpx;
  padding: 64rpx;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
}
.panel-content { position: relative; z-index: 1; display: flex; flex-direction: column; gap: 48rpx; }
.panel-title-row { display: flex; align-items: center; gap: 16rpx; }
.panel-title { color: #0b1c30; font-size: 40rpx; font-weight: 600; line-height: 1.4; }
.panel-desc { color: #434655; font-size: 32rpx; font-weight: 400; line-height: 1.6; }
.check-list { display: flex; flex-direction: column; gap: 32rpx; }
.check-item { display: flex; align-items: center; gap: 24rpx; color: #0b1c30; font-size: 28rpx; font-weight: 600; line-height: 1.14; }
.quote-box {
  border: 2rpx solid rgba(0, 74, 198, 0.1);
  border-radius: 16rpx;
  padding: 32rpx;
  background: rgba(0, 74, 198, 0.05);
  color: #004ac6;
  font-size: 24rpx;
  font-weight: 500;
  font-style: italic;
  line-height: 1.33;
}
.demo-link { color: #737686; font-size: 24rpx; text-decoration: underline; }

.action-area {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  width: 100%;
  max-width: 1152px;
  margin: 80rpx auto 0;
  border-top: 2rpx solid #c3c6d7;
  padding-top: 48rpx;
}
.security { display: flex; align-items: center; gap: 16rpx; color: #434655; font-size: 24rpx; font-weight: 500; line-height: 1.33; }
.action-buttons { display: flex; gap: 32rpx; }

@media (min-width: 768px) {
  .upload-container { gap: 24px; padding-top: 24px; padding-bottom: 40px; }
  .upload-grid { grid-template-columns: 8fr 4fr; gap: 24px; }
  .left-column { gap: 24px; }
  .drop-zone { padding: 48px 32px; min-height: 360px; border-radius: 12px; }
  .upload-icon-wrap { width: 80px; height: 80px; margin-bottom: 24px; }
  .upload-title { font-size: 20px; line-height: 28px; }
  .upload-desc { font-size: 16px; line-height: 24px; }
  .progress-card { margin-top: 32px; }
  .progress-head { font-size: 14px; margin-bottom: 8px; }
  .progress-head text:last-child { font-size: 12px; }
  .warning-card { padding: 24px; border-radius: 12px; gap: 16px; }
  .warning-title, .warning-text { font-size: 14px; }
  .glass-panel { padding: 32px; border-radius: 12px; }
  .panel-content { gap: 24px; }
  .panel-title { font-size: 20px; line-height: 28px; }
  .panel-desc { font-size: 16px; line-height: 24px; }
  .check-list { gap: 16px; }
  .check-item { font-size: 14px; gap: 12px; }
  .quote-box { padding: 16px; font-size: 12px; }
  .action-area { flex-direction: row; align-items: center; justify-content: space-between; margin-top: 40px; padding-top: 24px; gap: 24px; }
  .action-buttons { gap: 16px; }
  .security { font-size: 12px; }
  .parsing-title { font-size: 20px; }
  .parsing-desc { font-size: 16px; }
  .state-actions { flex-direction: row; flex-wrap: wrap; justify-content: center; }
  .state-actions .flow-btn { flex: none; min-width: 220rpx; }
}
</style>
