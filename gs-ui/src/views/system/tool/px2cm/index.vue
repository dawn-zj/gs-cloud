<template>
  <div class="app-container">
    <div class="mb20">
      <mark>Tips：像素不能直接转厘米，在不同分辨率下，厘米值不同。</mark><br>
      公式：<br>
      1. 实际英寸 = 像素 / 分辨率<br>
      2. 1英寸 = 2.54厘米<br>
    </div>
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="像素转厘米" name="px2cm">
        <el-form ref="px2cmForm" :model="px2cmFormData" label-width="100px">
          <el-form-item label="像素(px)" required>
            <el-input v-model="px2cmFormData.px" />
          </el-form-item>
          <el-form-item label="分辨率(dpi)" required>
            <el-input v-model="px2cmFormData.dpi" />
          </el-form-item>
          <el-form-item label="英寸(pt)">
            <span>{{ pt }}</span>
          </el-form-item>
          <el-form-item label="厘米(cm)">
            <span>{{ cm }}</span>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="厘米转像素" name="pt2cm">
        <el-form ref="px2cmForm" :model="cm2pxFormData" label-width="100px">
          <el-form-item label="厘米(cm)" required>
            <el-input v-model="cm2pxFormData.cm" />
          </el-form-item>
          <el-form-item label="分辨率(dpi)" required>
            <el-input v-model="cm2pxFormData.dpi" />
          </el-form-item>
          <el-form-item label="像素(px)">
            <span>{{ px }}</span>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="已知厘米像素，求分辨率" name="dpi">
        <el-form ref="px2cmForm" :model="dpiFormData" label-width="100px">
          <el-form-item label="厘米(cm)" required>
            <el-input v-model="dpiFormData.cm" />
          </el-form-item>
          <el-form-item label="像素(px)" required>
            <el-input v-model="dpiFormData.px" />
          </el-form-item>
          <el-form-item label="分辨率">
            <span>{{ dpi }}</span>
          </el-form-item>
        </el-form>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      activeName: 'px2cm',
      px2cmFormData: {
        px: '160',
        dpi: '72'
      },
      cm2pxFormData: {
        cm: '21',
        dpi: '72'
      },
      dpiFormData: {
        cm: '3.5',
        px: '100'
      }
    }
  },
  computed: {
    // 计算的变量，不用在data里定义
    pt() {
      return this.px2cmFormData.px / this.px2cmFormData.dpi
    },
    cm() {
      return this.pt * 2.54
    },
    px() {
      const pt = this.cm2pxFormData.cm / 2.54
      return pt * this.px2cmFormData.dpi
    },
    dpi() {
      const pt = this.dpiFormData.cm / 2.54
      return this.dpiFormData.px / pt
    }
  }
}
</script>

<style scoped>

</style>
