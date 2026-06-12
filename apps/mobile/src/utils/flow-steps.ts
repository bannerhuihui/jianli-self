import type { FlowDefinition } from '../constants/flows';

/**
 * 生成 ProgressSteps 的绑定 props，避免各流程页重复写 steps / routes / activeIndex。
 */
export function createFlowStepsProps(flow: FlowDefinition, activeIndex: number) {
  return {
    steps: [...flow.steps],
    routes: flow.routes,
    activeIndex,
  };
}
