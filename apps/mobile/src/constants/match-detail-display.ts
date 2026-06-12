/**
 * 匹配详情页的展示用静态文案（MVP mock）。
 * 与 mockMatches 中的结构化数据互补：此处偏「设计稿级」叙述与简历预览骨架。
 */

export const MATCH_BREAKDOWN_LEFT = [
  { label: '核心技能', value: 95 },
  { label: '项目经验', value: 88 },
] as const;

export const MATCH_BREAKDOWN_RIGHT = [
  { label: '团队契合', value: 82 },
  { label: '薪资预期', value: 90 },
] as const;

export const MATCH_DETAIL_REASONS = [
  {
    title: '深度后端架构能力：5年+ 高并发分布式系统经验',
    evidence: '在简历中明确提到主导过每秒 10w+ 请求的支付系统重构，且应用了岗位要求的 Kubernetes 部署栈。',
  },
  {
    title: '工具链高度匹配：精通 Go, Redis, Kafka 及 Prometheus',
    evidence: '候选人最近两段大厂经历均以 Go 为主，且在 GitHub 有 500+ Star 的分布式追踪工具贡献。',
  },
  {
    title: '管理潜力：曾带领 10 人研发小组完成 3 个核心项目',
    evidence: '候选人在个人画中表现出清晰的项目推进思路，符合部门储备 TL 的长期规划。',
  },
] as const;

export const MATCH_DETAIL_RISKS = [
  {
    title: '转行跨度稍大',
    desc: '候选人早期经历主要在金融，近期才转向纯互联网。',
  },
  {
    title: '当前状态：在职',
    desc: '到岗时间可能需要 1 个月，无法支持紧急入职。',
  },
] as const;

export const MATCH_INTERVIEW_ITEMS = [
  {
    title: 'Q1: 关于分布式系统一致性',
    text: '“您在 XX 项目中提到使用了 Redis 做分布式锁，请问如何解决 Redlock 算法下的网络分区风险？”',
  },
  {
    title: 'Q2: 关于团队冲突处理',
    text: '“当业务侧提出不合理的技术排期时，您通常如何平衡技术债务与交付进度？”',
  },
  {
    title: 'Q3: 关于个人技术追求',
    text: '“您近期对哪项新兴后端技术（如 eBPF 或 WebAssembly）感兴趣？您的调研深度到了什么程度？”',
  },
] as const;

export const MATCH_SALARY_ESTIMATE = '45k - 55k';

/** 右侧简历预览区的通用模板（候选人姓名动态注入）。 */
export function buildResumePreview(candidateName: string) {
  return {
    title: `${candidateName}的个人简历`,
    contact: '电话：138-xxxx-8888 | 邮箱：zhangsan@talent.ai',
    jobs: [
      {
        company: 'XX 互联网大厂',
        period: '2020.06 - 至今',
        role: '高级后端开发工程师',
        bullets: [
          '负责核心交易系统的分布式架构设计，支撑双十一 5w QPS 峰值。',
          '主导自研 Go-Framework 工具包，减少 30% 业务代码开发量。',
          '负责 Kubernetes 集群维护，优化资源使用率，降低 15% 云服务器成本。',
        ],
      },
      {
        company: 'YY 金融科技有限公司',
        period: '2018.07 - 2020.05',
        role: '后端开发工程师',
      },
    ],
    education: {
      line: '某某重点大学 · 计算机科学与技术',
      period: '2014.09 - 2018.07',
    },
    stack:
      '语言: Go, Java, Python, Shell\n数据库: MySQL, Redis, MongoDB, Elasticsearch\n中间件: Kafka, RabbitMQ, gRPC, Protobuf\n基础设施: Docker, K8s, Prometheus, Grafana',
  };
}
