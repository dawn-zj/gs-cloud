<template>
  <div class="app-container">
    <el-radio-group v-model="tabPosition" class="mb30">
      <el-radio-button label="top">top</el-radio-button>
      <el-radio-button label="right">right</el-radio-button>
      <el-radio-button label="bottom">bottom</el-radio-button>
      <el-radio-button label="left">left</el-radio-button>
    </el-radio-group>

    <el-tabs v-model="activeName" :tab-position="tabPosition" class="mt20">
      <el-tab-pane label="Base64" name="1">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>文字数据</span>
              </div>
              <text-base64 v-if="activeName == '1'" />
            </el-card>

          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>文件数据</span>
              </div>
              <file-base64 v-if="activeName == '1'" />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="制作二维码" name="2">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>制作二维码</span>
              </div>
              <barcode-index v-if="activeName == '2'" />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="像素尺寸转换" name="3">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>像素/分辨率/英寸/厘米 转换</span>
              </div>
              <px2cm-index v-if="activeName == '3'" />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="水印" name="4">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>水印</span>
              </div>
              <watermark v-if="activeName == '4'" />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="PDF签名" name="5">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>PDF签名</span>
              </div>
              <pdf-sign v-if="activeName == '5'" />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="PDF验签" name="6">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>PDF验签</span>
              </div>
              <pdf-verify v-if="activeName == '6'" />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>

      <el-tab-pane label="外链项目" name="7">
        <div v-for="(item,index) in gsConfig" :key="index" class="mb10">
          {{ index + 1 }}.{{ item.describe }}：<el-link :href="item.url" target="_blank">{{ item.url }}</el-link>
        </div>

      </el-tab-pane>

      <el-tab-pane label="获取PDF签章数据" name="8">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>获取PDF签章数据</span>
              </div>
              <pdf-get-stamp v-if="activeName == '8'" />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
  </div>

</template>

<script>
import textBase64 from './base64/textBase64'
import fileBase64 from './base64/fileBase64'
import barcodeIndex from './barcode/index'
import px2cmIndex from './px2cm/index'
import watermark from './watermark/index'
import pdfSign from './pdfSign/index'
import pdfVerify from './pdfVerify/index'
import pdfGetStamp from './pdfVerify/getStamp'

export default {
  name: 'Index',
  components: { textBase64, fileBase64, barcodeIndex, px2cmIndex, watermark, pdfSign, pdfVerify, pdfGetStamp },
  data() {
    return {
      tabPosition: 'top',
      // 用于各tab里的组件点击tab时渲染
      activeName: '1',
      gsConfig: []
    }
  },
  created() {
    this.gsConfig = window.gs.urlConfig
  }
}
</script>
