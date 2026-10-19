<template>
  <div class="app-container">
    <h5>实时查看系统日志</h5>
    <div class="mb20">
      <el-radio v-model="level" label="info">info</el-radio>
      <el-radio v-model="level" label="error">error</el-radio>
      <el-radio v-model="level" label="debug">debug</el-radio>
    </div>
    <div ref="logDiv" class="log" v-html="logText" />
    <!--    <el-button type="primary" @click="add()">添加一行</el-button>-->
  </div>

</template>

<script>
import { list } from '@/api/log/system'
import { parseTime } from '@/utils/ruoyi'

export default {
  // 框架使然，写name会有缓存，所以不写name
  data() {
    return {
      logText: '',
      timer: '',
      level: 'error'
    }
  },
  watch: {
    logText() {
      this.$nextTick(function() {
        const div = this.$refs.logDiv
        div.scrollTop = div.scrollHeight
      })
    }
  },
  created() {
    this.getSystemLog()
    this.timer = setInterval(this.getSystemLog, 2000)
  },
  beforeDestroy() {
    // 关闭窗口后清除定时器
    clearInterval(this.timer)
  },
  methods: {
    getSystemLog() {
      list(this.level).then((response) => {
        this.logText = response.data.replace(/\n/g, '<br>')
      })
    },
    add() {
      const str = parseTime(new Date()) + ' [test] INFO 添加一行'
      this.logText += str + '<br>'
    }
  }

}
</script>

<style scoped>
.log{
  width: 100%;
  height: 500px;
  background-color: #f7f7f7;
  color: rgba(0, 0, 0, 0.7470588235294118);
  overflow: scroll;
}
</style>
