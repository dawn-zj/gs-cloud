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
              <card-component
                v-if="col.buttonFunction != undefined"
                :title="col.cardTitle"
                :show-button="col.showButton"
                :button-text="col.buttonText"
                @function="callMethod(col.buttonFunction, 'formData测试')"
              >
                <component :is="col.componentName" />
              </card-component>
              <card-component
                v-else
                :title="col.cardTitle"
                :show-button="col.showButton"
                :button-text="col.buttonText"
              >
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
import pdfView from './pdfSign/pdfView'
import pdfViewField from './pdfSign/pdfViewField'
import pdfSign from './pdfSign/pdfSign'
import pdfVerify from './pdfVerify/index'
import pdfGetStamp from './pdfVerify/getStamp'
import linkProject from './linkProject/index'
import drag from './drag/index2'
import dynamicTable from './dynamic/index'
import CryptoSm3 from './crypto/sm3'
import CryptoCaesar from './crypto/caesar'
import { listTool } from '@/api/tool/tool'

export default {
  name: 'Index',
  components: {
    CardComponent, textBase64, fileBase64, barcodeIndex, px2cmIndex, watermark,
    pdfView, pdfViewField, pdfSign, pdfVerify, pdfGetStamp, linkProject, drag,
    dynamicTable, CryptoSm3, CryptoCaesar
  },
  data() {
    return {
      tabPosition: 'top',
      // 用于各tab里的组件点击tab时渲染
      activeName: '0',
      currentView: '',
      tabPaneArr: []
    }
  },
  created() {
    // this.tabPaneArr = window.gs.tabPaneArr
    this.list()
  },
  methods: {
    list() {
      listTool().then(res => {
        this.tabPaneArr = res.data.tabPaneArr
      })
    }
  }
}
</script>

<style>
</style>
