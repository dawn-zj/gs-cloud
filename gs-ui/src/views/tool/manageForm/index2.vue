<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane
        v-for="(item, index) in tabPaneArr"
        :key="index.toString()"
        :label="item.tabLabel"
        :name="index.toString()"
      >
        <!-- 自定义注入事件，名称固定：gs-change,gs-blur等，具体脚本通过inject指定       -->
        <form-create
          v-model="item.formData"
          :value.sync="item.formData"
          :rule="item.rules"
          :option="item.options"
          @submit="onSubmit"
          @gs-change="onChange"
          @gs-blur="onBlur"
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
      // eslint-disable-next-line no-eval
      const func = eval(fapi.options.submitEvent)
      func(formData)
    },
    onChange(inject, val) {
      // 具体脚本通过inject指定，格式为 "inject": [{"change":"(data, formData)=>{// 自定义}"}]
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0].change)
      func(inject.self.value, inject.$f.form)
    },
    onBlur(inject) {
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0].blur)
      func(inject.self.value, inject.$f.form)
    },
    list() {
      listTab().then(res => {
        const config = res.data.tabPaneConfig
        config.forEach((item, index) => {
          this.tabPaneArr.push(item)
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
