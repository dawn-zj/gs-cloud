<template>
  <div>
    <el-tabs v-model="activeName" :tab-position="tabPosition" class="mt20">
      <el-tab-pane label="编码" name="1">
        <el-form ref="formEncode" :model="formDataEncode" label-width="80px">
          <el-row>
            <el-col :xs="24" :sm="24" :md="10" :lg="10">
              <el-form-item label="原文" required>
                <el-input
                  v-model="formDataEncode.content"
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                />
              </el-form-item>

            </el-col>
            <el-col :xs="24" :sm="24" :md="2" :lg="2" :offset="1">
              <el-row><el-button type="primary" plain class="mt10" @click="handleEncode">编码</el-button></el-row>
            </el-col>
            <el-col :xs="24" :sm="24" :md="10" :lg="10">
              <el-form-item label="编码数据">
                <el-input
                  v-model="formDataEncode.contentB64"
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="解码" name="2">
        <el-form ref="formDecode" :model="formDataDecode" label-width="80px">
          <el-row>
            <el-col :xs="24" :sm="24" :md="10" :lg="10">
              <el-form-item label="编码数据" required>
                <el-input
                  v-model="formDataDecode.contentB64"
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="24" :md="2" :lg="2" :offset="1">
              <el-row><el-button type="success" plain class="mt10" @click="handleDecode">解码</el-button></el-row>
              <el-row><el-button type="success" plain class="mt10" @click="handleDecodeAndDownload">解码后下载</el-button></el-row>
            </el-col>
            <el-col :xs="24" :sm="24" :md="10" :lg="10">
              <el-form-item label="原文">
                <el-input
                  v-model="formDataDecode.content"
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>

import { downloadBase64 } from '@/utils/file'
import { base64Decode, base64Encode } from '@/utils/base64/base64'

export default {
  name: 'Index',
  data() {
    return {
      tabPosition: 'top',
      // 用于各tab里的组件点击tab时渲染
      activeName: '1',
      formDataEncode: {
        content: '',
        contentB64: ''
      },
      formDataDecode: {
        content: '',
        contentB64: ''
      }
    }
  },
  methods: {
    // 编码
    handleEncode() {
      this.formDataEncode.contentB64 = base64Encode(this.formDataEncode.content)
    },
    // 解码
    handleDecode() {
      this.formDataDecode.content = base64Decode(this.formDataDecode.contentB64)
    },
    // 解码并下载
    handleDecodeAndDownload() {
      this.formDataDecode.content = base64Decode(this.formDataDecode.contentB64)
      downloadBase64(this.formDataDecode.contentB64, 'decode.txt')
    }
  }
}
</script>
