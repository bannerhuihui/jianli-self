<template>
  <view class="page candidate-flow-page interview-page">
    <AppTopNav active="求职者流程" />

    <view class="container flow-container">
      <ProgressSteps v-bind="createFlowStepsProps(CANDIDATE_FLOW, 2)" navigable />
    </view>

    <view class="mode-bar">
      <view class="mode-switch">
        <button class="flow-segment" :class="{ 'flow-segment--active': mode === 'chat' }" @tap="mode = 'chat'">文字聊天</button>
        <button class="flow-segment" :class="{ 'flow-segment--active': mode === 'voice' }" @tap="mode = 'voice'">语音模式</button>
      </view>
    </view>

    <view v-if="mode === 'chat'" class="interview-shell">
      <aside class="progress-panel glass-panel">
        <view class="panel-title-row">
          <AppIcon name="format_list_numbered" :size="18" color="#004ac6" />
          <text class="panel-title primary">面试进度 ({{ currentStep }}/{{ totalSteps }})</text>
        </view>
        <view class="progress-list">
          <view v-for="item in stepItems" :key="item.name" class="progress-item" :class="item.state">
            <view class="progress-dot">{{ item.state === 'done' ? '✓' : item.index }}</view>
            <text class="progress-name">{{ item.name }}</text>
          </view>
        </view>
        <view class="time-box">
          <view class="time-row">
            <text>剩余预计时长</text>
            <text class="time-value">{{ estimatedMinutes }} 分钟</text>
          </view>
          <view class="small-track"><view class="small-value" :style="{ width: `${Math.round(interviewProgress * 100)}%` }" /></view>
        </view>
      </aside>

      <section class="chat-main">
        <StatePanel
          v-if="isLoadingSession"
          tone="info"
          icon="hourglass_top"
          icon-color="#004ac6"
          title="正在加载访谈会话"
          description="请稍候，我们正在同步你的历史问答。"
        />
        <StatePanel
          v-else-if="showLowConfidence"
          tone="warning"
          icon="info"
          icon-color="#b45309"
          title="信息仍不够完整"
          description="当前回答完整度偏低，继续补充可提升画像置信度；也可先结束访谈，后续在人才画像页继续补充。"
        />

        <view class="chat-card">
          <scroll-view scroll-y class="chat-history">
            <view v-for="message in messages" :key="message.id" class="message-row" :class="message.role">
              <view class="avatar" :class="message.role">
                <AppIcon
                  :name="message.role === 'ai' ? 'smart_toy' : 'person'"
                  :size="20"
                  :color="message.role === 'ai' ? '#004ac6' : '#565e74'"
                />
              </view>
              <view class="bubble" :class="[message.role, { thinking: message.thinking }]">
                <text>{{ message.content }}</text>
                <text v-if="message.thinking" class="thinking-dots">
                  <text class="dot dot-1">.</text>
                  <text class="dot dot-2">.</text>
                  <text class="dot dot-3">.</text>
                </text>
              </view>
            </view>
          </scroll-view>

          <view class="chat-input-area">
            <view class="input-stack">
              <view class="textarea-wrap">
                <textarea
                  class="answer-input"
                  :value="draftAnswer"
                  placeholder="输入您的回答..."
                  :disabled="isGenerating"
                  :maxlength="MAX_INPUT_CHARS"
                  @input="onDraftInput"
                />
                <text class="char-count">{{ charCount }}/{{ MAX_INPUT_CHARS }}</text>
              </view>
              <view class="input-actions">
                <view class="input-actions-left">
                  <button class="flow-btn flow-btn--secondary" :disabled="isGenerating" @tap="skipQuestion">跳过此题</button>
                </view>
                <view class="input-actions-right">
                  <button class="flow-btn flow-btn--primary" :disabled="isGenerating" @tap="sendAnswer">
                    <text>发送回答</text>
                    <AppIcon name="send" :size="18" color="#ffffff" />
                  </button>
                </view>
              </view>
            </view>
          </view>
        </view>

        <view class="quick-actions">
          <button class="flow-btn flow-btn--danger" @tap="abortInterview">中止面试</button>
          <button class="flow-btn flow-btn--success" :disabled="isGenerating || !canGenerateProfile" @tap="finishInterview">
            <AppIcon name="analytics" :size="18" color="#ffffff" />
            <text>
              {{ isGenerating ? '正在生成画像...' : (canGenerateProfile ? '结束面试并生成画像' : '继续回答以补全证据') }}
            </text>
          </button>
        </view>
      </section>

      <aside class="insight-panel">
        <view class="glass-panel insight-card">
          <view class="panel-title-row">
            <AppIcon name="track_changes" :size="18" color="#004ac6" />
            <text class="panel-title">目标胜任力标签（{{ stage }}）</text>
          </view>
          <view class="tag-list">
            <text
              v-for="item in (missingEvidence.length ? missingEvidence : ['证据完整度已满足，可进入画像'])"
              :key="item"
              class="competency-pill"
              :class="{ active: missingEvidence.length > 0 }"
            >
              {{ item }}
            </text>
          </view>
        </view>

        <view class="glass-panel feedback-card">
          <view class="panel-title-row">
            <AppIcon name="search_insights" :size="18" color="#004ac6" />
            <text class="panel-title">实时反馈建议</text>
          </view>
          <text class="feedback-desc">AI 正在根据你的回答补全证据链。建议增加：</text>
          <view class="suggestion-list">
            <view v-for="item in suggestions" :key="item" class="suggestion-item">
              <AppIcon name="add_circle" :size="16" color="#004ac6" />
              <text>{{ item }}</text>
            </view>
          </view>
        </view>

        <view class="score-card">
          <view class="score-head">
            <text>回答完整度评分</text>
            <text class="score-value">{{ completenessScore }}%</text>
          </view>
          <view class="score-track">
            <view class="score-progress" :style="{ width: `${completenessScore}%` }" />
          </view>
          <text class="score-tip">提示：补充更多实施细节可提升评估置信度</text>
        </view>
      </aside>
    </view>

    <view v-else class="voice-shell">
      <section class="voice-left">
        <view class="glass-panel ai-avatar-card">
          <view class="ai-avatar-wrap">
            <view class="ai-avatar">
              <AppIcon name="smart_toy" :size="48" color="#ffffff" filled />
            </view>
            <view class="waveform">
              <view
                v-for="(delay, index) in waveformDelays"
                :key="index"
                class="waveform-bar"
                :style="{ animationDelay: `${delay}s` }"
              />
            </view>
            <text class="listening-text">AI 正在倾听并分析您的逻辑表达能力</text>
          </view>
        </view>

        <view class="question-card">
          <view class="question-label-row">
            <AppIcon name="quiz" :size="16" color="#004ac6" />
            <text class="question-label">当前问题 (Q{{ currentStep }}/{{ totalSteps }})</text>
          </view>
          <text class="question-text">请结合您的项目经历，谈谈您在团队合作中遇到冲突时是如何处理的？</text>
        </view>
      </section>

      <section class="voice-center">
        <view class="video-card">
          <image class="video-feed" :src="candidateVideoUrl" mode="aspectFill" />
          <view class="rec-badge">
            <view class="rec-dot" />
            <text>REC 08:42</text>
          </view>
          <view class="control-bar">
            <button class="voice-control mic-control" :class="{ muted: isMuted }" @tap="toggleMic">
              <AppIcon :name="isMuted ? 'mic_off' : 'mic'" :size="24" :color="isMuted ? '#ba1a1a' : '#ffffff'" />
            </button>
            <button class="voice-control end-control" @tap="abortInterview">结束面试</button>
            <button class="voice-control next-control">
              <AppIcon name="arrow_forward" :size="24" color="#ffffff" />
            </button>
          </view>
        </view>
      </section>

      <section class="voice-right">
        <view class="glass-panel transcript-panel">
          <view class="transcript-head">
            <view class="panel-title-row">
              <AppIcon name="notes" :size="18" color="#0b1c30" />
              <text class="panel-title">实时转录预览</text>
            </view>
            <text class="live-badge">LIVE</text>
          </view>
          <scroll-view scroll-y class="transcript-list">
            <view
              v-for="item in transcripts"
              :key="item.time"
              class="transcript-item"
              :class="item.role"
            >
              <text class="transcript-time">{{ item.time }}</text>
              <text class="transcript-text">{{ item.text }}</text>
            </view>
          </scroll-view>
          <view class="confidence-box">
            <view class="score-head">
              <text>置信度分析</text>
              <text class="score-value">94%</text>
            </view>
            <view class="score-track"><view class="confidence-progress" /></view>
          </view>
        </view>
      </section>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import {
  ApiClientError,
  completeInterview,
  confirmStructuredResume,
  ensureActiveJourneyId,
  getInterviewSession,
  startProfileGeneration,
  submitInterviewTurn,
  waitForTask,
  skipInterviewQuestion,
} from '@ai-talent-agent/api';
import { CANDIDATE_FLOW } from '../../../constants/flows';
import { createFlowStepsProps } from '../../../utils/flow-steps';
import AppIcon from '../../../components/AppIcon.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { showToast } from '../../../utils/feedback';

