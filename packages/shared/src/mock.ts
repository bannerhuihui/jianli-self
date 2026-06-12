import type { JobProfile, MatchResult, StructuredResume, TalentProfile } from '@ai-talent-agent/domain';

export const mockResume: StructuredResume = {
  basicInfo: {
    id: 'candidate-001',
    name: '张伟',
    title: '高级软件工程师',
    location: '上海',
    experienceYears: 8,
    education: '清华大学 · 计算机科学与技术',
    phone: '138-0000-0000',
    email: 'zhangwei@example.com',
  },
  education: ['清华大学 · 计算机科学与技术 · 学士 · 2016-2020'],
  workExperience: [
    '阿里巴巴 · 高级软件工程师 · 负责微服务架构升级，支撑千万级 QPS。',
    '主导自动化测试框架，减少人工回归测试成本。',
  ],
  projects: ['AI 智能人才评估系统', '云服务分布式架构重构'],
  skills: ['TypeScript', 'Node.js', 'React', 'Kubernetes', 'MySQL', 'Redis'],
};

export const mockTalentProfile: TalentProfile = {
  candidate: mockResume.basicInfo,
  summary: '该候选人具备较强的分布式架构设计能力，在复杂系统建设和技术推进方面有明确证据。适合技术专家或架构方向岗位。',
  overallScore: 94,
  capabilities: [
    { key: 'learningAbility', name: '学习能力', score: 4.8, confidence: 'high', reason: '能快速掌握并应用新技术到复杂项目中。' },
    { key: 'logicAbility', name: '逻辑能力', score: 5.0, confidence: 'high', reason: '能清晰拆解复杂系统问题并给出方案。' },
    { key: 'communicationAbility', name: '沟通能力', score: 4.0, confidence: 'medium', reason: '有跨团队推进经验，但证据仍可继续补充。' },
    { key: 'executionAbility', name: '执行能力', score: 4.5, confidence: 'high', reason: '多次完成复杂项目交付。' },
    { key: 'innovationAbility', name: '创新能力', score: 4.9, confidence: 'high', reason: '提出自动化方案并产生业务价值。' },
    { key: 'leadershipAbility', name: '领导能力', score: 3.5, confidence: 'medium', reason: '有项目推进经验，但带团队证据不足。' },
    { key: 'stressTolerance', name: '抗压能力', score: 4.2, confidence: 'high', reason: '在高压系统升级中保持稳定交付。' },
    { key: 'careerStability', name: '职业稳定性', score: 2.0, confidence: 'low', reason: '近年变动较多，需要进一步确认动机。' },
  ],
  strengths: ['复杂系统架构经验丰富', '技术深度较强', '执行落地能力突出'],
  risks: ['职业稳定性待验证', '管理经验需要进一步确认'],
  preferences: ['技术深耕', '架构方向', '开放协作'],
  recommendedRoles: ['资深架构专家', '技术中台负责人'],
};

export const mockJobProfile: JobProfile = {
  id: 'job-001',
  title: '高级架构师（Cloud & AI）',
  seniority: '5-8 年互联网或高增长科技公司经验',
  responsibilities: ['负责高性能微服务架构设计', '推动核心模块升级', '指导初中级工程师'],
  requiredSkills: ['TypeScript', 'Node.js', 'React / Next.js', 'Kubernetes', 'PostgreSQL'],
  bonusSkills: ['Rust', 'Docker/K8s', 'Web3.js', '开源贡献'],
  requiredCapabilities: ['技术深度', '沟通协作', '系统架构', '团队领导力'],
  missingFields: ['建议补充工作模式偏好'],
};

export type ResumeVersionKey = 'ats' | 'hr' | 'platform' | 'email';

