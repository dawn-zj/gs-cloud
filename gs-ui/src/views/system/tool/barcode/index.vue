<template>
  <div>
    <el-form ref="barcodeForm" :model="barcodeFormData" label-width="100px">
      <el-col :span="20">
        <el-form-item label="二维码内容">
          <el-input v-model="barcodeFormData.content" />
        </el-form-item>
      </el-col>
      <el-col :span="1" :offset="1">
        <el-button type="primary" plain @click="handleBarcode">制作</el-button>
      </el-col>
    </el-form>
    <div v-if="url" class="img-lg m20auto">
      <el-image :src="url" />
    </div>
  </div>
</template>
<script>
import { genBarcode } from '@/api/system/tool'

export default {
  name: 'IndexVue',
  data() {
    return {
      url: '',
      barcodeFormData: {
        content: ''
      }
    }
  },
  methods: {
    handleBarcode() {
      genBarcode(this.barcodeFormData).then(response => {
        this.url = 'data:image/png;base64,' + response.data.result
      })
    }
  }
}
</script>

<style scoped>

</style>
