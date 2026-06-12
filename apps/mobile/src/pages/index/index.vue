<template>
  <view class="page landing-page">
    <AppTopNav active="首页" />

    <view class="hero-section">
      <view class="container hero-inner">
        <view class="hero-copy">
          <text class="hero-badge">新一代招聘 AI</text>
          <view class="hero-title-block">
            <text class="hero-title">AI 人才理解层 -</text>
            <text class="hero-title accent">赋能每一位候选人与 HR</text>
          </view>
          <text class="hero-desc">兼容各类 ATS、招聘平台与办公协同软件，深度解析简历，生成多版本专业简历，助你通过 AI 面试，展示最真实的才华。</text>
          <view class="hero-actions">
            <navigator url="/pages/candidate/upload/index" class="hero-btn hero-btn-primary">立即开始</navigator>
            <navigator url="/pages/hr/job/index" class="hero-btn hero-btn-secondary">了解更多</navigator>
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
              <view class="line w-75" />
              <view class="line w-full" />
              <view class="line w-85" />
            </view>
            <view class="mini-grid">
              <view v-for="item in miniCards" :key="item.title" class="mini-card" :class="item.tone">
                <AppIcon :name="item.icon" :size="24" :color="item.color" />
                <text class="mini-title">{{ item.title }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="compat-section">
      <view class="container compat-inner">
        <text class="compat-title">全面兼容主流招聘与协同生态</text>
        <view class="compat-list">
          <view v-for="item in compat" :key="item.title" class="compat-item">
            <AppIcon :name="item.icon" :size="22" color="#565e74" />
            <text>{{ item.title }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="feature-section">
      <view class="container">
        <view class="section-head">
          <text class="section-main-title">重塑人才发现与投递体验</text>
          <text class="section-desc">利用尖端 AI 技术，我们将碎片化的信息转化为深度的人才洞察，让每一次招聘都基于真实的数据证据。</text>
        </view>
        <view class="bento-grid">
          <view class="bento-card deep-card glass-card">
            <view class="deep-layout">
              <view class="feature-copy">
                <view class="feature-icon primary"><AppIcon name="analytics" :size="30" color="#004ac6" /></view>
                <text class="feature-title">简历深度解析</text>
                <text class="feature-desc">不仅是文字提取。我们通过 LLM 理解工作经验背后的实际贡献，提取超过 200 个维度的技能标签。</text>
              </view>
              <view class="doc-preview desktop-only">
                <view class="doc-line strong" />
                <view class="doc-line mid" />
                <view class="doc-line short" />
                <view class="doc-line faint" />
                <view class="doc-line faint short" />
              </view>
            </view>
          </view>

          <view class="bento-card interview-card">
            <view class="interview-content">
              <view class="feature-icon inverse"><AppIcon name="interpreter_mode" :size="30" color="#ffffff" /></view>
              <text class="feature-title light">AI 深度访谈</text>
              <text class="feature-desc light-desc">沉浸式语音/视频面试体验，实时反馈面试表现，帮助候选人以最佳状态面对机会。</text>
            </view>
            <view class="blur-orb" />
          </view>

          <view class="bento-card profile-card">
            <view class="feature-icon green"><AppIcon name="radar" :size="30" color="#006242" /></view>
            <text class="feature-title">人才画像</text>
            <text class="feature-desc">雷达图多维评分，包括技术广度、项目深度、软技能及文化契合度，让“人才”不再只是纸面数据。</text>
            <view class="radar-placeholder">
              <AppIcon name="person_search" :size="36" color="#737686" />
            </view>
          </view>

          <view class="bento-card resume-card">
            <view class="resume-layout">
              <view class="resume-copy">
                <view class="feature-icon white"><AppIcon name="auto_stories" :size="30" color="#ffffff" /></view>
                <text class="feature-title light">多版本简历生成</text>
                <text class="feature-desc light-desc">针对不同行业、不同岗位，AI 自动调整简历侧重点，生成最符合 JD 要求的专业投递版本。</text>
              </view>
              <view class="version-grid">
                <text v-for="version in versions" :key="version" class="version-pill">{{ version }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <view class="cta-section">
      <view class="container cta-wrap">
        <view class="cta-card glass-card">
          <view class="cta-bar" />
          <text class="cta-title">准备好开启 AI 赋能的职业之旅了吗？</text>
          <text class="cta-desc">加入 50,000+ 领先候选人与 HR，体验基于深度理解的高效招聘。</text>
          <view class="cta-actions">
            <navigator url="/pages/candidate/upload/index" class="cta-btn cta-btn-primary">立即免费开始</navigator>
            <navigator url="/pages/hr/job/index" class="cta-btn cta-btn-secondary">预约演示</navigator>
          </view>
        </view>
      </view>
    </view>

    <view class="footer-section">
      <view class="container footer-inner">
        <view class="footer-top">
          <view class="footer-brand">
            <text class="footer-title">AI Talent Agent</text>
            <text class="footer-desc">下一代 AI 人才理解基础设施，构建更智能的招聘生态。</text>
          </view>
          <view class="footer-links">
            <text v-for="link in footerLinks" :key="link" class="footer-link">{{ link }}</text>
          </view>
        </view>
        <view class="footer-bottom">
          <text class="footer-copy">© 2024 AI Talent Agent. 版权所有.</text>
          <view class="footer-utils">
            <AppIcon name="language" :size="20" color="#565e74" />
            <AppIcon name="help_outline" :size="20" color="#565e74" />
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import AppTopNav from '../../components/AppTopNav.vue';
import AppIcon from '../../components/AppIcon.vue';

const miniCards = [
  { title: '认知能力', icon: 'psychology', tone: 'blue', color: '#004ac6' },
  { title: '技术栈', icon: 'code', tone: 'green', color: '#006242' },
  { title: '沟通表达', icon: 'forum', tone: 'gray', color: '#565e74' },
];
const compat = [
  { title: '飞书', icon: 'grid_view' },
  { title: '企业微信', icon: 'chat_bubble' },
  { title: 'Excel', icon: 'table_chart' },
  { title: 'PDF 导出', icon: 'picture_as_pdf' },
];
const versions = ['技术研发版', '产品经理版', '市场营销版', '管理通用版'];
const footerLinks = ['ATS 集成', 'Excel 支持', '飞书插件', '企微同步', '隐私政策', '服务条款'];
</script>

<style lang="scss" scoped>
.landing-page { background: #f8f9ff; color: #0b1c30; }

/* Hero */
.hero-section { overflow: hidden; background: linear-gradient(135deg, #ffffff 0%, #e5eeff 100%); }
.hero-inner { display: flex; flex-direction: column; gap: 96rpx; padding-top: 96rpx; padding-bottom: 96rpx; }
.hero-copy { display: flex; flex-direction: column; align-items: flex-start; gap: 48rpx; }
.hero-badge {
  align-self: flex-start;
  border-radius: 999rpx;
  padding: 12rpx 32rpx;
  color: #fff;
  background: #2563eb;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
}
.hero-title-block { display: flex; flex-direction: column; gap: 0; }
.hero-title {
  display: block;
  color: #0b1c30;
  font-size: 72rpx;
  font-weight: 700;
  line-height: 1.22;
  letter-spacing: -0.02em;
}
.hero-title.accent { color: #004ac6; }
.hero-desc {
  max-width: 1152rpx;
  color: #565e74;
  font-size: 36rpx;
  font-weight: 400;
  line-height: 1.56;
}
.hero-actions { display: flex; flex-wrap: wrap; gap: 32rpx; padding-top: 8rpx; }
.hero-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24rpx 64rpx;
  border-radius: 16rpx;
  font-size: 28rpx;
  font-weight: 600;
  line-height: 1.14;
  box-sizing: border-box;
}
.hero-btn-primary { background: #004ac6; color: #fff; box-shadow: 0 8rpx 20rpx rgba(0, 74, 198, 0.22); }
.hero-btn-secondary { border: 2rpx solid #737686; background: transparent; color: #0b1c30; }

.hero-visual-shell { position: relative; }
.hero-visual {
  position: relative;
  overflow: hidden;
  padding: 48rpx;
  min-height: 420rpx;
  border-radius: 16rpx;
  box-shadow: 0 20rpx 50rpx rgba(15, 23, 42, 0.12);
  transform: rotate(1deg);
  transition: transform 0.5s ease;
}
.hero-visual:hover { transform: rotate(0); }
.window-bar { display: flex; align-items: center; gap: 12rpx; margin-bottom: 32rpx; }
.dot { width: 22rpx; height: 22rpx; border-radius: 999rpx; }
.red { background: #ba1a1a; }
.yellow { background: #facc15; }
.green { background: #007d55; }
.match-badge {
  margin-left: auto;
  border-radius: 999rpx;
  padding: 8rpx 18rpx;
  color: #065f46;
  background: rgba(16, 185, 129, 0.1);
  font-size: 24rpx;
  font-weight: 500;
}
.skeleton-list { display: flex; flex-direction: column; gap: 16rpx; }
.line { height: 16rpx; border-radius: 999rpx; background: #dce9ff; }
.line.w-75 { width: 75%; background: #dce9ff; }
.line.w-full { width: 100%; background: #e5eeff; }
.line.w-85 { width: 83%; background: #e5eeff; }
.mini-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 32rpx; margin-top: 48rpx; }
.mini-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16rpx;
  min-height: 160rpx;
  border-radius: 8rpx;
}
.mini-card.blue { background: rgba(0, 74, 198, 0.1); }
.mini-card.green { background: rgba(0, 98, 66, 0.1); }
.mini-card.gray { background: rgba(86, 94, 116, 0.1); }
.mini-title { color: inherit; font-size: 24rpx; font-weight: 500; line-height: 1.33; }
.mini-card.blue .mini-title { color: #004ac6; }
.mini-card.green .mini-title { color: #006242; }
.mini-card.gray .mini-title { color: #565e74; }

/* Compatibility */
.compat-section {
  padding: 96rpx 0;
  border-top: 2rpx solid #c3c6d7;
  border-bottom: 2rpx solid #c3c6d7;
  background: #fff;
}
.compat-inner { text-align: center; }
.compat-title {
  display: block;
  color: #565e74;
  font-size: 24rpx;
  font-weight: 500;
  letter-spacing: 0.12em;
  text-transform: uppercase;
  margin-bottom: 64rpx;
}
.compat-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 96rpx;
  opacity: 0.6;
  filter: grayscale(1);
  transition: opacity 0.3s ease, filter 0.3s ease;
}
.compat-list:hover {
  opacity: 1;
  filter: grayscale(0);
}
.compat-item {
  display: flex;
  align-items: center;
  gap: 16rpx;
  color: #565e74;
  font-size: 28rpx;
  font-weight: 600;
}

/* Features */
.feature-section { padding: 192rpx 0; background: #f8f9ff; }
.section-head {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 32rpx;
  margin-bottom: 128rpx;
  text-align: center;
}
.section-main-title {
  color: #0b1c30;
  font-size: 60rpx;
  font-weight: 700;
  line-height: 1.27;
  letter-spacing: -0.01em;
}
.section-desc {
  max-width: 1344rpx;
  color: #565e74;
  font-size: 32rpx;
  font-weight: 400;
  line-height: 1.5;
}
.bento-grid { display: grid; gap: 48rpx; }
.bento-card {
  position: relative;
  overflow: hidden;
  border-radius: 16rpx;
  padding: 64rpx;
}
.feature-copy, .resume-copy { display: flex; flex-direction: column; gap: 32rpx; }
.feature-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 96rpx;
  height: 96rpx;
  border-radius: 16rpx;
}
.feature-icon.primary { background: rgba(0, 74, 198, 0.1); }
.feature-icon.inverse { background: #004ac6; }
.feature-icon.green { background: rgba(0, 98, 66, 0.1); }
.feature-icon.white { background: rgba(255, 255, 255, 0.2); }
.feature-title {
  display: block;
  color: #0b1c30;
  font-size: 48rpx;
  font-weight: 600;
  line-height: 1.33;
}
.feature-desc { color: #565e74; font-size: 32rpx; font-weight: 400; line-height: 1.5; }
.light { color: #fff; }
.light-desc { color: rgba(211, 228, 254, 0.8); }

.deep-card { border: 2rpx solid #e2e8f0; background: rgba(255, 255, 255, 0.8); }
.deep-layout { display: flex; flex-direction: column; gap: 40rpx; }
.doc-preview {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 384rpx;
  height: 384rpx;
  border-radius: 16rpx;
  background: #e5eeff;
  padding: 32rpx;
  box-sizing: border-box;
}
.doc-line { height: 8rpx; margin-bottom: 16rpx; border-radius: 999rpx; background: rgba(0, 74, 198, 0.2); }
.doc-line.strong { width: 100%; }
.doc-line.mid { width: 75%; }
.doc-line.short { width: 50%; background: rgba(0, 74, 198, 0.4); }
.doc-line.faint { width: 100%; background: rgba(0, 74, 198, 0.1); margin-top: 32rpx; }
.doc-line.faint.short { width: 66%; margin-top: 0; }

.interview-card { min-height: 380rpx; background: #213145; border: none; }
.interview-content { position: relative; z-index: 1; display: flex; flex-direction: column; gap: 32rpx; }
.blur-orb {
  position: absolute;
  right: -32rpx;
  bottom: -32rpx;
  width: 256rpx;
  height: 256rpx;
  border-radius: 999rpx;
  background: rgba(0, 74, 198, 0.2);
  filter: blur(40rpx);
  transition: transform 0.7s ease;
}
.interview-card:hover .blur-orb { transform: scale(1.5); }

.profile-card {
  background: #fff;
  border: 2rpx solid #c3c6d7;
  transition: border-color 0.3s ease;
}
.profile-card:hover { border-color: #004ac6; }
.radar-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 256rpx;
  height: 256rpx;
  margin: 32rpx auto 0;
  border: 8rpx dashed #c3c6d7;
  border-radius: 999rpx;
}

.resume-card { background: #2563eb; border: none; color: #fff; }
.resume-layout { display: flex; flex-direction: column; gap: 64rpx; }
.version-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 24rpx; }
.version-pill {
  border: 2rpx solid rgba(255, 255, 255, 0.2);
  border-radius: 8rpx;
  padding: 32rpx 24rpx;
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  text-align: center;
  font-weight: 500;
  font-size: 24rpx;
  line-height: 1.33;
}

/* CTA */
.cta-section { padding: 192rpx 0; background: #fff; }
.cta-wrap { max-width: 896px; }
.cta-card {
  position: relative;
  overflow: hidden;
  border-radius: 32rpx;
  padding: 96rpx 64rpx;
  background: rgba(255, 255, 255, 0.8);
  border: none;
  box-shadow: 0 24rpx 64rpx rgba(15, 23, 42, 0.14);
  text-align: center;
}
.cta-bar {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4rpx;
  background: linear-gradient(90deg, #004ac6, #007d55, #004ac6);
}
.cta-title {
  display: block;
  color: #0b1c30;
  font-size: 60rpx;
  font-weight: 700;
  line-height: 1.27;
  letter-spacing: -0.01em;
}
.cta-desc {
  display: block;
  margin-top: 48rpx;
  color: #565e74;
  font-size: 36rpx;
  font-weight: 400;
  line-height: 1.56;
  max-width: 1344rpx;
  margin-left: auto;
  margin-right: auto;
}
.cta-actions {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
  margin-top: 80rpx;
  justify-content: center;
}
.cta-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32rpx 80rpx;
  border-radius: 16rpx;
  font-size: 40rpx;
  font-weight: 600;
  line-height: 1.4;
  box-sizing: border-box;
}
.cta-btn-primary {
  background: #004ac6;
  color: #fff;
  box-shadow: 0 12rpx 28rpx rgba(0, 74, 198, 0.22);
}
.cta-btn-secondary {
  background: #e5eeff;
  color: #004ac6;
}

/* Footer */
.footer-section {
  border-top: 2rpx solid #c3c6d7;
  background: #e5eeff;
  padding: 96rpx 0;
}
.footer-inner { display: flex; flex-direction: column; gap: 96rpx; }
.footer-top { display: flex; flex-direction: column; gap: 64rpx; }
.footer-brand { display: flex; flex-direction: column; gap: 32rpx; max-width: 520rpx; }
.footer-title { color: #565e74; font-size: 28rpx; font-weight: 600; }
.footer-desc { color: #3f465c; font-size: 28rpx; font-weight: 400; line-height: 1.43; }
.footer-links {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 32rpx 96rpx;
}
.footer-link { color: #3f465c; font-size: 24rpx; font-weight: 500; line-height: 1.33; }
.footer-bottom {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
  padding-top: 64rpx;
  border-top: 2rpx solid rgba(195, 198, 215, 0.3);
}
.footer-copy { color: #3f465c; font-size: 28rpx; opacity: 0.6; }
.footer-utils { display: flex; gap: 32rpx; opacity: 0.6; }

@media (min-width: 768px) {
  .hero-inner { flex-direction: row; align-items: center; gap: 96px; padding-top: 96px; padding-bottom: 96px; }
  .hero-copy, .hero-visual-shell { flex: 1; max-width: 50%; }
  .hero-copy { gap: 24px; }
  .hero-badge { padding: 6px 16px; font-size: 14px; }
  .hero-title { font-size: 36px; line-height: 44px; }
  .hero-desc { font-size: 18px; line-height: 28px; max-width: 36rem; }
  .hero-actions { flex-direction: row; gap: 16px; padding-top: 16px; }
  .hero-btn { padding: 12px 32px; border-radius: 8px; font-size: 14px; }
  .hero-visual { padding: 24px; border-radius: 12px; }

  .compat-section { padding: 48px 0; }
  .compat-title { font-size: 12px; margin-bottom: 32px; }
  .compat-list { gap: 48px; }
  .compat-item { font-size: 14px; gap: 8px; }

  .feature-section { padding: 96px 0; }
  .section-head { margin-bottom: 64px; gap: 16px; }
  .section-main-title { font-size: 30px; line-height: 38px; }
  .section-desc { font-size: 16px; line-height: 24px; max-width: 42rem; }
  .bento-grid { grid-template-columns: repeat(12, 1fr); gap: 24px; }
  .bento-card { padding: 32px; border-radius: 12px; }
  .deep-card { grid-column: span 7; }
  .deep-layout { flex-direction: row; justify-content: space-between; align-items: flex-start; }
  .interview-card { grid-column: span 5; }
  .profile-card { grid-column: span 5; }
  .resume-card { grid-column: span 7; }
  .resume-layout { flex-direction: row; align-items: center; gap: 32px; }
  .resume-copy, .version-grid { flex: 1; }
  .feature-title { font-size: 24px; line-height: 32px; }
  .feature-desc { font-size: 16px; line-height: 24px; }
  .doc-preview { width: 192px; height: 192px; padding: 16px; }
  .radar-placeholder { width: 128px; height: 128px; }

  .cta-section { padding: 96px 0; }
  .cta-card { padding: 48px; border-radius: 16px; }
  .cta-title { font-size: 30px; line-height: 38px; }
  .cta-desc { font-size: 18px; line-height: 28px; margin-top: 24px; }
  .cta-actions { flex-direction: row; gap: 16px; margin-top: 40px; }
  .cta-btn { padding: 16px 40px; border-radius: 8px; font-size: 20px; line-height: 28px; }

  .footer-section { padding: 48px 0; }
  .footer-top { flex-direction: row; justify-content: space-between; align-items: flex-start; gap: 32px; }
  .footer-links { grid-template-columns: repeat(3, auto); gap: 16px 48px; }
  .footer-bottom { flex-direction: row; justify-content: space-between; align-items: center; padding-top: 32px; }
  .footer-title { font-size: 14px; }
  .footer-desc { font-size: 14px; }
  .footer-link { font-size: 12px; }
  .footer-copy { font-size: 14px; }
}
</style>
