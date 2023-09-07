<template>
  <!--  调用示例：<dialog-component title="Hello" :open.sync="true" />-->
  <el-dialog
    v-if="open"
    :visible.sync="open"
    :title="title"
    :width="width"
    :before-close="cancel"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :destroy-on-close="true"
    :append-to-body="true"
  >
    <slot />
  </el-dialog>

</template>

<script>
export default {
  name: 'DialogComponent',
  props: {
    open: {
      type: Boolean,
      required: true
    },
    title: {
      type: String,
      required: true
    },
    width: {
      type: String,
      default: '600px',
      required: false
    }
  },
  data() {
    return {
    }
  },
  methods: {
    cancel() {
      this.$emit('update:open', false)
    }
  }
}
</script>

<style scoped>

</style>
