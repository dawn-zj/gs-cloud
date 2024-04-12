<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>{{ title }}</span>
      <el-button v-if="showButton" style="float: right; padding: 3px 0" type="text" @click="handleEvent('function')">{{ buttonText }}</el-button>
    </div>
    <slot />
  </el-card>
</template>

<script>
export default {
  name: 'Card',
  props: {
    title: {
      type: String,
      default: '卡片',
      required: false
    },
    width: {
      type: String,
      default: '600px',
      required: false
    },
    showButton: {
      type: Boolean,
      default: false,
      required: false
    },
    buttonText: {
      type: String,
      default: '操作按钮',
      required: false
    }
  },
  methods: {
    handleEvent(functionName) {
      this.$emit(functionName)
    }
  }
}
</script>

<style>
.el-card__header {
  border-radius: 4px;
  border-left: 5px solid #50bfff;
  border-bottom: 0px;
  margin: 20px 0px 0px 10px;
}
</style>
