<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '240px'
    },
    height: {
      type: String,
      default: '240px'
    },
    pieData: {
      type: Object,
      required: true
    },
    color: {
      type: Array
      // default() {
      //   return ['rgba(0,132,255,0.5)', 'rgb(0,132,255)']
      // }
    },
    showTitle: {
      type: Boolean,
      default: true
    },
    titleText: {
      type: String,
      default: '硬盘'
    },
    titleX: {
      type: String,
      default: 'center'
    },
    titleFontSize: {
      type: String,
      default: '15'
    },
    titleFontColor: {
      type: String,
      default: '#ccc'
    },
    radius: {
      default() {
        return ['55%', '60%']
      }
    },
    hoverAnimation: {
      type: Boolean,
      default: false
    },
    showLabel: {
      type: Boolean,
      default: true
    },
    labelPosition: {
      type: String,
      default: 'center'
    }
  },
  data() {
    return {
      chart: null,
      series: [],
      showTooltip: false,
      chartData: []
    }
  },
  mounted() {
    this.$nextTick(() => {
      if (this.labelPosition === 'center') {
        this.showTooltip = false
        this.chartData = [
          // 圆环分两段，效果形如进度条
          // 圆环第一段：值为使用率
          {
            value: this.pieData.usage,
            name: this.pieData.usage + '%',
            unit: this.pieData.unit
          },
          // 圆环第一段：值为剩余率
          {
            value: 100 - this.pieData.usage,
            name: this.pieData.usage + '%',
            label: {
              normal: {
                show: false
              }
            },
            unit: this.pieData.unit
          }
        ]
      } else {
        this.showTooltip = true
        this.chartData = [
          // 圆环第一段：值为使用大小
          {
            value: this.pieData.used,
            name: '已用' + this.titleText,
            unit: this.pieData.unit
          },
          // 圆环第一段：值为剩余大小
          {
            value: this.pieData.free,
            name: '剩余' + this.titleText,
            unit: this.pieData.unit
          }
        ]
      }
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        // 标题
        title: {
          show: this.showTitle,
          text: this.titleText,
          x: this.titleX,
          textStyle: {
            fontSize: this.titleFontSize,
            color: this.titleFontColor
          }
        },
        color: ['#46a4f7', '#36cbcb', '#11D074', '#fad337'], // ['rgba(0,132,255,0.5)', 'rgb(0,132,255)']
        series: [{
          type: 'pie',
          name: this.titleText,
          // 饼图的半径，数组的第一项是内半径，第二项是外半径
          radius: this.radius,
          // 鼠标悬浮放大
          hoverAnimation: this.hoverAnimation,
          // 图表data的name值
          label: {
            show: this.showLabel,
            position: this.labelPosition
          },
          // 数据值
          data: this.chartData
        }],
        tooltip: {
          show: this.showTooltip,
          formatter: function(params) {
            var result = '<div>' + params.seriesName + '<div/>' + params.marker + params.data.name + ' : ' + params.data.value + params.data.unit + '  (' + params.percent + '%)'
            return result
          }
        }
      })
    }
  }
}
</script>
