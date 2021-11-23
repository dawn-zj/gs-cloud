export function getChartsData(dataXY) {
  return {
    title: { text: '每天的请求数量' },
    tooltip: {},
    legend: {},
    toolbox: {
      show: false,
      feature: {
        mark: { show: true },
        dataView: { show: true, readOnly: false },
        magicType: { show: true, type: ['line', 'bar'] },
        restore: { show: true },
        saveAsImage: { show: true }
      }
    },
    dataZoom: {
      show: true,
      realtime: true,
      start: 50,
      end: 100
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: true,
        axisTick: { onGap: false },
        splitLine: { show: false },
        data: dataXY.X
      }
    ],
    yAxis: [
      {
        type: 'value',
        scale: true,
        axisLabel: {
          formatter: '{value} 次'
        }
      }
    ],
    series: [
      {
        name: '请求次数',
        type: 'line',
        data: dataXY.Y
      }
    ]
  }
}

// 饼图
export function getPieChartsData(data) {
  return {
    title: {
      text: '证书统计量',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'right'
    },
    series: [
      {
        type: 'pie', // 设置图表类型为饼图
        radius: '65%', // 饼图的半径，外半径为可视区尺寸
        data: [ // 数据数组，name 为数据项名称，value 为数据项值
          { value: data.issueCount, name: '有效证书' },
          { value: data.revokedCount, name: '作废证书' },
          { value: data.freezeCount, name: '冻结证书' }
        ]
      }
    ]
  }
}

export function getBarChartsData(data) {
  return {
    title: {
      text: '证书统计量',
      left: 'left'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      left: 'center'
    },
    toolbox: {
      show: false,
      feature: {
        mark: { show: true },
        dataView: { show: true, readOnly: false },
        magicType: { show: true, type: ['line', 'bar'] },
        restore: { show: true },
        saveAsImage: { show: true }
      }
    },
    dataset: {
      dimensions: ['day', '有效证书', '作废证书', '冻结证书'],
      source: data
    },
    xAxis: { type: 'category' },
    yAxis: { type: 'value' },
    series: [
      { type: 'bar' },
      { type: 'bar' },
      { type: 'bar' }
    ]
  }
}

// 环形饼图
export function getCPUChartsData(data) {
  return {
    // 标题
    title: {
      show: true,
      text: 'CPU',
      x: 'center',
      textStyle: {
        fontSize: '15',
        color: '#ccc'
      }
    },
    series: [{
      type: 'pie',
      // 饼图的半径，数组的第一项是内半径，第二项是外半径
      radius: ['40%', '48%'],
      // 鼠标悬浮放大
      hoverAnimation: false,
      // 图表data的name值
      label: {
        show: true,
        position: 'center'
      },
      // 数据值
      data: [
        // 圆环分两段，效果形如进度条
        // 圆环第一段：值为20
        { value: 20, name: '20%' },
        // 圆环第一段：值为80
        {
          value: 80,
          label: {
            normal: {
              show: false
            }
          }
        }
      ]
    }]
  }
}
