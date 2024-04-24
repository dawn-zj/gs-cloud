<template>
  <div class="app-container">
    <el-form ref="form" class="form" :model="formData" label-position="left" label-width="68px">
      <el-form-item label="事件">
        <el-select v-model="formData.name" style="width: 100%">
          <el-option
            v-for="item in eventList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <div class="mb20">
      提示：支持使用this.$request向后台发请求，示例：<br>
      this.$request.post('/tool/base64/encode', formData).then(res=>{
      console.log(res)
      })
    </div>
    <codemirror
      ref="myCodeMirror"
      v-model="code"
      :options="cmOptions"
    />
    <div class="el-dialog__footer dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
    </div>
  </div>
</template>
<script>
import { codemirror } from 'vue-codemirror'
// 组件样式
import 'codemirror/lib/codemirror.css'
// 主题
import 'codemirror/theme/eclipse.css'

// 语言模式
import 'codemirror/mode/javascript/javascript.js'
import 'codemirror/mode/css/css.js'
import 'codemirror/mode/xml/xml.js'
// 代码展开折叠
import 'codemirror/addon/fold/foldcode.js'
import 'codemirror/addon/fold/foldgutter.js'
import 'codemirror/addon/fold/foldgutter.css'
import 'codemirror/addon/fold/brace-fold.js'

export default {
  components: { codemirror },
  props: {
    activeRule: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      eventList: [
        { label: 'blur', value: 'blur' },
        { label: 'change', value: 'change' },
        { label: 'click', value: 'click' }
      ],
      cmOptions: {
        mode: 'text/javascript', // 实现javascript代码高亮
        tabSize: 4, // tab的空格宽度
        styleActiveLine: true, // 设置光标所在行高亮true/false
        lineNumbers: true, // 显示行号
        theme: 'eclipse', // 设置主题cobalt/monokai
        json: true,
        readOnly: false, // 设置为只读true/false;也可设置为"nocursor"失去焦点
        lineWrapping: false,
        foldGutter: true,
        gutters: [
          'CodeMirror-lint-markers', // 代码错误检测
          'CodeMirror-linenumbers',
          'CodeMirror-foldgutter' // 展开折叠
        ]
      },
      code: '(formData) => {\n // 在此编写代码\n}',
      formData: {
        name: 'change',
        inject: []
      }
    }
  },
  mounted() {
    var emit = this.activeRule.emit
    if (emit) {
      this.formData.name = emit[0].name
      this.code = emit[0].inject[0]
    }
  },
  methods: {
    submitForm() {
      this.formData.inject[0] = this.code
      this.$emit('success', this.formData)
    }
  }
}
</script>

<style scoped>
</style>
