<template>
  <div>
    <el-form label-width="100px">
      <el-form-item label="选择文件">
        <el-upload
          ref="upload"
          class="pdf-upload"
          name="fileList"
          drag
          action="#"
          :auto-upload="false"
          :file-list="fileList"
          :on-remove="handleFileRemove"
          :on-change="handleFileChange"
        >
          <i class="el-icon-upload" />
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        </el-upload>
        <el-button type="primary" plain class="mt10 mb10" @click="parseFile">解析PDF</el-button>
        <el-button type="primary" plain class="mt10 mb10" @click="save2File">保存</el-button>
      </el-form-item>
      <el-form-item label="签章数据">
        <json-viewer
          theme="jv-light"
          :value="stampMap"
          :show-array-index="false"
          boxed
          sort
          copyable
        >
          <template slot="copy">
            <i class="el-icon-document-copy" title="复制"></i>
          </template>
        </json-viewer>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getPdfStamp } from '@/api/system/tool/pdfStamp'

export default {
  name: 'Index',
  data() {
    return {
      limitNum: 1,
      // 上传文件列表
      fileList: [],
      stampMap: {}
    }
  },
  methods: {
    // 上传文件变化
    handleFileChange(file, fileList) {
      this.stampMap = ''

      if (this.limitNum === 1) {
        this.fileList = [fileList[fileList.length - 1]]
      }
    },
    // 删除文件
    handleFileRemove(file, fileList) {
      this.$set(this, 'stampMap', {})
    },
    parseFile() {
      if (this.fileList.length === 0) {
        this.msgError('请上传PDF签章文件')
        return
      }
      const formData = new FormData()
      const file = this.fileList[0]
      formData.append('file', file.raw)
      getPdfStamp(formData).then(res => {
        this.msgSuccess('解析成功')
        this.stampMap = res.data.stampMap
      })
    },
    save2File() {
      for (var key in this.stampMap) {
        this.downloadBase64(this.stampMap[key], key + '-stamp.asn1')
      }
    }
  }
}
</script>
