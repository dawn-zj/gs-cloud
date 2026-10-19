<template>
  <div>
    <codemirror
      ref="myCodeMirror"
      v-model="value"
      :options="cmOptions"
      @input="handleChange"
    />
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
  name: 'DialogComponent',
  components: { codemirror },
  props: {
    code: {
      required: false
    },
    mode: {
      type: String,
      required: false,
      default: 'text/javascript'
    },
    readOnly: {
      type: Boolean,
      required: false,
      default: false
    },
    options: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      value: {},
      cmOptions: {
        // autoRefresh: true, // 重点是这句，为true
        // value: '', // 初始内容
        mode: this.mode, // 实现javascript代码高亮
        tabSize: 4, // tab的空格宽度
        styleActiveLine: true, // 设置光标所在行高亮true/false
        lineNumbers: true, // 显示行号
        theme: 'eclipse', // 设置主题cobalt/monokai
        json: true,
        readOnly: this.readOnly, // 设置为只读true/false;也可设置为"nocursor"失去焦点
        lineWrapping: false,
        foldGutter: true,
        gutters: [
          'CodeMirror-lint-markers', // 代码错误检测
          'CodeMirror-linenumbers',
          'CodeMirror-foldgutter' // 展开折叠
        ]
      }
    }
  },
  created() {
    if (this.mode === 'application/json') {
      this.value = JSON.stringify(this.code, null, 2)
    } else {
      this.value = this.code
    }
  },
  methods: {
    handleChange(newCode) {
      this.$emit('change', newCode)
    }

  }
}
</script>

<style scoped>

</style>
