<template>
  <view class="page interview-page">
    <AppTopNav active="求职者流程" />

    <view class="container interview-container">
      <ProgressSteps :steps="candidateSteps" :active-index="2" />

      <view class="interview-head">
        <view class="head-copy">
          <text class="eyebrow">候选人 · 第二步</text>
          <text class="title">AI 访谈补齐简历无法表达的信息</text>
          <text class="desc">默认使用文字聊天模式，适合公众号 H5 和企微场景；语音模式用于更接近真实面试的沉浸体验。</text>
        </view>
        <view class="mode-switch">
          <button class="mode-button" :class="{ active: mode === 'chat' }" @tap="mode = 'chat'">文字聊天</button>
          <button class="mode-button" :class="{ active: mode === 'voice' }" @tap="mode = 'voice'">语音模式</button>
        </view>
      </view>

      <view v-if="mode === 'chat'" class="chat-layout">
        <aside class="progress-panel glass-panel">
          <text class="panel-title">面试进度 ({{ currentStep }}/{{ totalSteps }})</text>
          <view class="progress-list">
            <view v-for="item in stepItems" :key="item.name" class="progress-item" :class="item.state">
              <view class="progress-dot">{{ item.state === 'done' ? '✓' : item.index }}</view>
              <text class="progress-name">{{ item.name }}</text>
            </view>
          </view>
          <view class="time-box">
            <view class="time-row"><text>剩余预计时长</text><text>15 分钟</text></view>
            <view class="small-track"><view class="small-value" /></view>
          </view>
        </aside>

        <section class="chat-main">
          <StatePanel
            v-if="showLowConfidence"
            tone="warning"
            icon="info"
            icon-color="#b45309"
            title="信息仍不够完整"
            description="当前回答完整度偏低，继续补充可提升画像置信度；也可先结束访谈，后续在人才画像页继续补充。"
          />

          <view class="chat-card">
            <scroll-view scroll-y class="chat-history">
              <view v-for="message in messages" :key="message.id" class="message-row" :class="message.role">
                <view class="avatar">{{ message.role === 'ai' ? 'AI' : '我' }}</view>
                <view class="bubble">
                  <text>{{ message.content }}</text>
                </view>
              </view>
            </scroll-view>

            <view class="chat-input-area">
              <textarea
                class="answer-input"
                :value="draftAnswer"
                placeholder="输入您的回答..."
                :disabled="isGenerating"
                @input="onDraftInput"
              />
              <view class="input-bottom">
                <text class="char-count">已输入 {{ charCount }} 字</text>
                <view class="input-actions">
                  <button class="skip-button" :disabled="isGenerating" @tap="skipQuestion">跳过此题</button>
                  <button class="send-button" :disabled="isGenerating" @tap="sendAnswer">发送回答</button>
                </view>
              </view>
            </view>
          </view>

          <view class="quick-actions">
            <button class="danger-button" @tap="abortInterview">中止面试</button>
            <button class="finish-button" :disabled="isGenerating" @tap="finishInterview">
              {{ isGenerating ? '正在生成画像...' : '结束面试并生成画像' }}
            </button>
          </view>
        </section>

        <aside class="insight-panel">
          <view class="glass-panel insight-card">
            <text class="panel-title">目标胜任力标签</text>
            <view class="tag-list">
              <AppTag label="团队领导力 (L4)" tone="blue" />
              <AppTag label="技术洞察 (L3)" tone="blue" />
              <AppTag label="冲突解决 (L2)" tone="gray" />
              <AppTag label="抗压能力" tone="gray" />
              <AppTag label="沟通表达" tone="gray" />
            </view>
          </view>

          <view class="glass-panel feedback-card">
            <text class="panel-title">实时反馈建议</text>
            <text class="feedback-desc">AI 正在识别您的“冲突解决”能力。建议补充：</text>
            <view class="suggestion-list">
              <view v-for="item in suggestions" :key="item" class="suggestion-item"><text class="plus">+</text><text>{{ item }}</text></view>
            </view>
          </view>

          <view class="score-card">
            <view class="score-head"><text>回答完整度评分</text><text class="score-value">{{ completenessScore }}%</text></view>
            <view class="score-track"><view class="score-progress" :style="{ width: `${completenessScore}%` }" /></view>
            <text class="score-tip">补充更多实施细节可提升评估置信度</text>
          </view>
        </aside>
      </view>

      <view v-else class="voice-layout">
        <section class="voice-left">
          <view class="glass-panel ai-avatar-card">
            <view class="ai-avatar">AI</view>
            <view class="waveform">
              <view v-for="bar in 8" :key="bar" class="wave-bar" :class="`bar-${bar}`" />
            </view>
            <text class="listening-text">AI 正在倾听并分析您的逻辑表达能力</text>
          </view>
          <view class="question-card">
            <text class="question-label">当前问题 (Q3/10)</text>
            <text class="question-text">请结合您的项目经历，谈谈您在团队合作中遇到冲突时是如何处理的？</text>
          </view>
        </section>

        <section class="voice-center">
          <view class="video-card">
            <view class="rec-badge"><text class="rec-dot" />REC 08:42</view>
            <view class="video-person">
              <view class="person-avatar">候</view>
              <text class="video-title">候选人语音 / 视频面试中</text>
              <text class="video-desc">这里后续可接入真实录音、视频或微信小程序录音能力。</text>
            </view>
            <view class="control-bar">
              <button class="round-control">麦</button>
              <button class="end-control">结束面试</button>
              <button class="round-control next">→</button>
            </view>
          </view>
        </section>

        <section class="voice-right glass-panel">
          <view class="transcript-head">
            <text class="panel-title">实时转录预览</text>
            <AppTag label="LIVE" tone="green" />
          </view>
          <view class="transcript-list">
            <view v-for="item in transcripts" :key="item.time" class="transcript-item" :class="{ active: item.active }">
              <text class="transcript-time">{{ item.time }}</text>
              <text class="transcript-text">{{ item.text }}</text>
            </view>
          </view>
          <view class="confidence-box">
            <view class="score-head"><text>置信度分析</text><text class="score-value">94%</text></view>
            <view class="score-track"><view class="confidence-progress" /></view>
          </view>
        </section>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import AppTag from '../../../components/AppTag.vue';
