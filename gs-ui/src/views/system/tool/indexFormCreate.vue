<template>
  <div class="app-container">
    <el-radio-group v-model="tabPosition" class="mb30">
      <el-radio-button label="top">top</el-radio-button>
      <el-radio-button label="right">right</el-radio-button>
      <el-radio-button label="bottom">bottom</el-radio-button>
      <el-radio-button label="left">left</el-radio-button>
    </el-radio-group>
    <el-tabs v-model="activeName" :tab-position="tabPosition" class="mt20">
      <el-tab-pane
        v-for="(item, index) in tabPaneArr"
        :key="index.toString()"
        :label="item.tabLabel"
        :name="index.toString()"
      >
        <div v-if="activeName == index">
          <card-component
            :title="item.tabLabel"
          >
            <form-view :data="item" />
          </card-component>
        </div>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import CardComponent from '@/views/components/CardComponent'
import FormView from '@/views/tool/buildForm/formView'
import { list } from '@/api/tool/tool'
export default {
  components: { FormView, CardComponent },
  data() {
    return {
      tabPosition: 'top',
      activeName: '0',
      tabPaneArr: []
    }
  },
  created() {
    this.list()
  },
  methods: {
    list(filePath) {
      list(filePath).then(res => {
        var data = res.data
        this.tabPaneArr = []
        data.forEach(item => {
          if (item.visiable === 1) {
            this.tabPaneArr.push(item)
          }
        })
      })
    }
  }
}
</script>

<style scoped>

</style>
