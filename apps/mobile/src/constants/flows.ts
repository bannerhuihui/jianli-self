/**
 * 求职者 / HR 双流程的步骤定义。
 *
 * steps 与 routes 数组下标一一对应，供 ProgressSteps 展示与跳转。
 * 修改流程顺序时只需改此文件。
 */
export type FlowDefinition = {
  readonly steps: readonly string[];
  readonly routes: readonly string[];
};

export const HR_FLOW: FlowDefinition = {
  steps: ['岗位需求', '岗位画像', '候选人推荐', '匹配详情', '导出中心'],
  routes: [
    '/pages/hr/job/index',
    '/pages/hr/profile/index',
    '/pages/hr/candidates/index',
    '/pages/hr/match-detail/index',
    '/pages/hr/export/index',
  ],
};

export const CANDIDATE_FLOW: FlowDefinition = {
  steps: ['上传简历', '简历校对', 'AI 访谈', '人才画像', '简历生成'],
  routes: [
    '/pages/candidate/upload/index',
    '/pages/candidate/review/index',
    '/pages/candidate/interview/index',
    '/pages/candidate/profile/index',
    '/pages/candidate/resume/index',
  ],
};
