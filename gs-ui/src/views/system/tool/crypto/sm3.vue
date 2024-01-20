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
          <el-form-item label="操作" required>
            <el-radio v-model="type" label="1">转为hex</el-radio>
            <el-radio v-model="type" label="2">转为hex并格式化</el-radio>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-button type="success" plain class="mb20" @click="handleSM3">SM3</el-button>
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
import { smEncrypt } from '@/utils/crypto/smEncrypt.min'
import * as HexUtil from '@/utils/hex/hex'

export default {
  name: 'CryptoSm3',
  data() {
    return {
      type: '2',
      formData: {
        content: '',
        result: ''
      }
    }
  },
  methods: {
    // 摘要
    handleSM3() {
      const hex = smEncrypt.sm3(this.formData.content)
      // eslint-disable-next-line eqeqeq
      if (this.type == 1) {
        this.formData.result = hex
      } else {
        this.formData.result = HexUtil.format(hex)
      }
    }
  }
}
</script>