const mode = ref<'chat' | 'voice'>('chat');
const MAX_INPUT_CHARS = 2000;
const draftAnswer = ref('');
const isGenerating = ref(false);
const isLoadingSession = ref(false);
const journeyId = ref('');
const stage = ref('experience_exploration');
const missingEvidence = ref<string[]>([]);
const canGenerateProfile = ref(false);
const interviewProgress = ref(0);
const answeredCount = ref(2);
const skippedCount = ref(0);
const totalSteps = 10;
const currentStep = computed(() => {
  const byProgress = Math.ceil(interviewProgress.value * totalSteps);
  const byTurns = answeredCount.value + skippedCount.value + 1;
  return Math.min(totalSteps, Math.max(1, Math.max(byProgress, byTurns)));
});
const estimatedMinutes = computed(() => Math.max(1, Math.ceil((1 - interviewProgress.value) * 20)));

const stepNames = ['自我介绍', '核心技能考核', '团队冲突处理', '项目难点攻克', '未来职业规划'];
const stepItems = computed(() => stepNames.map((name, index) => {
  const stepNo = index + 1;
  let state = 'todo';
  if (stepNo < currentStep.value) state = 'done';
  else if (stepNo === currentStep.value) state = 'active';
  else if (stepNo === stepNames.length) state = 'todo faded';
  return { index: stepNo, name, state };
}));

