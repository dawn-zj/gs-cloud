<template>
  <div id="memoryChart" />
</template>

<script>
import { getServer } from '@/api/monitor/server'
import echarts from 'echarts'
export default {
  name: 'MemoryChart',
  data() {
    return {
      // 实时数据数组
      memData: [],
      jvmData: [],
      date: [],
      systemUsage: [],
      userUsage: [],
      $_sidebarElm: null,
      // 折线图echarts初始化选项
      echartsOption: {
        backgroundColor: '#fff',
        textStyle: {
          fontFamily: 'Roboto-Regular,Roboto',
          fontWeight: 400,
          color: '#999'
        },
        color: ['rgba(0,132,255,0.5)', 'rgb(0,132,255)'],
        title: [{
          text: '内存信息',
          textStyle: {
            fontFamily: 'SourceHanSansSC-Medium,SourceHanSansSC',
            fontSize: 16,
            fontWeight: 500
          }
        }, {
          text: '内存',
          left: '22%',
          top: '20%',
          textStyle: {
            fontFamily: 'SourceHanSansSC-Medium,SourceHanSansSC',
            fontSize: 16,
            fontWeight: 500
          }
        }, {
          text: 'JVM',
          left: '72%',
          top: '20%',
          textStyle: {
            fontFamily: 'SourceHanSansSC-Medium,SourceHanSansSC',
            fontSize: 16,
            fontWeight: 500
          }
        }],
        tooltip: {
          trigger: 'item',
          show: true,
          axisPointer: {
            type: 'shadow',
            z: 1,
            shadowStyle: {
              color: 'rgba(229, 242, 255, 1)'
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
            if (params.seriesName === '内存') { var result = '<div>' + params.seriesName + '<div/>' + params.marker + params.data.name + ' : ' + params.data.value + 'GB' + '  (' + params.percent + '%)' }
            if (params.seriesName === 'JVM') { var result = '<div>' + params.seriesName + '<div/>' + params.marker + params.data.name + ' : ' + params.data.value + 'MB' + '  (' + params.percent + '%)' }
            return result
          }
        },
        legend: {
          right: '0',
          top: '48',
          icon: 'circle',
          itemWidth: 8,
          itemHeight: 8,
          itemGap: 20,
          textStyle: {
            fontSize: 15,
            color: '#999'
          }
        },
        series: [
          {
            name: '内存',
            type: 'pie',
            radius: '40%',
            center: ['25%', '50%'],
            data: [],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          },
          {
            name: 'JVM',
            type: 'pie',
            radius: '40%',
            center: ['75%', '50%'],
            data: [],
            itemStyle: {
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
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
        this.echartsOption.series[0].data = [
          { name: '剩余内存', value: response.data.mem.free },
          { name: '已用内存', value: response.data.mem.used }
        ]
        this.echartsOption.series[1].data = [
          { name: '剩余内存', value: response.data.jvm.free },
          { name: '已用内存', value: response.data.jvm.used }
        ]
        this.memoryChart.setOption(this.echartsOption)
      })
    },
    initChart() {
      this.memoryChart = echarts.init(document.getElementById('memoryChart'))	// 初始化echarts
      this.memoryChart.setOption(this.echartsOption)
      this.getServer()
      window.addEventListener('resize', () => { this.memoryChart.resize() })
      this.$_sidebarElm = document.getElementsByClassName('sidebar-container')[0]
      this.$_sidebarElm && this.$_sidebarElm.addEventListener('transitionend', () => { this.memoryChart.resize() })
    }
  }
}
</script>
<style>
  #memoryChart{
    width: 100%;
    height: 500px;
    margin: 0 auto;
  }
</style>
