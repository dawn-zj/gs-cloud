<template>
  <div id="diskChart" />
</template>

<script>
import { getServer } from '@/api/monitor/server'
import echarts from 'echarts'
export default {
  name: 'DiskChart',
  data() {
    return {
      // 实时数据数组
      date: [],
      systemUsage: [],
      userUsage: [],
      $_sidebarElm: null,
      // 折线图echarts初始化选项
      echartsOption: {
        color: ['rgb(0,132,255)', 'rgba(0,132,255,0.7)', 'rgba(0,132,255,0.4)'],
        textStyle: {
          fontFamily: 'Roboto-Regular,Roboto',
          fontWeight: 400,
          color: '#999'
        },
        title: {
          text: '磁盘情况',
          textStyle: {
            fontFamily: 'SourceHanSansSC-Medium,SourceHanSansSC',
            fontSize: 16,
            fontWeight: 500
          }
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
          }
        },
        grid: {
          left: '70',
          top: '82',
          width: '96%',
          show: true,
          backgroundColor: '#FCFCFC',
          borderWidth: '0'
        },
        tooltip: {
          show: true,
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            z: 1,
            shadowStyle: {
              color: 'rgba(229, 242, 255, 0.7)'
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
              result += item.marker + item.seriesName + ' : ' + item.data + 'GB' + '</br>'
            })
            return result
          }
        },
        xAxis: {
          boundaryGap: ['20%', '20%'],
          offset: 20,
          splitLine: {
            show: false
          },
          axisLabel: {
            interval: 0
          },
          axisTick: {
            alignWithLabel: true,
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
          data: []
        },
        yAxis: {
          name: '单位(GB)',
          nameTextStyle: {
            padding: [0, 60, 10, 0],
            align: 'center'
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: '#E5E5E5'
            }
          },
          axisLabel: {
            show: true
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: '#E5E5E5'
            }
          }
        },
        series: [
          {
            name: '总大小',
            type: 'bar',
            barWidth: 50,
            data: []
          },
          {
            name: '可用大小',
            type: 'bar',
            barWidth: 50,
            data: []
          },
          {
            name: '已用大小',
            type: 'bar',
            barWidth: 50,
            data: []
          }
        ]
      }
    }
  },
  mounted() {
    this.initChart()
  },
  methods: {
    getServer() {
      getServer().then(response => {
        var _this = this
        response.data.disks.forEach(function(item) {
          _this.echartsOption.xAxis.data.push(item.dirName)
          _this.echartsOption.series[0].data.push(item.total)
          _this.echartsOption.series[1].data.push(item.free)
          _this.echartsOption.series[2].data.push(item.used)
        })
        this.diskChart.setOption(this.echartsOption)
      })
    },
    initChart() {
      this.diskChart = echarts.init(document.getElementById('diskChart'))	// 初始化echarts
      this.diskChart.setOption(this.echartsOption)
      this.getServer()
      window.addEventListener('resize', () => { this.diskChart.resize() })
      this.$_sidebarElm = document.getElementsByClassName('sidebar-container')[0]
      this.$_sidebarElm && this.$_sidebarElm.addEventListener('transitionend', () => { this.diskChart.resize() })
    }
  }
}
</script>
<style>
  #diskChart{
    width: 100%;
    height: 500px;
    margin: 0 auto;
  }
</style>