import AppTopNav from '../../../components/AppTopNav.vue';
import ProgressSteps from '../../../components/ProgressSteps.vue';
import StatePanel from '../../../components/StatePanel.vue';
import { runAsyncAction, showToast, simulateDelay } from '../../../utils/feedback';

const candidateSteps = ['上传简历', '简历校对', 'AI 访谈', '人才画像', '简历生成'];
const mode = ref<'chat' | 'voice'>('chat');
const draftAnswer = ref('我会先把差异拆成成本、风险和交付周期，再通过小范围验证降低争议。');
const isGenerating = ref(false);
const answeredCount = ref(2);
const skippedCount = ref(0);
const totalSteps = 10;
const currentStep = computed(() => Math.min(answeredCount.value + skippedCount.value + 1, totalSteps));

const stepNames = ['自我介绍', '核心技能考核', '团队冲突处理', '项目难点攻克', '未来职业规划'];
const stepItems = computed(() => stepNames.map((name, index) => {
  const stepNo = index + 1;
  let state = 'todo';
  if (stepNo < currentStep.value) state = 'done';
  else if (stepNo === currentStep.value) state = 'active';
  else if (stepNo === stepNames.length) state = 'todo faded';
  return { index: stepNo, name, state };
}));

const charCount = computed(() => draftAnswer.value.trim().length);
const completenessScore = computed(() => Math.min(95, 48 + answeredCount.value * 12 - skippedCount.value * 8));
const showLowConfidence = computed(() => completenessScore.value < 65 && answeredCount.value + skippedCount.value >= 2);

type ChatMessage = { id: string; role: 'ai' | 'user'; content: string };
const messages = ref<ChatMessage[]>([
  { id: 'm1', role: 'ai', content: '好的，接下来请结合您的项目经历，谈谈您在团队合作中遇到冲突时是如何处理的？' },
  { id: 'm2', role: 'user', content: '在我上一个负责的电商平台重构项目中，曾与技术负责人就架构选择产生了分歧。我主张使用微服务架构以应对未来的流量增长，而他担心引入过多复杂度。' },
  { id: 'm3', role: 'ai', content: '这是一个很典型的架构分歧。请具体描述一下你是如何推动讨论并最终达成一致的？在这个过程中你使用了哪些数据或事实来支持你的观点？' },
]);

const followUpQuestions = [
  '请分享一次你在高压交付下如何保持团队节奏的经历。',
  '你未来 3 年的职业方向更偏向技术专家还是管理路线？',
];

