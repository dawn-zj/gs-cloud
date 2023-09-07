<template>
  <div>
    <div v-for="(item,index) in gsConfig" :key="index" class="mb10">
      {{ index + 1 }}.{{ item.describe }}：<el-link :href="item.url" target="_blank">{{ item.url }}</el-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LinkProject',
  data() {
    return {
      gsConfig: []
    }
  },
  created() {
    this.gsConfig = window.gs.urlConfig
  }
}
</script>

<style scoped>

</style>
