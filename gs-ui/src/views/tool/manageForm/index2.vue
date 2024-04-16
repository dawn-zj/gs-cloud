<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane
        v-for="(item, index) in tabPaneArr"
        :key="index.toString()"
        :label="item.tabLabel"
        :name="index.toString()"
      >
        <!-- todo 这种注册组件方式，不适合事件注入功能，待动态化       -->
        <form-create
          v-model="item.formData"
          :rule="item.rules"
          :option="item.options"
          @submit="onSubmit"
          @prefix1-change="onChange"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { listTab } from '@/api/tool/tool'
export default {
  data() {
    return {
      activeName: '0',
      tabPaneArr: []
    }
  },
  created() {
    this.tabPaneArr = []
    this.list()
  },
  methods: {
    onSubmit(formData, fapi) {
      this.msgInfo('submit:' + JSON.stringify(formData))
      console.log(fapi)
    },
    onChange(inject) {
      console.log(inject)
      this.msgInfo(`change: ${inject.inject}[${inject.$f.getValue('inputField')}]`)
      this.handleChange(inject.$f.form)
    },
    list() {
      listTab().then(res => {
        const config = res.data.tabPaneConfig
        config.forEach((item, index) => {
          this.tabPaneArr.push(item)

          var event = item.event
          event.forEach((item, index) => {
            this.newFunction(item.funcName, item.funcParams, item.funcScript)
          })
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