const suggestions = ['如何量化评估方案优劣的细节', '最终落地后的业务反馈数据'];

function onDraftInput(event: { detail: { value: string } }) {
  draftAnswer.value = event.detail.value;
}

function pushMessage(role: 'ai' | 'user', content: string) {
  messages.value.push({ id: `m-${Date.now()}`, role, content });
}

function sendAnswer() {
  const text = draftAnswer.value.trim();
  if (!text) {
    showToast('请先输入回答');
    return;
  }
  pushMessage('user', text);
  draftAnswer.value = '';
  answeredCount.value += 1;
  const nextQuestion = followUpQuestions[answeredCount.value - 3];
  if (nextQuestion) {
    pushMessage('ai', nextQuestion);
  } else {
    pushMessage('ai', '感谢补充。如需继续，可回答更多问题；也可结束访谈生成人才画像。');
  }
  showToast('回答已发送', 'success');
}

function skipQuestion() {
  skippedCount.value += 1;
  pushMessage('user', '（跳过此题）');
  pushMessage('ai', '已记录跳过。你可以继续回答下一题，或在信息不足时先结束访谈。');
  showToast('已跳过，置信度可能降低');
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
  const ok = await runAsyncAction(
    () => simulateDelay(1400),
    { loading: '生成画像中', success: '人才画像已生成' },
  );
  isGenerating.value = false;
  if (ok) uni.navigateTo({ url: '/pages/candidate/profile/index' });
}

const transcripts = [
  { time: '08:35', text: '面试官：好的，接下来请结合您的项目经历，谈谈您在团队合作中遇到冲突时是如何处理的？', active: false },
  { time: '08:40', text: '候选人：在我上一个负责的电商平台重构项目中，曾与技术负责人就架构选择产生了分歧。', active: false },
  { time: '08:42', text: '正在识别中：冲突发生时，我首先组织了一个技术评审会，将两种方案的优劣进行量化对比...', active: true },
];
</script>

