<template>
  <div class="app-container">
    <h5>实时查看系统日志</h5>
    <div ref="logDiv" class="log" v-html="logText" />
    <el-button type="primary" @click="add()">添加一行</el-button>
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
      timer: ''
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
      list().then((response) => {
        // let reg = /^[\d+][\d+][\d+][\d+]-[\d+][\d+]-[\d+][\d+] [\d+][\d+]:[\d+][\d+]:[\d+][\d+]$/  日期正则
        this.logText += response
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
  width: 80%;
  height: 500px;
  background-color: #cccccc;
  overflow: scroll;
}
</style>
