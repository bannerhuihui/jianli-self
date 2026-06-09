<template>
  <view class="page landing-page">
    <AppTopNav active="首页" />

    <view class="hero-section">
      <view class="container hero-inner">
        <view class="hero-copy">
          <text class="hero-badge">新一代招聘 AI</text>
          <text class="hero-title">AI 人才理解层 -</text>
          <text class="hero-title accent">赋能每一位候选人与 HR</text>
          <text class="hero-desc">兼容各类 ATS、招聘平台与办公协同软件，深度解析简历，生成多版本专业简历，助你通过 AI 面试，展示最真实的才华。</text>
          <view class="hero-actions">
            <navigator url="/pages/candidate/upload/index" class="primary-button hero-btn">立即开始</navigator>
            <navigator url="/pages/hr/job/index" class="secondary-button hero-btn">了解更多</navigator>
          </view>
        </view>

        <view class="hero-visual-shell">
          <view class="hero-visual glass-card">
            <view class="window-bar">
              <view class="dot red" />
              <view class="dot yellow" />
              <view class="dot green" />
              <text class="match-badge">AI 匹配度: 98%</text>
            </view>
            <view class="skeleton-list">
              <view class="line w-70" />
              <view class="line" />
              <view class="line w-85" />
            </view>
            <view class="mini-grid">
              <view v-for="item in miniCards" :key="item.title" class="mini-card" :class="item.tone">
                <text class="mini-icon"><AppIcon :name="item.icon" :size="24" /></text>
                <text class="mini-title">{{ item.title }}</text>
              </view>
            </view>
          </view>
          <view class="decor decor-a" />
          <view class="decor decor-b" />
        </view>
      </view>
    </view>

    <view class="compat-section">
      <text class="compat-title">全面兼容主流招聘与协同生态</text>
      <view class="compat-list">
        <view v-for="item in compat" :key="item.title" class="compat-item">
          <text class="compat-icon"><AppIcon :name="item.icon" :size="22" color="#737686" /></text>
          <text>{{ item.title }}</text>
        </view>
      </view>
    </view>

    <view class="container feature-section">
      <view class="section-head">
        <text class="section-main-title">重塑人才发现与投递体验</text>
        <text class="section-desc">利用尖端 AI 技术，我们将碎片化的信息转化为深度的人才洞察，让每一次招聘都基于真实的数据证据。</text>
      </view>
      <view class="bento-grid">
        <view class="bento-card deep-card">
          <view class="feature-copy">
            <view class="feature-icon"><AppIcon name="search" :size="28" color="#004ac6" /></view>
            <text class="feature-title">简历深度解析</text>
            <text class="feature-desc">不仅是文字提取。我们通过 LLM 理解工作经验背后的实际贡献，提取超过 200 个维度的技能标签。</text>
          </view>
          <view class="doc-preview desktop-only">
            <view class="doc-line strong" />
            <view class="doc-line" />
            <view class="doc-line short" />
            <view class="doc-line faint" />
          </view>
        </view>

        <view class="bento-card interview-card">
          <view class="feature-icon inverse"><AppIcon name="chat" :size="28" color="#ffffff" /></view>
          <text class="feature-title light">AI 深度访谈</text>
          <text class="feature-desc light-desc">沉浸式文字/语音面试体验，实时反馈面试表现，帮助候选人以最佳状态面对机会。</text>
          <view class="blur-orb" />
        </view>

        <view class="bento-card profile-card">
          <view class="feature-icon green"><AppIcon name="person" :size="28" color="#006242" /></view>
          <text class="feature-title">人才画像</text>
          <text class="feature-desc">雷达图多维评分，包括技术广度、项目深度、软技能及文化契合度，让“人才”不再只是纸面数据。</text>
          <view class="radar-placeholder">
            <view class="radar-ring" />
            <text class="radar-text">94</text>
          </view>
        </view>

        <view class="bento-card resume-card">
          <view class="resume-copy">
            <view class="feature-icon white"><AppIcon name="paperclip" :size="28" color="#ffffff" /></view>
            <text class="feature-title light">多版本简历生成</text>
            <text class="feature-desc light-desc">针对不同行业、不同岗位，AI 自动调整简历侧重点，生成最符合 JD 要求的专业投递版本。</text>
          </view>
          <view class="version-grid">
            <text v-for="version in versions" :key="version" class="version-pill">{{ version }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="cta-section">
      <view class="container">
        <view class="cta-card">
          <view class="cta-bar" />
          <text class="cta-title">准备好开启 AI 赋能的职业之旅了吗？</text>
          <text class="cta-desc">加入领先候选人与 HR，体验基于深度理解的高效招聘。</text>
          <view class="cta-actions">
            <navigator url="/pages/candidate/upload/index" class="primary-button cta-btn">立即免费开始</navigator>
            <navigator url="/pages/hr/job/index" class="secondary-button cta-btn">预约演示</navigator>
          </view>
        </view>
      </view>
    </view>

    <view class="footer-section">
      <view class="container footer-inner">
        <view class="footer-brand">
          <text class="footer-title">AI Talent Agent</text>
          <text class="footer-desc">下一代 AI 人才理解基础设施，构建更智能的招聘生态。</text>
        </view>
        <view class="footer-links">
          <text v-for="link in footerLinks" :key="link">{{ link }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import AppTopNav from '../../components/AppTopNav.vue';
import AppIcon from '../../components/AppIcon.vue';

const miniCards = [
  { title: '认知能力', icon: 'person', tone: 'blue' },
  { title: '技术栈', icon: 'compose', tone: 'green' },
  { title: '沟通表达', icon: 'chat', tone: 'gray' },
];
const compat = [
  { title: '飞书', icon: 'paperplane' },
  { title: '企业微信', icon: 'chatboxes' },
  { title: 'Excel', icon: 'list' },
  { title: 'PDF 导出', icon: 'download' },
];
const versions = ['技术研发版', '产品经理版', '市场营销版', '管理通用版'];
const footerLinks = ['ATS 集成', 'Excel 支持', '飞书插件', '企微同步', '隐私政策', '服务条款'];
</script>

<style lang="scss" scoped>
.landing-page { background: #f8f9ff; color: #0b1c30; }
.hero-section { overflow: hidden; background: linear-gradient(135deg, #ffffff 0%, #e5eeff 100%); }
.hero-inner { display: flex; flex-direction: column; gap: 64rpx; padding-top: 112rpx; padding-bottom: 112rpx; }
.hero-copy { display: flex; flex-direction: column; gap: 24rpx; }
.hero-badge { align-self: flex-start; border-radius: 999rpx; padding: 12rpx 28rpx; color: #fff; background: #2563eb; font-size: 26rpx; font-weight: 900; }
.hero-title { display: block; color: #0b1c30; font-size: 68rpx; font-weight: 900; line-height: 1.08; letter-spacing: -1rpx; }
.accent { color: #004ac6; }
.hero-desc { color: #565e74; font-size: 30rpx; line-height: 1.7; }
.hero-actions { display: flex; flex-direction: column; gap: 20rpx; padding-top: 16rpx; }
.hero-visual-shell { position: relative; }
.hero-visual { position: relative; overflow: hidden; padding: 42rpx; transform: rotate(1deg); min-height: 420rpx; }
.window-bar { display: flex; align-items: center; gap: 12rpx; margin-bottom: 32rpx; }
.dot { width: 22rpx; height: 22rpx; border-radius: 999rpx; }
.red { background: #ba1a1a; } .yellow { background: #facc15; } .green { background: #007d55; }
.match-badge { margin-left: auto; border-radius: 999rpx; padding: 8rpx 18rpx; color: #065f46; background: rgba(16, 185, 129, 0.1); font-size: 22rpx; font-weight: 900; }
.skeleton-list { display: flex; flex-direction: column; gap: 18rpx; }
.line { height: 24rpx; border-radius: 999rpx; background: #dce9ff; }
.w-70 { width: 70%; } .w-85 { width: 85%; }
.mini-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 18rpx; margin-top: 36rpx; }
.mini-card { display: flex; flex-direction: column; align-items: center; justify-content: center; gap: 12rpx; min-height: 140rpx; border-radius: 18rpx; }
.mini-card.blue { background: rgba(0, 74, 198, 0.1); color: #004ac6; }
.mini-card.green { background: rgba(0, 98, 66, 0.1); color: #006242; }
.mini-card.gray { background: rgba(86, 94, 116, 0.1); color: #565e74; }
.mini-icon { font-weight: 900; font-size: 30rpx; }
.mini-title { font-size: 23rpx; font-weight: 800; }
.decor { position: absolute; width: 180rpx; height: 180rpx; border-radius: 999rpx; opacity: 0.12; filter: blur(20rpx); }
.decor-a { right: -60rpx; top: -50rpx; background: #004ac6; }
.decor-b { left: -60rpx; bottom: -50rpx; background: #007d55; }
.compat-section { padding: 48rpx 32rpx; border-top: 2rpx solid #c3c6d7; border-bottom: 2rpx solid #c3c6d7; background: #fff; text-align: center; }
.compat-title { color: #565e74; font-size: 23rpx; font-weight: 900; letter-spacing: 3rpx; }
.compat-list { display: flex; flex-wrap: wrap; justify-content: center; gap: 40rpx; margin-top: 32rpx; }
.compat-item { display: flex; align-items: center; gap: 12rpx; color: #565e74; font-size: 30rpx; font-weight: 900; opacity: 0.72; }
.compat-icon { color: #737686; font-size: 32rpx; }
.feature-section { padding-top: 96rpx; padding-bottom: 96rpx; }
.section-head { display: flex; flex-direction: column; align-items: center; gap: 18rpx; margin-bottom: 64rpx; text-align: center; }
.section-main-title { color: #0b1c30; font-size: 48rpx; font-weight: 900; }
.section-desc { max-width: 900rpx; color: #565e74; font-size: 28rpx; line-height: 1.7; }
.bento-grid { display: grid; gap: 24rpx; }
.bento-card { position: relative; overflow: hidden; border-radius: 24rpx; padding: 48rpx; border: 2rpx solid #e2e8f0; background: rgba(255,255,255,0.82); box-shadow: 0 10rpx 30rpx rgba(0,74,198,0.06); }
.feature-copy, .resume-copy { display: flex; flex-direction: column; gap: 20rpx; }
.feature-icon { display: flex; align-items: center; justify-content: center; width: 96rpx; height: 96rpx; border-radius: 18rpx; background: rgba(0,74,198,0.1); color: #004ac6; font-size: 36rpx; font-weight: 900; }
.feature-icon.inverse { background: #004ac6; color: #fff; }
.feature-icon.green { background: rgba(0,98,66,0.1); color: #006242; }
.feature-icon.white { background: rgba(255,255,255,0.18); color: #fff; }
.feature-title { display: block; color: #0b1c30; font-size: 36rpx; font-weight: 900; }
.feature-desc { color: #565e74; font-size: 27rpx; line-height: 1.7; }
.light { color: #fff; }
.light-desc { color: rgba(255,255,255,0.82); }
.deep-card { display: flex; gap: 40rpx; justify-content: space-between; }
.doc-preview { width: 300rpx; height: 300rpx; border-radius: 18rpx; background: #e5eeff; padding: 28rpx; flex-direction: column; justify-content: center; }
.doc-line { height: 16rpx; margin-bottom: 16rpx; border-radius: 999rpx; background: rgba(0, 74, 198, 0.18); }
.doc-line.strong { width: 100%; background: rgba(0, 74, 198, 0.32); }
.doc-line.short { width: 52%; }
.doc-line.faint { width: 70%; background: rgba(0, 74, 198, 0.1); margin-top: 32rpx; }
.interview-card { min-height: 380rpx; background: #213145; color: #fff; }
.blur-orb { position: absolute; right: -40rpx; bottom: -40rpx; width: 220rpx; height: 220rpx; border-radius: 999rpx; background: rgba(0,74,198,0.28); filter: blur(20rpx); }
.profile-card { background: #fff; }
.radar-placeholder { display: flex; align-items: center; justify-content: center; width: 210rpx; height: 210rpx; margin: 32rpx auto 0; border: 8rpx dashed #c3c6d7; border-radius: 999rpx; position: relative; }
.radar-ring { position: absolute; inset: 34rpx; border: 4rpx solid #d3e4fe; border-radius: 999rpx; }
.radar-text { color: #737686; font-size: 42rpx; font-weight: 900; }
.resume-card { display: flex; flex-direction: column; gap: 40rpx; background: #2563eb; color: #fff; border-color: #2563eb; }
.version-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 18rpx; }
.version-pill { border: 2rpx solid rgba(255,255,255,0.24); border-radius: 12rpx; padding: 22rpx; background: rgba(255,255,255,0.1); color: #fff; text-align: center; font-weight: 900; font-size: 24rpx; }
.cta-section { padding: 96rpx 0; background: #fff; }
.cta-card { position: relative; overflow: hidden; border-radius: 32rpx; padding: 72rpx 48rpx; background: rgba(255,255,255,0.86); box-shadow: 0 22rpx 56rpx rgba(11,28,48,0.12); text-align: center; }
.cta-bar { position: absolute; top: 0; left: 0; right: 0; height: 8rpx; background: linear-gradient(90deg,#004ac6,#007d55,#004ac6); }
.cta-title { display: block; color: #0b1c30; font-size: 46rpx; font-weight: 900; line-height: 1.25; }
.cta-desc { display: block; margin-top: 22rpx; color: #565e74; font-size: 28rpx; line-height: 1.7; }
.cta-actions { display: flex; flex-direction: column; gap: 20rpx; margin-top: 44rpx; }
.footer-section { border-top: 2rpx solid #c3c6d7; background: #e5eeff; padding: 56rpx 0; }
.footer-inner { display: flex; flex-direction: column; gap: 40rpx; }
.footer-brand { display: flex; flex-direction: column; gap: 12rpx; }
.footer-title { color: #565e74; font-size: 32rpx; font-weight: 900; }
.footer-desc { color: #3f465c; font-size: 25rpx; line-height: 1.6; }
.footer-links { display: grid; grid-template-columns: repeat(2, 1fr); gap: 18rpx 40rpx; color: #3f465c; font-size: 24rpx; font-weight: 800; }
@media (min-width: 768px) {
  .hero-inner { flex-direction: row; align-items: center; }
  .hero-copy, .hero-visual-shell { flex: 1; }
  .hero-actions { flex-direction: row; }
  .hero-btn { padding: 0 56rpx; }
  .bento-grid { grid-template-columns: repeat(12, 1fr); }
  .deep-card { grid-column: span 7; }
  .interview-card { grid-column: span 5; }
  .profile-card { grid-column: span 5; }
  .resume-card { grid-column: span 7; flex-direction: row; align-items: center; }
  .resume-copy, .version-grid { flex: 1; }
  .cta-actions { flex-direction: row; justify-content: center; }
  .cta-btn { min-width: 260rpx; padding: 0 44rpx; }
  .footer-inner { flex-direction: row; justify-content: space-between; align-items: flex-start; }
  .footer-links { grid-template-columns: repeat(3, auto); }
}
</style>