const charCount = computed(() => draftAnswer.value.length);
const completenessScore = computed(() => Math.min(95, 48 + answeredCount.value * 12 - skippedCount.value * 8));
const showLowConfidence = computed(() => completenessScore.value < 65 && answeredCount.value + skippedCount.value >= 2);

type ChatMessage = { id: string; role: 'ai' | 'user'; content: string; thinking?: boolean };
const messages = ref<ChatMessage[]>([]);

const suggestions = ['如何量化评估方案优劣的细节', '最终落地后的业务反馈数据'];

function onDraftInput(event: any) {
  draftAnswer.value = String(event.detail.value ?? '').slice(0, MAX_INPUT_CHARS);
}

function pushMessage(role: 'ai' | 'user', content: string, options: { thinking?: boolean } = {}) {
  messages.value.push({ id: `m-${Date.now()}-${Math.random().toString(16).slice(2, 8)}`, role, content, thinking: options.thinking });
}

function hydrateFromSession(session: {
  turns: Array<{ id: string; role: string; content: string }>;
  canGenerateProfile: boolean;
  missingEvidence: string[];
  stage: string;
  progress?: number;
}) {
  messages.value = session.turns.map((turn) => ({
    id: turn.id,
    role: turn.role === 'agent' ? 'ai' : 'user',
    content: turn.content,
    thinking: false,
  }));
  answeredCount.value = session.turns.filter((turn) => turn.role === 'user').length;
  skippedCount.value = 0;
  canGenerateProfile.value = session.canGenerateProfile;
  missingEvidence.value = session.missingEvidence;
  stage.value = session.stage;
  interviewProgress.value = Math.max(0, Math.min(1, session.progress ?? 0));
}

async function initInterview() {
  isLoadingSession.value = true;
  try {
    journeyId.value = await ensureActiveJourneyId();
    try {
      await confirmStructuredResume(journeyId.value);
    } catch (error) {
      if (!(error instanceof ApiClientError) || error.code !== 'JOURNEY_STATE_INVALID') {
        throw error;
      }
    }
    const session = await getInterviewSession(journeyId.value);
    hydrateFromSession(session);
  } catch (error) {
    const message = error instanceof ApiClientError ? error.message : '加载访谈失败';
    showToast(message, 'error');
  } finally {
    isLoadingSession.value = false;
  }
}

