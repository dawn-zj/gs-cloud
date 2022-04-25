<template>
  <div>
    <el-form ref="form" :model="formData" label-width="80px">
      <el-row>
        <el-col :xs="24" :sm="24" :md="10" :lg="10">
          <el-form-item label="原文">
            <el-input
              v-model="formData.content"
              type="textarea"
              :autosize="{ minRows: 5}"
            />
          </el-form-item>

        </el-col>
        <el-col :xs="24" :sm="24" :md="2" :lg="2" :offset="1">
          <el-row><el-button type="primary" plain class="mt10" @click="handleEncode">编码</el-button></el-row>
          <el-row><el-button type="success" plain class="mt10" @click="handleDecode">解码</el-button></el-row>
        </el-col>
        <el-col :xs="24" :sm="24" :md="10" :lg="10">
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
  </div>
</template>
<script>

import { decode, encode } from '@/api/system/tool'

export default {
  name: 'Index',
  data() {
    return {
      formData: {
        content: '',
        contentB64: ''
      }
    }
  },
  methods: {
    // 编码
    handleEncode() {
      encode(this.formData).then(response => {
        this.formData.contentB64 = response.data.result
      })
    },
    // 解码
    handleDecode() {
      decode(this.formData).then(response => {
        this.formData.content = response.data.result
      })
    }
  }
}
</script>
