<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>服务器信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <tbody>
                <tr>
                  <td><div class="cell">服务器名称</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.serverName }}</div></td>
                  <td><div class="cell">操作系统</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.osName }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">服务器IP</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.serverIp }}</div></td>
                  <td><div class="cell">系统架构</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.osArch }}</div></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span>Java虚拟机信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <tbody>
                <tr>
                  <td><div class="cell">Java名称</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.jvmName }}</div></td>
                  <td><div class="cell">Java版本</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.javaVersion }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">安装路径</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.javaHome }}</div></td>
                  <td><div class="cell">启动时间</div></td>
                  <td><div v-if="server.server" class="cell">{{ parseTime(server.server.jvmStartTime) }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">项目路径</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.userDir }}</div></td>
                  <td><div class="cell">运行时长</div></td>
                  <td><div v-if="server.server" class="cell">{{ server.server.jvmRunTime }}</div></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>CPU信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <tbody>
                <tr>
                  <td><div class="cell">CPU核数</div></td>
                  <td><div v-if="server.cpu" class="cell">{{ server.cpu.cpuNum }}</div></td>
                </tr>
                <tr>
                  <td><div class="cell">系统使用率</div></td>
                  <td><div v-if="server.cpu" class="cell">{{ server.cpu.sys }}%</div></td>
                </tr>
                <tr>
                  <td><div class="cell">用户使用率</div></td>
                  <td><div v-if="server.cpu" class="cell">{{ server.cpu.used }}%</div></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span>内存信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%;">
              <tbody>
                <tr>
                  <td><div class="cell">总内存</div></td>
                  <td><div v-if="server.memory" class="cell">{{ server.memory.total }}GB</div></td>
                </tr>
                <tr>
                  <td><div class="cell">已用内存</div></td>
                  <td><div v-if="server.memory" class="cell">{{ server.memory.free }}GB</div></td>
                </tr>
                <tr>
                  <td><div class="cell">剩余内存</div></td>
                  <td><div v-if="server.memory" class="cell">{{ server.memory.used }}GB</div></td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <!--          <diskChart />-->
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script>
import { getServer } from '@/api/monitor/server'
import cpuChart from './cpuChart'
import diskChart from './diskChart'
import memoryChart from './memoryChart'

export default {
  components: { cpuChart, diskChart, memoryChart },
  data() {
    return {
      // 加载层信息
      loading: [],
      // 服务器信息
      server: []
    }
  },
  created() {
    this.getList()
    this.openLoading()
  },
  methods: {
    /** 查询服务器信息 */
    getList() {
      getServer().then(response => {
        this.server = response.data
        this.loading.close()
      })
    },
    // 打开加载层
    openLoading() {
      this.loading = this.$loading({
        lock: true,
        text: '拼命读取中',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
  }
}
</script>