onMounted(() => {
  initInterview();
});

async function sendAnswer() {
  const text = draftAnswer.value.trim().slice(0, MAX_INPUT_CHARS);
  if (!text) {
    showToast('请先输入回答');
    return;
  }
  if (!journeyId.value) {
    showToast('旅程未初始化', 'error');
    return;
  }
  isGenerating.value = true;
  draftAnswer.value = '';
  pushMessage('user', text);
  pushMessage('ai', 'AI 正在思考中...', { thinking: true });
  const thinkingId = messages.value[messages.value.length - 1]?.id;
  try {
    const response = await submitInterviewTurn(journeyId.value, text);
    if (thinkingId) {
      const idx = messages.value.findIndex((item) => item.id === thinkingId);
      if (idx >= 0) {
        messages.value[idx] = {
          ...messages.value[idx],
          content: response.agentTurn.content,
          thinking: false,
        };
      } else {
        pushMessage('ai', response.agentTurn.content);
      }
    } else {
      pushMessage('ai', response.agentTurn.content);
    }
    answeredCount.value += 1;
    canGenerateProfile.value = response.canGenerateProfile;
    missingEvidence.value = response.missingEvidence;
    stage.value = response.stage;
    interviewProgress.value = Math.max(interviewProgress.value, Math.min(1, currentStep.value / totalSteps));
  } catch (error) {
    if (thinkingId) {
      messages.value = messages.value.filter((item) => item.id !== thinkingId);
    }
    const message = error instanceof ApiClientError ? error.message : '发送失败';
    showToast(message, 'error');
  } finally {
    isGenerating.value = false;
  }
}

async function skipQuestion() {
  if (!journeyId.value) {
    showToast('旅程未初始化', 'error');
    return;
  }
  isGenerating.value = true;
  try {
    const session = await skipInterviewQuestion(journeyId.value);
    skippedCount.value += 1;
    hydrateFromSession(session);
    showToast('已跳过，置信度可能降低');
  } catch (error) {
    const message = error instanceof ApiClientError ? error.message : '跳过失败';
    showToast(message, 'error');
  } finally {
    isGenerating.value = false;
  }
}

function abortInterview() {
  uni.showModal({
    title: '中止面试',
    content: '确定要中止当前访谈吗？已回答内容会保留，可稍后继续。',
    success: (res) => {
      if (res.confirm) {
        showToast('访谈已暂停');
        uni.navigateBack({ fail: () => uni.navigateTo({ url: '/pages/candidate/review/index' }) });
      }
    },
  });
}

async function finishInterview() {
  if (!journeyId.value) {
    showToast('旅程未初始化', 'error');
    return;
  }
  if (completenessScore.value < 55) {
    uni.showModal({
      title: '信息可能不足',
      content: '当前回答完整度偏低，画像置信度可能不足。是否仍要结束并生成？',
      confirmText: '继续生成',
      success: async (res) => {
        if (res.confirm) await navigateToProfile();
      },
    });
    return;
  }
  await navigateToProfile();
}

async function navigateToProfile() {
  isGenerating.value = true;
  let ok = false;
  try {
    uni.showLoading({ title: '生成画像中', mask: true });
    await completeInterview(journeyId.value);
    const accepted = await startProfileGeneration(journeyId.value);
    const task = await waitForTask(accepted.taskId, { timeoutMs: 180000 });
    if (task.status === 'failed') {
      throw new ApiClientError(task.error?.code ?? 'TASK_FAILED', task.error?.message ?? '画像生成失败');
    }
    ok = true;
    showToast('人才画像已生成', 'success');
  } catch (error) {
    const message = error instanceof ApiClientError ? error.message : '画像生成失败';
    showToast(message, 'error');
  } finally {
    uni.hideLoading();
  }
  isGenerating.value = false;
  if (ok) uni.navigateTo({ url: '/pages/candidate/profile/index' });
}

