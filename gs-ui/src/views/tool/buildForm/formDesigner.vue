<template>
  <div class="app-container">
    <div class="mb20">
      <el-button @click="handleImportRule">导入json</el-button>
      <el-button @click="handleImportOption">导入option</el-button>
      <el-button type="success" @click="handleGenRule">生成json</el-button>
      <el-button type="success" @click="handleGenOption">生成option</el-button>
      <el-button type="primary" @click="handleEvent">绑定事件</el-button>
      <el-button type="success" @click="handleView">预览</el-button>
      <el-button type="primary" @click="handleSave">完成</el-button>
    </div>

    <fc-designer ref="designer" />

    <dialog-component v-if="showView" title="预览" :open.sync="showView">
      <form-view :data="egData" />
    </dialog-component>

    <dialog-component v-if="showEvent" title="绑定事件" :open.sync="showEvent">
      <gen-func :active-event="activeEvent" @success="setEvent" />
    </dialog-component>

    <dialog-component v-if="showRule" title="生成数据" :open.sync="showRule">
      <code-view-component
        :code="value"
        :read-only="true"
        mode="application/json"
      />
    </dialog-component>

    <dialog-component v-if="importRule" title="导入json" :open.sync="importRule">
      <code-view-component
        mode="application/json"
        @change="setRule"
      />
      <div class="el-dialog__footer dialog-footer">
        <el-button type="primary" @click="()=>{return this.importRule = false}">确 定</el-button>
      </div>
    </dialog-component>

    <dialog-component v-if="importOption" title="导入option" :open.sync="importOption">
      <code-view-component
        mode="application/json"
        @change="setOption"
      />
      <div class="el-dialog__footer dialog-footer">
        <el-button type="primary" @click="()=>{return this.importOption = false}">确 定</el-button>
      </div>
    </dialog-component>
  </div>
</template>

<script>
import DialogComponent from '@/views/components/DialogComponent'
import CodeViewComponent from '@/views/components/CodeViewComponent'
import FormView from '@/views/tool/buildForm/formView'
import GenFunc from '@/views/tool/buildForm/genFunc'
import link from '@/views/tool/buildForm/rules/link'

export default {
  components: { GenFunc, FormView, DialogComponent, CodeViewComponent },
  data() {
    return {
      importRule: false,
      importOption: false,
      showRule: false,
      showEvent: false,
      showView: false,
      value: '',
      egData: {
        // 表单默认值，也作为双向绑定的表单数据
        formData: {},
        rule: [],
        options: {}
      },
      activeEvent: {
        name: '',
        inject: ['']
      }
    }
  },
  watch: {
  },
  created() {
    this.$nextTick(() => {
      console.log(this.$refs.designer)
      this.$refs.designer.addMenu({
        title: '自定义分组',
        name: 'group1',
        list: [link]
      })
      // 插入组件规则
      this.$refs.designer.addComponent(link)
    })
  },
  methods: {
    getValue() {
      // 拷贝一份，以防预览页面的测试数据被记录
      this.egData.rule = JSON.parse(JSON.stringify(this.$refs.designer.getRule()))
      this.egData.options = JSON.parse(JSON.stringify(this.$refs.designer.getOption()))
    },
    setRule(newCode) {
      this.$refs.designer.setRule(newCode)
    },
    setOption(newCode) {
      this.$refs.designer.setOption(newCode)
    },
    handleImportRule() {
      this.importRule = true
    },
    handleImportOption() {
      this.importOption = true
    },
    handleGenRule() {
      this.getValue()
      this.value = this.egData.rule
      this.showRule = true
    },
    handleGenOption() {
      this.getValue()
      this.value = this.egData.options
      this.showRule = true
    },
    handleView() {
      this.getValue()
      this.showView = true
    },
    handleEvent() {
      this.activeEvent.name = 'change'
      this.activeEvent.inject = ['']
      this.getValue()
      var activeRule = this.$refs.designer._self.activeRule
      if (activeRule == null || activeRule.type === 'FcRow' || activeRule.type === 'col') {
        this.activeEvent.name = 'submit'
        this.activeEvent.inject = [this.egData.options.submitEvent]
      } else {
        var emit = activeRule.emit
        if (emit) {
          this.activeEvent.name = emit[0].name
          this.activeEvent.inject = emit[0].inject
        }
      }
      this.showEvent = true
    },
    setEvent(obj) {
      if (obj.name === 'submit') {
        var options = JSON.parse(JSON.stringify(this.$refs.designer.getOption()))
        options.submitEvent = obj.inject[0]
        console.log(options)
        this.$refs.designer.setOption(options)
      } else {
        // 用户手动输入事件脚本后，回传到最终数据里
        var rule = this.$refs.designer.getRule()
        var activeRule = this.$refs.designer._self.activeRule
        // 遍历找到当前选中的表单组件，赋值
        var firstElement = rule[0]
        if (firstElement.type === 'FcRow') {
          var cols = firstElement.children
          cols.forEach(col => {
            var formItem = col.children
            if (formItem.field === activeRule.field) {
              formItem.emitPrefix = 'gs'
              formItem.emit = []
              formItem.emit.push(obj)
            }
          })
        } else {
          rule.forEach(formItem => {
            if (formItem.field === activeRule.field) {
              formItem.emitPrefix = 'gs'
              formItem.emit = []
              formItem.emit.push(obj)
            }
          })
        }

        this.$refs.designer.setRule(rule)
      }
      this.msgSuccess('添加成功')
      this.showEvent = false
    },
    handleSave() {
      this.getValue()
      this.$emit('save', JSON.parse(JSON.stringify(this.egData)))
    }
  }
}
</script>

<style scoped>
::v-deep .el-aside {
  background-color: white !important;
}
</style>
