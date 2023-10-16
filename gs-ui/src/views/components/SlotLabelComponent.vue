<template>
  <div>
    <span>{{ label }}</span>
    <el-tooltip class="item" effect="dark" placement="top">
      <!--  问号的图标   -->
      <i class="el-icon-question" style="font-size: 14px; vertical-align: middle;" />
      <!--  提示的内容 -->
      <div slot="content">
        {{ tip }}
      </div>
    </el-tooltip>
  </div>
</template>

<script>
export default {
  name: 'SlotLabelComponent',

  props: {
    label: {
      type: String,
      default: 'label',
      required: false
    },
    tip: {
      type: String,
      default: '内容提示',
      required: false
    }
  }
}
</script>

<style scoped>

</style>
