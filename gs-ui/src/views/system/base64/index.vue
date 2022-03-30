<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>文件数据</span>
          </div>
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
        </el-card>
      </el-col>
    </el-row>
    <el-row class="mt20">
      <el-col :span="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>文字数据</span>
          </div>
          <el-form ref="form" :model="formData" label-width="80px">
            <el-row>
              <el-col :span="10">
                <el-form-item label="原文">
                  <el-input
                    v-model="formData.content"
                    type="textarea"
                    :autosize="{ minRows: 5}"
                  />
                </el-form-item>

              </el-col>
              <el-col :span="2" :offset="1">
                <el-row><el-button class="mt10" @click="handleEncode">编码</el-button></el-row>
                <el-row><el-button class="mt10" @click="handleDecode">编码</el-button></el-row>
              </el-col>
              <el-col :span="10">
                <el-form-item label="编码数据">
                  <el-input
                    v-model="formData.contentB64"
                    type="textarea"
                    :autosize="{ minRows: 5}"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>

<script>
import { getBase64 } from '@/utils/file'
import { decode, encode } from '@/api/system/base64'

export default {
  name: 'Index',
  data() {
    return {
      loading: true,
      limitNum: 1,
      // 上传文件列表
      fileList: [],
      fileBase64: '',
      formData: {
        content: '',
        contentB64: ''
      }
    }
  },
  created() {
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
    },
    handleEncode() {
      encode(this.formData).then(response => {
        this.formData.contentB64 = response.data.result
      })
    },
    handleDecode() {
      decode(this.formData).then(response => {
        this.formData.content = response.data.result
      })
    }
  }
}
</script>

<style scoped>
  .form {
    width: 70%;
    margin: 20px auto;
  }

</style>
