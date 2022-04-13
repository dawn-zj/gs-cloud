<template>
  <div class="app-container">
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
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>文件数据</span>
          </div>
          <file-base64 />
        </el-card>
      </el-col>
      <el-col :span="11" :offset="1">
        <el-card>
          <div slot="header" class="clearfix">
            <span>制作二维码</span>
          </div>
          <barcode-index />
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import textBase64 from './base64/textBase64'
import fileBase64 from './base64/fileBase64'
import barcodeIndex from './barcode/index'

export default {
  name: 'Index',
  components: { textBase64, fileBase64, barcodeIndex }
}
</script>