export const mockResumeVersionTexts: Record<ResumeVersionKey, string> = {
  ats: [
    '张伟 (Felix)',
    '高级全栈工程师 | 北京, 中国 | felix.zhang@example.com',
    '',
    '核心总结',
    '拥有 8 年以上构建可扩展 SaaS 架构的经验。精通 React、Node.js 和分布式系统，在大型科技公司有成功领导跨职能团队的记录。',
    '',
    '工作经历',
    '科技巨头解决方案 - 资深负责人 | 2020 - 至今',
    '• 使用 Golang 构建了每分钟处理 100 万次以上请求的微服务架构。',
    '• 领导分布在 3 个时区的 15 名开发人员组成的跨职能团队。',
    '• 通过系统重构将核心服务的延迟降低了 35%。',
    '',
    '专业技能',
    'JavaScript, TypeScript, Python, Go, Kubernetes, AWS, SQL, NoSQL.',
  ].join('\n'),
  hr: [
    '张伟 · 高级全栈工程师',
    '联系方式：felix.z@ai.com / 138-0000-0000',
    '',
    '个人总结',
    '致力于通过人工智能与前沿工程实践解决复杂商业问题，拥有卓越的技术前瞻性与团队领导力。',
    '',
    '核心项目',
    '云端分布式架构重构：负责公司核心系统的微服务转型，提升了 40% 的吞吐效率。',
    'AI 智能人才评估系统：主导开发基于 NLP 的简历解析引擎，准确率达到 98.5%。',
  ].join('\n'),
  platform: [
    '张伟 | 高级架构师 · 8 年经验',
    '上海 · 清华大学 · 计算机科学与技术',
    '',
    '一句话亮点',
    '擅长分布式系统与云原生架构，具备从 0 到 1 推动复杂技术方案落地的经验。',
    '',
    '核心技能',
    'Kubernetes · Node.js · PostgreSQL · 系统设计 · 跨团队推进',
    '',
    '适合岗位',
    '后端架构师 · 技术专家 · 云原生平台负责人',
  ].join('\n'),
  email: [
    '主题：候选人推荐 - 张伟（高级架构师）',
    '',
    '您好，',
    '',
    '基于岗位「高级架构师（Cloud & AI）」的匹配分析，向您推荐候选人张伟。其在分布式系统建设、微服务架构升级和跨团队技术推进方面具备明确证据，综合匹配度 94.8%。',
    '',
    '核心优势：复杂系统架构经验丰富、技术深度较强、执行落地能力突出。',
    '待验证项：职业稳定性、管理经验深度。',
    '',
    '如需完整简历或安排面试，请告知我方便的时间。',
    '',
    '此致',
    '敬礼',
    'AI Talent Agent',
  ].join('\n'),
};

export interface MatchDisplayExtra {
  avatar: string;
  company: string;
  reasons: string[];
  tags: string[];
}

export const mockMatchExtras: Record<string, MatchDisplayExtra> = {
  'c-001': {
    avatar:
      'https://lh3.googleusercontent.com/aida/AP1WRLuqVcWtoRILIou0Vy6NNPh9-16prkvuj-SwxzRgLEH5oUXaS6qa81-N-pOWiK0LE_NwqsO5cRj3xIkzYElKu9G_M-danPB2YtdJrwpLW_WvB3niHBoSM22eQUk4wWq1kEAoN1KRba2RcE8NMG_hHr45RtscWtslVTee5lHgcswA6OIwIx1Z79vg0P9r0GBbyndoawnhXoUwBeXYxXCFRU8C-G4q-bdgS_HWCV7a3XAQ2Z-eqrABLvhdKw',
    company: '字节跳动',
    reasons: [
      '具备深厚的超大规模集群调度经验，曾主导万级节点迁移项目。',
      '技术栈与JD 100% 契合，擅长 Go / C++ 及 PyTorch 优化。',
      '候选人近期对“云原生+AI”方向有明确求职意愿，与岗位目标高度同步。',
    ],
    tags: ['高活跃', '大厂背景'],
  },
  'c-002': {
    avatar:
      'https://lh3.googleusercontent.com/aida/AP1WRLs_LxjyyIJAFR_y0TwwE06SVyS0qtr51rgdTBWnh7CoVRPTyJ--xsOcj0PO1VjhwQ2-lJ5k7mYNhYroDLOSDqUOnUb51GoksaX-guk0QIW-vDL0hNsTBxmWAesWMSHytDdrufrslI_HSGE2QA5RZLKuBdDez--aphhJJ0-CyMchNE_jJPijURQ5mx5GB7eM4HmJJtkwwpy2JpV02Mo5oP4kHSVqXqctVt1pqgirPgsQP8wZEDYVfJAXcA',
    company: '阿里云',
    reasons: [
      '在分布式系统一致性协议领域有学术突破，发表多篇顶会论文。',
      '深刻理解云原生架构，对于高性能算力分配有独到见解。',
    ],
    tags: ['博士专家', '名校校友'],
  },
};

export const mockMatches: MatchResult[] = [
  {
    candidate: { id: 'c-001', name: '林子杰', title: '资深架构师', location: '上海', experienceYears: 12, education: '清华大学 · 硕士' },
    totalScore: 94,
    breakdown: { experience: 96, skills: 92, profile: 98, careerPreference: 88 },
    reasons: ['具备大型分布式系统经验', '技术栈与 JD 高度匹配', '近期求职意向明确'],
    risks: ['英文口语'],
    interviewQuestions: ['请说明一次分布式系统一致性问题的解决方案。', '请介绍你如何推动跨团队技术方案落地。'],
  },
  {
    candidate: { id: 'c-002', name: '王佳敏', title: '高级技术专家', location: '杭州', experienceYears: 9, education: '浙江大学 · 博士' },
    totalScore: 88,
    breakdown: { experience: 85, skills: 94, profile: 99, careerPreference: 75 },
    reasons: ['系统设计能力突出', '算法与工程结合经验强', '学术背景扎实'],
    risks: ['职涯跳槽频率'],
    interviewQuestions: ['请确认近期到岗时间和岗位方向偏好。', '请说明一次高性能系统优化经历。'],
  },
];
