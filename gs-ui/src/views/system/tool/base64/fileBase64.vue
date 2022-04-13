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
      </el-form-item>
      <el-form-item label="文件Base64">
        <el-input
          v-model="fileBase64"
          readonly
          type="textarea"
          :autosize="{ minRows: 5}"
        />
      </el-form-item>
    </el-form>
  </div>
</template>
<script>

import { getBase64 } from '@/utils/file'

export default {
  name: 'Index',
  data() {
    return {
      limitNum: 1,
      // 上传文件列表
      fileList: [],
      fileBase64: ''
    }
  },
  methods: {
    // 上传文件变化
    handleFileChange(file, fileList) {
      if (this.limitNum === 1) {
        this.fileList = [fileList[fileList.length - 1]]
      }
      // 获取base64
      getBase64(file.raw).then(res => {
        this.$set(this, 'fileBase64', res)
      })
    },
    // 删除文件
    handleFileRemove(file, fileList) {
      this.$set(this, 'fileBase64', '')
    }
  }
}
</script>
