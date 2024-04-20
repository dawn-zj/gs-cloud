<template>
  <div>
    <el-form ref="form" :model="formData">
      <el-row>
        <el-col>
          <el-form-item label="字符串" required>
            <el-input
              v-model="formData.content"
              type="textarea"
              :autosize="{ minRows: 5, maxRows: 10}"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-form-item label="密钥">
            <el-input
              v-model="formData.key"
              type="text"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-button type="success" plain class="mb20" @click="handle">暴力破解</el-button>
          <el-button type="success" plain class="mb20" @click="handleEncrypt">加密/解密</el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-form-item label="结果">
            <el-input
              v-model="formData.result"
              type="textarea"
              :autosize="{ minRows: 5, maxRows: 10}"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

  </div>
</template>
<script>
import * as simpleEncryptUtil from '@/utils/crypto/simpleEncrypt'

export default {
  name: 'CryptoCaesar',
  data() {
    return {
      type: '2',
      formData: {
        content: 'Hello World!',
        key: 3,
        result: ''
      }
    }
  },
  methods: {
    // 摘要
    handle() {
      let msg = ''
      const cipherText = this.formData.content
      for (let key = 1; key <= 25; key++) {
        const es = simpleEncryptUtil.caesar(cipherText, key)
        msg = msg + '密钥：' + key + '(或' + (key - 26) + ')，破解后原文：' + es + '\n'
      }
      this.formData.result = msg
    },
    handleEncrypt() {
      let key = parseInt(this.formData.key)
      if (this.formData.key < 0) {
        key = key + 26
      }
      console.log(key)
      this.formData.result = simpleEncryptUtil.caesar(this.formData.content, key)
    }
  }

}
</script>
