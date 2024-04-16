<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane
        v-for="(item, index) in tabPaneArr"
        :key="index.toString()"
        :label="item.tabLabel"
        :name="index.toString()"
      >
        <form-create
          v-model="item.formData"
          :rule="item.rules"
          :option="item.options"
          @submit="onSubmit"
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
      console.log('submit事件')
      console.log(formData)
      console.log(fapi)
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
