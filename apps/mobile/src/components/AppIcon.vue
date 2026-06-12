<template>
  <text
    class="app-icon"
    :class="{ 'material-symbols-outlined': useMaterialIcon }"
    :style="iconStyle"
  >{{ displayGlyph }}</text>
</template>

<script setup lang="ts">
/**
 * 跨平台图标：H5 使用 Material Symbols，小程序等回退到 glyph 映射。
 * 新增图标时同时更新 materialIconNames 与 iconMap。
 */
import { computed } from 'vue';

const props = defineProps<{
  name: string;
  size?: number;
  color?: string;
  filled?: boolean;
}>();

const isH5 = process.env.UNI_PLATFORM === 'h5';

const materialIconNames = new Set([
  'psychology',
  'code',
  'forum',
  'grid_view',
  'chat_bubble',
  'table_chart',
  'picture_as_pdf',
  'analytics',
  'interpreter_mode',
  'radar',
  'person_search',
  'auto_stories',
  'language',
  'help_outline',
  'notifications',
  'settings',
  'upload_file',
  'warning',
  'check_circle',
  'data_object',
  'security',
  'auto_awesome',
  'zoom_in',
  'zoom_out',
  'school',
  'work',
  'edit',
  'close',
  'article',
  'download',
  'person',
  'format_list_numbered',
  'smart_toy',
  'send',
  'track_changes',
  'search_insights',
  'add_circle',
  'quiz',
  'mic',
  'mic_off',
  'arrow_forward',
  'notes',
  'content_copy',
  'description',
  'terminal',
  'palette',
  'chevron_right',
  'fullscreen',
  'info',
  'mail',
  'loop',
  'tips_and_updates',
  'preview',
  'inventory_2',
  'groups',
  'assignment',
  'explore',
  'star',
  'location_on',
  'payments',
  'verified',
  'add_box',
  'chevron_left',
  'add',
  'arrow_back',
  'ios_share',
  'open_in_new',
  'chat',
  'history',
  'cloud_upload',
  'unarchive',
  'table_view',
  'checklist',
  'drag_indicator',
  'visibility',
  'share',
]);

const iconAliases: Record<string, string> = {
  arrowleft: 'arrow-left',
  arrowright: 'arrow-right',
  chart: 'bars',
  table: 'list',
  file: 'paperclip',
  work: 'staff',
};

const resolvedName = computed(() => {
  if (materialIconNames.has(props.name)) return props.name;
  return iconAliases[props.name] || props.name;
});

const useMaterialIcon = computed(() => isH5 && materialIconNames.has(resolvedName.value));

const iconMap: Record<string, string> = {
  'arrow-left': '←',
  'arrow-right': '→',
  arrow_back: '←',
  arrow_forward: '→',
  add: '+',
  add_box: '▣',
  add_circle: '+',
  analytics: '▤',
  assignment: '≡',
  auto_awesome: '✦',
  auto_stories: '▱',
  back: '←',
  bell: '◌',
  bars: '▤',
  calendar: '□',
  chat: '◌',
  chevron_left: '‹',
  chevron_right: '›',
  content_copy: '⎘',
  checklist: '☑',
  chat_bubble: '◌',
  chatboxes: '◌',
  checkbox: '□',
  'checkbox-filled': '☑',
  article: '▱',
  check_circle: '✓',
  checkmarkempty: '✓',
  close: '×',
  edit: '✎',
  explore: '◎',
  circle: '○',
  'circle-filled': '●',
  code: '</>',
  cloud_upload: '↑',
  'cloud-upload': '↑',
  compose: '✎',
  contact: '◯',
  data_object: '{ }',
  description: '▤',
  drag_indicator: '⋮',
  download: '↓',
  fullscreen: '□',
  email: '✉',
  eye: '◉',
  flag: '⚑',
  font: 'Aa',
  format_list_numbered: '≡',
  forum: '◫',
  groups: '◯◯',
  history: '↻',
  gear: '⚙',
  grid_view: '▦',
  help_outline: '?',
  home: '⌂',
  info: 'i',
  interpreter_mode: '◎',
  inventory_2: '▣',
  ios_share: '↗',
  language: '文',
  link: '↗',
  list: '▦',
  location_on: '⌖',
  locked: '◆',
  loop: '↻',
  mail: '✉',
  palette: '◐',
  mic: '◌',
  mic_off: '⊘',
  notes: '▤',
  open_in_new: '↗',
  quiz: '?',
  map: '◇',
  notifications: '◌',
  paperclip: '▱',
  payments: '¤',
  paperplane: '➤',
  person: '◯',
  person_search: '⌕',
  'person-filled': '●',
  picture_as_pdf: 'PDF',
  plus: '+',
  preview: '◫',
  psychology: 'Ψ',
  radar: '◎',
  search: '⌕',
  search_insights: '⌕',
  send: '➤',
  smart_toy: '◎',
  track_changes: '◎',
  security: '◆',
  share: '↗',
  settings: '⚙',
  staff: '◯',
  star: '☆',
  'star-filled': '★',
  table_chart: '▤',
  table_view: '▦',
  unarchive: '▣',
  terminal: '>_',
  tips_and_updates: '✦',
  upload: '↑',
  upload_file: '↑',
  visibility: '◉',
  verified: '✓',
  warning: '!',
  work: '◯',
  zoom_in: '+',
  zoom_out: '−',
  school: '▣',
};

const displayGlyph = computed(() => {
  if (useMaterialIcon.value) return resolvedName.value;
  return iconMap[resolvedName.value] || '•';
});

const iconStyle = computed(() => ({
  fontSize: `${props.size || 20}px`,
  color: props.color || 'currentColor',
  fontVariationSettings: props.filled && useMaterialIcon.value ? "'FILL' 1" : undefined,
}));
</script>

<style lang="scss" scoped>
.app-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 1em;
  line-height: 1;
  font-weight: 900;
}
.app-icon.material-symbols-outlined {
  font-weight: normal;
}
</style>
