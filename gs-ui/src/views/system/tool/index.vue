<template>
  <div class="app-container">
    <el-radio-group v-model="tabPosition" class="mb30">
      <el-radio-button label="top">top</el-radio-button>
      <el-radio-button label="right">right</el-radio-button>
      <el-radio-button label="bottom">bottom</el-radio-button>
      <el-radio-button label="left">left</el-radio-button>
    </el-radio-group>

    <el-tabs :tab-position="tabPosition" class="mt20">
      <el-tab-pane label="Base64">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>文字数据</span>
              </div>
              <text-base64 />
            </el-card>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>文件数据</span>
              </div>
              <file-base64 />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="制作二维码">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>制作二维码</span>
              </div>
              <barcode-index />
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="像素尺寸转换">
        <el-row>
          <el-col :span="24">
            <el-card>
              <div slot="header" class="clearfix">
                <span>像素/分辨率/英寸/厘米 转换</span>
              </div>
              <px2cm-index />
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
import px2cmIndex from '@/views/system/tool/px2cm'

export default {
  name: 'Index',
  components: { textBase64, fileBase64, barcodeIndex, px2cmIndex },
  data() {
    return {
      tabPosition: 'top'
    }
  }
}
</script>