const candidateVideoUrl =
  'https://lh3.googleusercontent.com/aida/AP1WRLslYQy0FstfrxwfCQaEMu8a_EABBVxgkstuYhtj_BsEp6tfAsEOlHurc9xNFX5d81zRarx6fV-7wZUrQnQyUJObCFPcA5HFWCmmKbuY3-y7bAEX2AUqpSlXY0mUEM4EHtQ6bisMtCAg_Sj0RXLnQ942tceusj2N6NpkLBjh00T3GKjIBiPQZpyq2rsvpfLIeM_tDGUg2Wo1e_3gofC3rCFfJTmh-Ml0Gn1KFkhmE2lbePhA7V03b4qeb2I';
const waveformDelays = [0.1, 0.3, 0.2, 0.5, 0.4, 0.6];
const isMuted = ref(false);

const transcripts = [
  {
    time: '08:35',
    role: 'interviewer',
    text: '面试官: 好的，接下来请结合您的项目经历，谈谈您在团队合作中遇到冲突时是如何处理的？',
  },
  {
    time: '08:40',
    role: 'candidate',
    text: '在我上一个负责的电商平台重构项目中，曾与技术负责人就架构选择产生了分歧。我主张使用微服务架构以应对未来的流量增长...',
  },
  {
    time: '08:42',
    role: 'recognizing',
    text: '(正在识别中...) 冲突发生时，我首先组织了一个技术评审会，将两种方案的优劣进行量化对比...',
  },
];

function toggleMic() {
  isMuted.value = !isMuted.value;
  showToast(isMuted.value ? '麦克风已静音' : '麦克风已开启');
}
</script>

<style lang="scss" scoped>
.flow-container { padding-bottom: 16rpx; }

.mode-bar {
  display: flex;
  justify-content: flex-end;
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 32rpx 16rpx;
}
.mode-switch {
  display: flex;
  gap: 8rpx;
  padding: 8rpx;
  border-radius: 12rpx;
  background: #e5eeff;
}
.interview-shell {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  max-width: 1440px;
  margin: 0 auto;
  padding: 32rpx;
  overflow: hidden;
}

.glass-panel {
  border: 2rpx solid rgba(226, 232, 240, 0.8);
  border-radius: 16rpx;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(12px);
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
}

