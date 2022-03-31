<template>
  <div id="cpuChart" style="width:100%" />
</template>

<script>
import { getCpu } from '@/api/monitor/server'
import echarts from 'echarts'
export default {
  name: 'CpuChart',
  data() {
    return {
      timer: null,
      // 实时数据数组
      date: [],
      systemUsage: [],
      userUsage: [],
      systemData: '',
      userData: '',
      $_sidebarElm: null,
      // 折线图echarts初始化选项
      echartsOption: {
        color: ['rgb(0,132,255)', 'rgba(0,132,255,0.5)'],
        textStyle: {
          fontFamily: 'Roboto-Regular,Roboto',
          fontWeight: 400,
          color: '#999'
        },
        title: {
          text: 'CPU状况',
          textStyle: {
            fontFamily: 'SourceHanSansSC-Medium,SourceHanSansSC',
            fontSize: 16,
            fontWeight: 500
          }
        },
        grid: {
          top: '90',
          left: '70',
          right: '80',
          show: true,
          backgroundColor: '#FCFCFC',
          borderWidth: '0'
        },
        legend: {
          right: '0',
          icon: 'circle',
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 20,
          textStyle: {
            fontSize: 15,
            color: '#999'
          },
          data: ['系统使用率', '用户使用率']
        },
        xAxis: {
          offset: 20,
          axisTick: {
            show: true,
            lineStyle: {
              color: '#ccc'
            }
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: '#ccc'
            }
          },
          name: '时间',
          nameTextStyle: {
            padding: 35
          },
          axisLabel: {
            interval: 0 // 间隔的个数
          },
          type: 'category',
          boundaryGap: false,
          data: this.date	// 绑定实时数据数组
        },
        yAxis: [
          {
            name: '系统使用率（%）',
            type: 'value',
            offset: 20,
            nameTextStyle: {
              padding: 20
            },
            axisLine: {
              show: false
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#E5E5E5'
              }
            },
            scale: true,
            boundaryGap: [0, '100%'],
            max: 100,
            min: 0,
            axisLabel: {
              interval: 'auto'
            },
            axisTick: {
              show: false
            }
          },
          {
            name: '用户使用率（%）',
            type: 'value',
            offset: 20,
            nameTextStyle: {
              padding: 20
            },
            axisLine: {
              show: false
            },
            splitLine: {
              show: true,
              lineStyle: {
                color: '#E5E5E5'
              }
            },
            scale: true,
            boundaryGap: [0, '100%'],
            max: 100,
            min: 0,
            axisLabel: {
              interval: 'auto'
            },
            axisTick: {
              show: false
            }
          }
        ],
        tooltip: {
          show: true,
          trigger: 'axis',
          axisPointer: {
            type: 'line',
            lineStyle: {
              color: '#ccc'
            }
          },
          backgroundColor: '#fff',
          extraCssText: 'box-shadow:0px 0px 8px 0px rgba(0,132,255,0.2);',
          padding: [14, 20],
          textStyle: {
            color: '#999',
            fontSize: 14,
            lineHeight: 30
          },
          formatter: function(params) {
            var result = ''
            result += '<div>' + params[0].name + '<div/>'
            params.forEach(function(item) {
              result += item.marker + item.seriesName + ' : ' + item.data + '%' + '</br>'
            })
            return result
          }
        },
        series: [
          {
            name: '系统使用率',
            type: 'line',
            smooth: true,
            yAxisIndex: 0,
            data: this.systemUsage	// 绑定实时数据数组
          },
          {
            name: '用户使用率',
            type: 'line',
            smooth: true,
            yAxisIndex: 1,
            data: this.userUsage	// 绑定实时数据数组
          }
        ]
      }
    }
  },
  created() {
    this.addData()
  },
  mounted() {
    this.initChart()
  },
  destroyed() {
    // 页面关闭时清除定时器
    clearInterval(this.timer)
  },
  methods: {
    // 获取当前时间
    getTime() {
      var ts = arguments[0] || 0
      var t, h, i, s
      t = ts ? new Date(ts * 1000) : new Date()
      h = t.getHours()
      i = t.getMinutes()
      s = t.getSeconds()
      // 定义时间格式
      return (h < 10 ? '0' + h : h) + ':' + (i < 10 ? '0' + i : i) + ':' + (s < 10 ? '0' + s : s)
    },
    getCpu() {
      getCpu().then(response => {
        this.systemData = response.data.sys
        this.userData = response.data.used
        this.systemUsage.push(this.systemData)
        this.userUsage.push(this.userData)
        this.date.push(this.getTime(Math.round(new Date().getTime() / 1000)))
        // 重新将数组赋值给echarts选项
        this.echartsOption.xAxis.data = this.date
        this.echartsOption.series[0].data = this.systemUsage
        this.echartsOption.series[1].data = this.userUsage
        this.cpuChart.setOption(this.echartsOption)
      })
    },
    // 添加实时数据
    addData() {
      // 从接口获取数据并添加到数组
      if (this.systemUsage.length > 9) {
        this.systemUsage.shift()
        this.userUsage.shift()
        this.date.shift()
      }
      this.getCpu()
    },
    initChart() {
      this.cpuChart = echarts.init(document.getElementById('cpuChart'))	// 初始化echarts
      this.cpuChart.setOption(this.echartsOption)
      window.addEventListener('resize', () => { this.cpuChart.resize() })
      this.$_sidebarElm = document.getElementsByClassName('sidebar-container')[0]
      this.$_sidebarElm && this.$_sidebarElm.addEventListener('transitionend', () => { this.cpuChart.resize() })
      this.timer = setInterval(this.addData, 5000)
    }
  }
}
</script>
<style>
  #cpuChart{
    width: 100%;
    height: 500px;
    margin: 0 auto;
  }
</style>
