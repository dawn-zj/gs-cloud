<template>
  <div>
    <el-tabs v-model="activeName" :tab-position="tabPosition" class="mt20">
      <el-tab-pane label="编码" name="1">
        <el-form ref="formEncode" :model="formDataEncode" label-width="80px">
          <el-row>
            <el-col>
              <el-form-item label="原文" required>
                <el-input
                  v-model="formDataEncode.content"
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :offset="10">
              <el-button type="primary" plain class="mb20" @click="handleEncode">编码</el-button>
              <el-button type="primary" plain class="mb20" @click="handleHex">转十六进制</el-button>
              <el-button type="primary" plain class="mb20" @click="handleFormatHex">转十六进制(格式化)</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
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
            <el-col>
              <el-form-item label="编码数据" required>
                <el-input
                  v-model="formDataDecode.contentB64"
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="下载类型" required>
                <el-radio v-model="fileType" label=".txt">txt</el-radio>
                <el-radio v-model="fileType" label=".pdf">pdf</el-radio>
                <el-radio v-model="fileType" label=".ofd">ofd</el-radio>
                <el-radio v-model="fileType" label=".zip">zip</el-radio>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :offset="10">
              <el-button type="success" plain class="mb20" @click="handleDecode">解码</el-button>
              <el-button type="success" plain class="mb20" @click="handleDecodeAndDownload">解码后下载</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
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

      <el-tab-pane label="格式化" name="3">
        <el-form ref="formDecode" :model="formDataFormat" label-width="80px">
          <el-row>
            <el-col>
              <el-form-item label="编码数据" required>
                <el-input
                  v-model="formDataFormat.contentB64"
                  type="textarea"
                  :autosize="{ minRows: 5, maxRows: 10}"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :offset="10">
              <el-button type="success" plain class="mb20" @click="handleFormat">PEM格式化</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label="格式化后">
                <el-input
                  v-model="formDataFormat.content"
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
import { format, strToHex } from '@/utils/hex/hex'
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'Index',
  data() {
    return {
      // 用于各tab里的组件点击tab时渲染
      tabPosition: 'top',
      activeName: '1',
      fileType: '.txt',
      formDataEncode: {
        content: '',
        contentB64: ''
      },
      formDataDecode: {
        content: '',
        contentB64: ''
      },
      formDataFormat: {
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
    // 十六进制
    handleHex() {
      this.formDataEncode.contentB64 = strToHex(this.formDataEncode.content)
    },
    // 格式化十六进制
    handleFormatHex() {
      this.formDataEncode.contentB64 = format(strToHex(this.formDataEncode.content))
    },
    // 解码
    handleDecode() {
      this.formDataDecode.content = base64Decode(this.formDataDecode.contentB64)
    },
    // 解码并下载
    handleDecodeAndDownload() {
      this.formDataDecode.content = base64Decode(this.formDataDecode.contentB64)
      const timestamp = new Date().getTime()
      const localTime = parseTime(timestamp, '{y}{m}{d}{h}{i}{s}')
      downloadBase64(this.formDataDecode.contentB64, 'decode-' + localTime + this.fileType)
    },
    handleFormat() {
      var base64 = this.formDataFormat.contentB64
      var str = ''
      if (base64.indexOf('\\n') === -1) {
        for (var i = 0; i < base64.length; i++) {
          if ((i + 1) % 64 === 0) {
            str += base64.charAt(i) + '\n'
          } else {
            str += base64.charAt(i)
          }
        }
        this.formDataFormat.content = str
      } else {
        this.formDataFormat.content = this.formDataFormat.contentB64.replaceAll('\\n', '\n')
      }
    }
  }
}
</script>
<style>
textarea {
  font-family: 'Courier New', Courier, monospace !important;
}
</style>