<style lang="scss" scoped>
.interview-page { min-height: 100vh; background: #f8f9ff; color: #0b1c30; }
.interview-container { display: flex; flex-direction: column; gap: 32rpx; }
.interview-head { display: flex; flex-direction: column; gap: 28rpx; }
.head-copy { display: flex; flex-direction: column; gap: 14rpx; }
.eyebrow { align-self: flex-start; border-radius: 999rpx; padding: 8rpx 18rpx; background: #e5eeff; color: #004ac6; font-size: 24rpx; font-weight: 900; }
.title { color: #0b1c30; font-size: 48rpx; font-weight: 900; line-height: 1.2; }
.desc { color: #565e74; font-size: 28rpx; line-height: 1.7; }
.mode-switch { display: flex; gap: 12rpx; padding: 8rpx; border-radius: 18rpx; background: #e5eeff; }
.mode-button { flex: 1; min-height: 72rpx; border-radius: 14rpx; color: #565e74; background: transparent; font-size: 26rpx; font-weight: 900; }
.mode-button.active { background: #004ac6; color: #fff; box-shadow: 0 8rpx 18rpx rgba(0, 74, 198, 0.18); }
.glass-panel { border: 2rpx solid rgba(226,232,240,0.9); border-radius: 24rpx; background: rgba(255,255,255,0.78); box-shadow: 0 8rpx 40rpx rgba(0,74,198,0.08); }
.chat-layout { display: grid; gap: 28rpx; }
.progress-panel, .insight-card, .feedback-card { padding: 32rpx; }
.panel-title { color: #0b1c30; font-size: 28rpx; font-weight: 900; }
.progress-list { display: flex; flex-direction: column; gap: 24rpx; margin-top: 28rpx; }
.progress-item { display: flex; align-items: center; gap: 18rpx; color: #434655; }
.progress-item.done .progress-name { color: #565e74; text-decoration: line-through; }
.progress-item.active .progress-name { color: #0b1c30; font-weight: 900; }
.progress-item.faded { opacity: 0.5; }
.progress-dot { display: flex; align-items: center; justify-content: center; width: 44rpx; height: 44rpx; border-radius: 999rpx; background: #d3e4fe; color: #565e74; font-size: 22rpx; font-weight: 900; }
.done .progress-dot { background: #007d55; color: #fff; }
.active .progress-dot { background: #004ac6; color: #fff; }
.progress-name { font-size: 26rpx; }
.time-box { margin-top: 40rpx; padding-top: 28rpx; border-top: 2rpx solid #c3c6d7; }
.time-row, .score-head { display: flex; justify-content: space-between; align-items: center; color: #565e74; font-size: 24rpx; }
.small-track, .score-track { overflow: hidden; height: 12rpx; margin-top: 16rpx; border-radius: 999rpx; background: #c3c6d7; }
.small-value { width: 30%; height: 100%; background: #004ac6; }
.chat-main { display: flex; flex-direction: column; gap: 20rpx; }
.chat-card { overflow: hidden; display: flex; flex-direction: column; min-height: 760rpx; border: 2rpx solid #c3c6d7; border-radius: 24rpx; background: #fff; box-shadow: 0 8rpx 26rpx rgba(0,74,198,0.04); }
.chat-history { flex: 1; height: 560rpx; padding: 32rpx; box-sizing: border-box; }
.message-row { display: flex; align-items: flex-end; gap: 18rpx; max-width: 88%; margin-bottom: 32rpx; }
.message-row.user { margin-left: auto; flex-direction: row-reverse; }
.avatar { display: flex; align-items: center; justify-content: center; width: 72rpx; height: 72rpx; border-radius: 999rpx; background: #2563eb; color: #fff; font-size: 22rpx; font-weight: 900; }
.user .avatar { background: #dae2fd; color: #565e74; }
.bubble { border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 24rpx; background: #eff4ff; color: #0b1c30; font-size: 28rpx; line-height: 1.7; }
.user .bubble { border-color: #004ac6; background: #004ac6; color: #fff; }
.chat-input-area { padding: 24rpx; border-top: 2rpx solid #c3c6d7; background: #fff; }
.answer-input { width: 100%; min-height: 160rpx; padding: 22rpx; border: 2rpx solid #c3c6d7; border-radius: 20rpx; box-sizing: border-box; color: #0b1c30; font-size: 28rpx; line-height: 1.6; }
.input-bottom { display: flex; flex-direction: column; gap: 18rpx; margin-top: 18rpx; }
.char-count { color: #565e74; font-size: 24rpx; }
.input-actions { display: flex; gap: 16rpx; }
.skip-button, .send-button, .danger-button, .finish-button { display: flex; align-items: center; justify-content: center; min-height: 72rpx; border-radius: 16rpx; font-size: 26rpx; font-weight: 900; }
.skip-button { flex: 1; border: 2rpx solid #c3c6d7; color: #565e74; background: #fff; }
.send-button { flex: 1; color: #fff; background: #004ac6; }
.quick-actions { display: flex; gap: 16rpx; justify-content: flex-end; }
.danger-button { flex: 1; border: 2rpx solid #ba1a1a; color: #ba1a1a; background: #fff; }
.finish-button { flex: 1; color: #fff; background: #007d55; }
.insight-panel { display: flex; flex-direction: column; gap: 24rpx; }
.tag-list { display: flex; flex-wrap: wrap; gap: 12rpx; margin-top: 22rpx; }
.feedback-card { border-left: 8rpx solid #004ac6; }
.feedback-desc { display: block; margin-top: 16rpx; color: #565e74; font-size: 26rpx; line-height: 1.6; }
.suggestion-list { display: flex; flex-direction: column; gap: 16rpx; margin-top: 20rpx; }
.suggestion-item { display: flex; align-items: flex-start; gap: 12rpx; color: #0b1c30; font-size: 26rpx; line-height: 1.5; }
.plus { color: #004ac6; font-weight: 900; }
.score-card { border: 2rpx solid #c3c6d7; border-radius: 24rpx; padding: 32rpx; background: #eff4ff; }
.score-value { color: #004ac6; font-weight: 900; }
.score-progress { width: 72%; height: 100%; background: #004ac6; }
.score-tip { display: block; margin-top: 12rpx; color: #565e74; font-size: 22rpx; }
.voice-layout { display: grid; gap: 28rpx; }
.voice-left { display: flex; flex-direction: column; gap: 28rpx; }
.ai-avatar-card { position: relative; overflow: hidden; min-height: 360rpx; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 28rpx; padding: 32rpx; }
.ai-avatar { display: flex; align-items: center; justify-content: center; width: 160rpx; height: 160rpx; border-radius: 999rpx; background: #2563eb; color: #fff; font-size: 44rpx; font-weight: 900; box-shadow: 0 16rpx 32rpx rgba(0,74,198,0.2); }
.waveform { display: flex; align-items: center; gap: 8rpx; height: 70rpx; }
.wave-bar { width: 10rpx; border-radius: 999rpx; background: #004ac6; }
.bar-1, .bar-5 { height: 18rpx; } .bar-2, .bar-7 { height: 42rpx; } .bar-3, .bar-6 { height: 60rpx; } .bar-4, .bar-8 { height: 30rpx; }
.listening-text { color: #004ac6; font-size: 26rpx; font-weight: 900; text-align: center; }
.question-card { border: 2rpx solid rgba(0,74,198,0.2); border-radius: 24rpx; padding: 32rpx; background: #d3e4fe; }
.question-label { display: block; color: #004ac6; font-size: 24rpx; font-weight: 900; margin-bottom: 14rpx; }
.question-text { color: #0b1c30; font-size: 34rpx; font-weight: 900; line-height: 1.45; }
.video-card { position: relative; overflow: hidden; min-height: 720rpx; border-radius: 24rpx; background: linear-gradient(135deg, #213145, #0b1c30); box-shadow: 0 16rpx 44rpx rgba(11,28,48,0.24); }
.rec-badge { position: absolute; left: 28rpx; top: 28rpx; display: flex; align-items: center; gap: 12rpx; border-radius: 999rpx; padding: 10rpx 18rpx; background: rgba(186,26,26,0.82); color: #fff; font-size: 24rpx; font-weight: 900; }
.rec-dot { width: 14rpx; height: 14rpx; border-radius: 999rpx; background: #fff; }
.video-person { position: absolute; inset: 0; display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 20rpx; color: #fff; padding: 48rpx; text-align: center; }
.person-avatar { display: flex; align-items: center; justify-content: center; width: 180rpx; height: 180rpx; border-radius: 999rpx; background: rgba(255,255,255,0.14); border: 2rpx solid rgba(255,255,255,0.24); font-size: 56rpx; font-weight: 900; }
.video-title { font-size: 36rpx; font-weight: 900; }
.video-desc { color: rgba(255,255,255,0.72); font-size: 26rpx; line-height: 1.6; }
.control-bar { position: absolute; left: 50%; bottom: 32rpx; transform: translateX(-50%); display: flex; align-items: center; gap: 18rpx; border: 2rpx solid rgba(255,255,255,0.12); border-radius: 999rpx; padding: 16rpx; background: rgba(11,28,48,0.48); }
.round-control, .end-control { display: flex; align-items: center; justify-content: center; min-height: 88rpx; border-radius: 999rpx; color: #fff; font-weight: 900; }
.round-control { width: 88rpx; background: rgba(255,255,255,0.12); }
.round-control.next { background: #004ac6; }
.end-control { padding: 0 44rpx; background: #ba1a1a; }
.voice-right { display: flex; flex-direction: column; overflow: hidden; }
.transcript-head { display: flex; align-items: center; justify-content: space-between; gap: 16rpx; padding: 28rpx; border-bottom: 2rpx solid #c3c6d7; background: #eff4ff; }
.transcript-list { display: flex; flex-direction: column; gap: 24rpx; padding: 32rpx; }
.transcript-item { display: flex; flex-direction: column; gap: 8rpx; }
.transcript-item.active { padding-left: 18rpx; border-left: 4rpx solid #004ac6; opacity: 0.8; }
.transcript-time { color: #004ac6; font-size: 24rpx; font-weight: 900; }
.transcript-text { color: #434655; font-size: 26rpx; line-height: 1.7; }
.confidence-box { margin-top: auto; padding: 28rpx; border-top: 2rpx solid #c3c6d7; background: #eff4ff; }
.confidence-progress { width: 94%; height: 100%; background: #004ac6; }
@media (min-width: 768px) {
  .interview-head { flex-direction: row; justify-content: space-between; align-items: flex-end; }
  .head-copy { flex: 1; }
  .mode-switch { min-width: 420rpx; }
  .chat-layout { grid-template-columns: 300rpx minmax(0, 1fr) 380rpx; align-items: start; }
  .input-bottom { flex-direction: row; align-items: center; justify-content: space-between; }
  .input-actions { min-width: 380rpx; }
  .danger-button, .finish-button { flex: none; padding: 0 32rpx; }
  .voice-layout { grid-template-columns: 420rpx minmax(0, 1fr) 420rpx; align-items: stretch; }
  .voice-right { min-height: 720rpx; }
}
</style>
