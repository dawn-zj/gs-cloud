<template>
  <div class="app-container">
    <el-radio-group v-model="tabPosition" class="mb30">
      <el-radio-button label="top">top</el-radio-button>
      <el-radio-button label="right">right</el-radio-button>
      <el-radio-button label="bottom">bottom</el-radio-button>
      <el-radio-button label="left">left</el-radio-button>
    </el-radio-group>

    <el-tabs v-model="activeName" :tab-position="tabPosition" class="mt20">
      <el-tab-pane v-for="(tab, index) in tabPaneArr" :key="index" :label="tab.tabLabel" :name="index.toString()">
        <div v-if="activeName == index">
          <el-row v-for="(row, index) in tab.row" :key="index" class="mb20" :gutter="20">
            <el-col v-for="(col, index) in row.col" :key="index" :span="col.span" :offset="col.offset">
              <card-component :title="col.cardTitle" :show-button="col.showButton" :button-text="col.buttonText">
                <component :is="col.componentName" />
              </card-component>
            </el-col>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<script>
import CardComponent from '@/views/components/CardComponent'
import textBase64 from './base64/textBase64'
import fileBase64 from './base64/fileBase64'
import barcodeIndex from './barcode/index'
import px2cmIndex from './px2cm/index'
import watermark from './watermark/index'
import pdfSign from './pdfSign/index'
import pdfVerify from './pdfVerify/index'
import pdfGetStamp from './pdfVerify/getStamp'
import linkProject from './linkProject/index'
import drag from './drag/index2'
import dynamicTable from './dynamic/index'

export default {
  name: 'Index',
  components: {
    CardComponent, textBase64, fileBase64, barcodeIndex, px2cmIndex, watermark,
    pdfSign, pdfVerify, pdfGetStamp, linkProject, drag,
    dynamicTable
  },
  data() {
    return {
      tabPosition: 'top',
      // 用于各tab里的组件点击tab时渲染
      activeName: '0',
      currentView: '',
      tabPaneArr: [
        {
          tabLabel: 'Base64',
          row: [
            {
              col: [
                { span: 24, cardTitle: '文字编码', componentName: 'textBase64' }
              ]
            },
            {
              col: [
                { span: 24, cardTitle: '文件编码', componentName: 'fileBase64' }
              ]
            }
          ]
        },
        {
          tabLabel: '制作二维码',
          row: [
            {
              col: [
                { span: 24, cardTitle: '制作二维码', componentName: 'barcodeIndex' }
              ]
            }
          ]
        },
        {
          tabLabel: '像素尺寸转换',
          row: [
            {
              col: [
                { span: 24, cardTitle: '像素尺寸转换', componentName: 'px2cmIndex' }
              ]
            }
          ]
        },
        {
          tabLabel: '水印(外链项目)',
          row: [
            {
              col: [
                { span: 12, cardTitle: '外链项目', componentName: 'linkProject', showButton: true, buttonText: '添加' },
                { span: 12, cardTitle: '水印', componentName: 'watermark' }
              ]
            }
          ]
        },
        {
          tabLabel: 'PDF签名',
          row: [
            {
              col: [
                { span: 24, cardTitle: 'PDF签名', componentName: 'pdfSign' }
              ]
            }
          ]
        },
        {
          tabLabel: 'PDF验签',
          row: [
            {
              col: [
                { span: 24, cardTitle: 'PDF验签', componentName: 'pdfVerify' }
              ]
            }
          ]
        },
        {
          tabLabel: '获取PDF签章数据',
          row: [
            {
              col: [
                { span: 24, cardTitle: '获取PDF签章数据', componentName: 'pdfGetStamp' }
              ]
            }
          ]
        },
        {
          tabLabel: '拖拽组件',
          row: [
            {
              col: [
                { span: 24, cardTitle: '拖拽组件', componentName: 'drag' }
              ]
            }
          ]
        },
        {
          tabLabel: '动态表格',
          row: [
            {
              col: [
                { span: 24, cardTitle: '动态表格', componentName: 'dynamicTable' }
              ]
            }
          ]
        }
      ]
    }
  },
  created() {
  }
}
</script>

<style>
</style>
