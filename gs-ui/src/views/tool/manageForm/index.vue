<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="card" @edit="handleTabsEdit">
      <el-tab-pane
        v-for="(item, index) in tabPaneArr"
        :key="index.toString()"
        :label="item.tabLabel"
        :name="index.toString()"
      >
        <div v-if="activeName == index">
          <el-row v-for="(row, index) in item.row" :key="index" class="mb20" :gutter="20">
            <el-col v-for="(col, index) in row.col" :key="index" :span="col.span" :offset="col.offset">
              <card-component
                :title="col.cardTitle"
                :show-button="col.showButton"
                :button-text="col.buttonText"
                @function="handleEditForm(item)"
              >
                <from-create-component :data="col.data" />
              </card-component>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import CardComponent from '@/views/components/CardComponent'
import FromCreateComponent from '@/views/components/FromCreateComponent'
export default {
  components: { CardComponent, FromCreateComponent },
  data() {
    return {
      activeName: '0',
      egTab: {
        tabLabel: '标签页示例',
        row: [{
          col: [{ span: 24, cardTitle: '功能示例',
            data: [
              {
                type: 'input',
                field: 'f1',
                title: '输入框1',
                props: {
                  type: 'text'
                },
                info: '',
                value: '111',
                hidden: false,
                display: false
              },
              {
                type: 'button',
                title: '按钮',
                btnScript: 'this.msgInfo(\'按钮2\')\n' +
                  '      console.log(this.formData)',
                hidden: false,
                display: false
              }
            ]
          }]
        }]
      },
      tabPaneArr: []
    }
  },
  created() {
    const config = window.gs.tabPaneConfig
    this.tabPaneArr = []
    this.tabPaneArr.push(JSON.parse(JSON.stringify(this.egTab)))

    config.forEach((item, index) => {
      this.tabPaneArr.push(item)
    })
  },
  methods: {
    handleTabsEdit(targetName, action) {
      if (action === 'add') {
        const newTabName = this.tabPaneArr.length + ''
        this.tabPaneArr.push({
          tabLabel: 'New Tab',
          name: newTabName,
          row: [{
            col: [{ span: 24, cardTitle: '功能示例', showButton: true, buttonText: '修改表单', data: [] }]
          }]
        })
        this.activeName = newTabName
      }
      if (action === 'remove') {
        const tabs = this.tabPaneArr
        let activeName = this.activeName
        if (activeName == '0') {
          this.msgInfo('示例数据，不可删除')
          return
        }
        if (activeName === targetName) {
          tabs.forEach((tab, index) => {
            if (index == targetName) {
              const nextTab = tabs[index + 1]
              if (nextTab) {
                activeName = index + ''
              } else {
                activeName = index - 1 + ''
              }
            }
          })
        }

        this.tabPaneArr = tabs.filter((tab, index) => index != targetName)
        this.activeName = activeName
      }
    },
    handleEditForm(item) {
      this.msgInfo('修改表单')
      console.log(item)
    }
  }
}
</script>

<style scoped>

</style>
