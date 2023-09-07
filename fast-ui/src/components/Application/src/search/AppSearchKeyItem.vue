<template>
  <span :class="$attrs.class">
    <Icon :icon="icon" />
  </span>
</template>
<script lang="ts" setup>
  import Icon from '@/components/Icon/index';

  defineProps({
    icon: String,
  });
</script>
