<template>
  <div class="app-container">
    <form-designer @save="handleSave" />
  </div>
</template>

<script>
import FormDesigner from '@/views/tool/buildForm/formDesigner'
import { add } from '@/api/tool/tool'
export default {
  components: { FormDesigner },
  data() {
    return {
    }
  },
  watch: {
  },
  created() {
  },
  methods: {
    handleSave(data) {
      this.$prompt('请输入Tab标签页标题', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        data.tabLabel = value
        data.visiable = 0
        data.formData = {}
        add(data).then(res => {
          this.msgSuccess('发布成功')
        })
      }).catch(() => {
      })
    }
  }
}
</script>

<style scoped>

</style>
