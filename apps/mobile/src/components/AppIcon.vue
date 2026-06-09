<template>
  <text class="app-icon" :style="{ fontSize: `${size || 20}px`, color: color || 'currentColor' }">{{ iconGlyph }}</text>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = defineProps<{
  name: string;
  size?: number;
  color?: string;
}>();

const iconAliases: Record<string, string> = {
  arrowleft: 'arrow-left',
  arrowright: 'arrow-right',
  chart: 'bars',
  analytics: 'bars',
  table: 'list',
  file: 'paperclip',
  work: 'staff',
};

const iconName = computed(() => iconAliases[props.name] || props.name);

const iconMap: Record<string, string> = {
  'arrow-left': '←',
  'arrow-right': '→',
  back: '←',
  bars: '▤',
  calendar: '□',
  chat: '◌',
  chatboxes: '◌',
  checkbox: '□',
  'checkbox-filled': '☑',
  checkmarkempty: '✓',
  circle: '○',
  'circle-filled': '●',
  'cloud-upload': '↑',
  compose: '✎',
  contact: '◯',
  download: '↓',
  email: '✉',
  eye: '◉',
  flag: '⚑',
  font: 'Aa',
  gear: '⚙',
  home: '⌂',
  info: 'i',
  link: '↗',
  list: '▦',
  locked: '◆',
  loop: '↻',
  map: '◇',
  paperclip: '▱',
  paperplane: '➤',
  person: '◯',
  'person-filled': '●',
  plus: '+',
  search: '⌕',
  staff: '◯',
  star: '☆',
  'star-filled': '★',
  upload: '↑',
};

const iconGlyph = computed(() => iconMap[iconName.value] || '•');
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
</style>
