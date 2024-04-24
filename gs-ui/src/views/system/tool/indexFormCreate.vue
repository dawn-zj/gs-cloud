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
      showEdit: false,
      activeName: '0',
      showButton: true,
      buttonText: '修改',
      tabPaneArr: []
    }
  },
  watch: {
    showEdit(val) {
      if (!val) {
        this.$refs.myDesigner.setRule([])
        this.$refs.myDesigner.setOption({})
      }
    }
  },
  created() {
    this.list('formCreate/list.json')
  },
  methods: {
    list(filePath) {
      list(filePath).then(res => {
        this.tabPaneArr = res.data
      })
    },
    handleEditForm(item) {
      this.msgInfo('修改表单')
      this.showEdit = true
      this.$nextTick(() => {
        this.$refs.myDesigner.setRule(item.rule)
        this.$refs.myDesigner.setOption(item.options)
      })
    }
  }
}
</script>

<style scoped>

</style>