.progress-panel {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
  padding: 48rpx;
}
.panel-title-row {
  display: flex;
  align-items: center;
  gap: 16rpx;
}
.panel-title {
  color: #0b1c30;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}
.panel-title.primary { color: #004ac6; }

.progress-list { display: flex; flex-direction: column; gap: 32rpx; }
.progress-item { display: flex; align-items: center; gap: 24rpx; }
.progress-item.done .progress-name { color: #565e74; text-decoration: line-through; }
.progress-item.active .progress-name { color: #0b1c30; font-weight: 600; }
.progress-item.faded { opacity: 0.5; }
.progress-dot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48rpx;
  height: 48rpx;
  border-radius: 999rpx;
  background: #d3e4fe;
  color: #565e74;
  font-size: 20rpx;
  font-weight: 600;
}
.progress-item.done .progress-dot { background: #007d55; color: #fff; }
.progress-item.active .progress-dot { background: #004ac6; color: #fff; }
.progress-name { color: #434655; font-size: 28rpx; line-height: 1.43; }

.time-box {
  margin-top: 16rpx;
  padding-top: 48rpx;
  border-top: 2rpx solid #c3c6d7;
}
.time-row {
  display: flex;
  justify-content: space-between;
  color: #565e74;
  font-size: 24rpx;
  line-height: 1.33;
}
.time-value { color: #0b1c30; font-weight: 600; }
.small-track {
  height: 12rpx;
  margin-top: 16rpx;
  border-radius: 999rpx;
  background: #c3c6d7;
  overflow: hidden;
}
.small-value { height: 100%; background: #004ac6; border-radius: 999rpx; transition: width 0.35s ease; }

.chat-main {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
  min-height: calc(100vh - 320rpx);
}
.chat-card {
  display: flex;
  flex: 1;
  flex-direction: column;
  overflow: hidden;
  min-height: 640rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  background: #fff;
  box-shadow: 0 4rpx 16rpx rgba(15, 23, 42, 0.04);
}
.chat-history {
  flex: 1;
  height: 480rpx;
  padding: 48rpx;
  box-sizing: border-box;
}
.message-row {
  display: flex;
  align-items: flex-start;
  gap: 32rpx;
  max-width: 85%;
  margin-bottom: 48rpx;
}
.message-row.user {
  margin-left: auto;
  flex-direction: row-reverse;
}
.avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  width: 80rpx;
  height: 80rpx;
  border-radius: 999rpx;
}
.avatar.ai { background: #2563eb; }
.avatar.user { background: #dae2fd; }
.bubble {
  padding: 32rpx;
  border-radius: 16rpx;
  font-size: 32rpx;
  line-height: 1.5;
}
.bubble.ai {
  border: 2rpx solid #c3c6d7;
  border-bottom-left-radius: 4rpx;
  background: #eff4ff;
  color: #0b1c30;
}
.bubble.user {
  border-bottom-right-radius: 4rpx;
  background: #004ac6;
  color: #fff;
}
.bubble.thinking {
  color: #2563eb;
}
.thinking-dots {
  display: inline-flex;
  margin-left: 6rpx;
  color: #2563eb;
}
.thinking-dots .dot {
  display: inline-block;
  width: 10rpx;
  animation: dot-bounce 1.2s infinite ease-in-out;
}
.thinking-dots .dot-1 { animation-delay: 0s; }
.thinking-dots .dot-2 { animation-delay: 0.2s; }
.thinking-dots .dot-3 { animation-delay: 0.4s; }
@keyframes dot-bounce {
  0%, 80%, 100% { opacity: 0.2; transform: translateY(0); }
  40% { opacity: 1; transform: translateY(-4rpx); }
}

.chat-input-area {
  padding: 32rpx;
  border-top: 2rpx solid #c3c6d7;
  background: #fff;
}
.input-stack { display: flex; flex-direction: column; gap: 24rpx; }
.textarea-wrap { position: relative; }
.answer-input {
  display: block;
  width: 100%;
  min-height: 200rpx;
  padding: 24rpx 32rpx 72rpx;
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  box-sizing: border-box;
  color: #0b1c30;
  font-size: 32rpx;
  line-height: 1.5;
  background: #fff;
}
.char-count {
  position: absolute;
  right: 24rpx;
  bottom: 24rpx;
  color: #565e74;
  font-size: 24rpx;
  line-height: 1.33;
}
.input-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24rpx;
}
.input-actions-left,
.input-actions-right {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: flex-end;
  gap: 24rpx;
}
.insight-panel { display: flex; flex-direction: column; gap: 48rpx; }
.insight-card,
.feedback-card { padding: 48rpx; }
.feedback-card { border-left: 8rpx solid #004ac6; }
.tag-list { display: flex; flex-wrap: wrap; gap: 16rpx; margin-top: 32rpx; }
.competency-pill {
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  background: #d3e4fe;
  color: #565e74;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}
.competency-pill.active {
  background: rgba(0, 74, 198, 0.1);
  color: #004ac6;
}
.feedback-desc {
  display: block;
  margin-top: 24rpx;
  color: #434655;
  font-size: 24rpx;
  line-height: 1.5;
}
.suggestion-list { display: flex; flex-direction: column; gap: 24rpx; margin-top: 32rpx; }
.suggestion-item {
  display: flex;
  align-items: flex-start;
  gap: 16rpx;
  color: #0b1c30;
  font-size: 28rpx;
  line-height: 1.43;
}
.score-card {
  border: 2rpx solid #c3c6d7;
  border-radius: 16rpx;
  padding: 48rpx;
  background: #eff4ff;
}
.score-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #565e74;
  font-size: 24rpx;
  line-height: 1.33;
}
.score-value { color: #004ac6; font-weight: 700; }
.score-track {
  height: 12rpx;
  margin-top: 24rpx;
  border-radius: 999rpx;
  background: #c3c6d7;
  overflow: hidden;
}
.score-progress { height: 100%; background: #004ac6; border-radius: 999rpx; transition: width 0.5s ease; }
.score-tip { display: block; margin-top: 16rpx; color: #434655; font-size: 22rpx; line-height: 1.4; }

/* Voice mode */
.voice-shell {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  max-width: 1440px;
  margin: 0 auto;
  padding: 32rpx;
}
.voice-left,
.voice-center,
.voice-right {
  display: flex;
  flex-direction: column;
  gap: 48rpx;
  min-width: 0;
}

.ai-avatar-card {
  position: relative;
  overflow: hidden;
  min-height: 600rpx;
  padding: 48rpx;
}
.ai-avatar-wrap {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 48rpx;
}
.ai-avatar {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 192rpx;
  height: 192rpx;
  border-radius: 999rpx;
  background: #2563eb;
  box-shadow: 0 16rpx 32rpx rgba(0, 74, 198, 0.2);
}
.waveform {
  display: flex;
  align-items: flex-end;
  gap: 8rpx;
  height: 64rpx;
}
.waveform-bar {
  width: 12rpx;
  height: 16rpx;
  border-radius: 999rpx;
  background: #004ac6;
  animation: waveform 1.2s ease-in-out infinite;
}
@keyframes waveform {
  0%, 100% { height: 16rpx; }
  50% { height: 64rpx; }
}
.listening-text {
  color: #004ac6;
  font-size: 28rpx;
  font-weight: 600;
  text-align: center;
  animation: pulse-text 2s ease-in-out infinite;
}
@keyframes pulse-text {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.65; }
}

.question-card {
  border: 2rpx solid rgba(0, 74, 198, 0.2);
  border-radius: 16rpx;
  padding: 48rpx;
  background: #d3e4fe;
}
.question-label-row {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 16rpx;
}
.question-label { color: #004ac6; font-size: 24rpx; font-weight: 500; line-height: 1.33; }
.question-text { color: #0b1c30; font-size: 40rpx; font-weight: 600; line-height: 1.4; }

.video-card {
  position: relative;
  overflow: hidden;
  flex: 1;
  min-height: 1000rpx;
  border-radius: 16rpx;
  background: #0b1c30;
  box-shadow: 0 24rpx 48rpx rgba(11, 28, 48, 0.24);
}
.video-feed {
  width: 100%;
  height: 100%;
  min-height: 1000rpx;
  opacity: 0.9;
}
.rec-badge {
  position: absolute;
  left: 32rpx;
  top: 32rpx;
  z-index: 2;
  display: flex;
  align-items: center;
  gap: 16rpx;
  border-radius: 999rpx;
  padding: 8rpx 24rpx;
  background: rgba(186, 26, 26, 0.8);
  backdrop-filter: blur(12px);
  color: #fff;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}
.rec-dot {
  width: 16rpx;
  height: 16rpx;
  border-radius: 999rpx;
  background: #fff;
  animation: rec-ping 1.5s ease-in-out infinite;
}
@keyframes rec-ping {
  0%, 100% { opacity: 1; transform: scale(1); }
  50% { opacity: 0.5; transform: scale(0.85); }
}
.control-bar {
  position: absolute;
  left: 50%;
  bottom: 32rpx;
  z-index: 2;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 32rpx;
  border: 2rpx solid rgba(255, 255, 255, 0.1);
  border-radius: 999rpx;
  padding: 24rpx 48rpx;
  background: rgba(11, 28, 48, 0.4);
  backdrop-filter: blur(24px);
}
.voice-control {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0;
  padding: 0;
  border: none;
  border-radius: 999rpx;
  color: #fff;
  font-weight: 600;
  &::after { border: none; }
}
.mic-control,
.next-control {
  width: 96rpx;
  height: 96rpx;
  background: rgba(255, 255, 255, 0.1);
}
.mic-control.muted { background: rgba(255, 255, 255, 0.16); }
.next-control { background: #004ac6; }
.end-control {
  min-height: 96rpx;
  padding: 0 64rpx;
  background: #ba1a1a;
  font-size: 28rpx;
}

.transcript-panel {
  display: flex;
  flex-direction: column;
  overflow: hidden;
  max-height: 1300rpx;
}
.transcript-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
  padding: 24rpx 32rpx;
  border-bottom: 2rpx solid #c3c6d7;
  border-radius: 16rpx 16rpx 0 0;
  background: #eff4ff;
}
.live-badge {
  border-radius: 8rpx;
  padding: 4rpx 16rpx;
  background: #6ffbbe;
  color: #005236;
  font-size: 24rpx;
  font-weight: 500;
  line-height: 1.33;
}
.transcript-list {
  flex: 1;
  height: 640rpx;
  padding: 48rpx;
  box-sizing: border-box;
}
.transcript-item {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
  margin-bottom: 32rpx;
}
.transcript-item.interviewer .transcript-time { color: #565e74; }
.transcript-item.interviewer .transcript-text {
  color: #434655;
  font-style: italic;
}
.transcript-item.candidate .transcript-time { color: #004ac6; }
.transcript-item.candidate .transcript-text { color: #0b1c30; }
.transcript-item.recognizing {
  opacity: 0.6;
}
.transcript-item.recognizing .transcript-time { color: #004ac6; }
.transcript-item.recognizing .transcript-text {
  padding-left: 16rpx;
  border-left: 4rpx solid #004ac6;
  color: #0b1c30;
  animation: pulse-text 2s ease-in-out infinite;
}
.transcript-time { font-size: 24rpx; font-weight: 500; line-height: 1.33; }
.transcript-text { font-size: 28rpx; line-height: 1.43; }
.confidence-box {
  padding: 24rpx 32rpx;
  border-top: 2rpx solid #c3c6d7;
  border-radius: 0 0 16rpx 16rpx;
  background: #eff4ff;
}
.confidence-box .score-track { height: 8rpx; margin-top: 16rpx; }
.confidence-progress { width: 94%; height: 100%; background: #004ac6; border-radius: 999rpx; }

@media (min-width: 768px) {
  .mode-bar { padding: 0 40px 8px; }
  .interview-shell {
    flex-direction: row;
    align-items: stretch;
    gap: 24px;
    padding: 40px;
    min-height: calc(100vh - 160px);
  }
  .progress-panel { width: 256px; flex-shrink: 0; padding: 24px; }
  .panel-title { font-size: 14px; }
  .progress-name { font-size: 14px; }
  .progress-dot { width: 24px; height: 24px; font-size: 10px; }
  .time-row { font-size: 12px; }
  .chat-main { flex: 1; min-width: 0; min-height: calc(100vh - 200px); }
  .chat-history { height: auto; min-height: 360px; padding: 24px; }
  .message-row { gap: 16px; margin-bottom: 24px; }
  .avatar { width: 40px; height: 40px; }
  .bubble { padding: 16px; font-size: 16px; border-radius: 12px; }
  .input-stack { gap: 12px; }
  .answer-input { min-height: 100px; font-size: 16px; padding: 12px 16px 40px; border-radius: 12px; }
  .char-count { font-size: 12px; right: 12px; bottom: 12px; }
  .quick-actions { gap: 12px; }
  .insight-panel { width: 320px; flex-shrink: 0; gap: 24px; }
  .insight-card, .feedback-card { padding: 24px; }
  .competency-pill { font-size: 12px; }
  .score-card { padding: 24px; }
  .voice-shell {
    flex-direction: row;
    align-items: stretch;
    gap: 24px;
    padding: 40px;
    min-height: calc(100vh - 160px);
  }
  .voice-left {
    flex: 1;
    max-width: 448px;
    gap: 24px;
  }
  .voice-center { flex: 2; min-width: 0; }
  .voice-right {
    flex: 1;
    max-width: 384px;
  }
  .ai-avatar-card { min-height: 300px; padding: 24px; }
  .ai-avatar { width: 96px; height: 96px; }
  .ai-avatar-wrap { gap: 24px; }
  .waveform { height: 32px; }
  .waveform-bar { width: 6px; }
  @keyframes waveform {
    0%, 100% { height: 8px; }
    50% { height: 32px; }
  }
  .listening-text { font-size: 14px; }
  .question-card { padding: 24px; border-radius: 12px; }
  .question-label { font-size: 12px; }
  .question-text { font-size: 20px; line-height: 28px; }
  .video-card { min-height: 500px; border-radius: 12px; }
  .video-feed { min-height: 500px; }
  .rec-badge { left: 16px; top: 16px; font-size: 12px; padding: 4px 12px; }
  .rec-dot { width: 8px; height: 8px; }
  .control-bar { bottom: 16px; gap: 16px; padding: 12px 24px; }
  .mic-control, .next-control { width: 48px; height: 48px; }
  .end-control { min-height: 48px; padding: 0 32px; font-size: 14px; }
  .transcript-panel { max-height: 650px; }
  .transcript-list { height: auto; min-height: 320px; max-height: 480px; padding: 24px; }
  .transcript-time { font-size: 12px; }
  .transcript-text { font-size: 14px; }
  .live-badge { font-size: 12px; }
}
</style>
