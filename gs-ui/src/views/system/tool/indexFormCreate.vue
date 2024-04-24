<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="card">
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
      activeName: '0',
      tabPaneArr: []
    }
  },
  created() {
    this.list('formCreate/list.json')
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
