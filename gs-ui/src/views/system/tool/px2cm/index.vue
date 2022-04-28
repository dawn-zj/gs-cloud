<template>
  <div class="app-container">
    <el-tabs v-model="activeName" type="card">

      <el-tab-pane label="像素转厘米" name="px2cm">
        <el-form ref="px2cmForm" :model="px2cmFormData" label-width="100px">
          <el-form-item label="像素(px)" required>
            <el-input v-model="px2cmFormData.px" />
          </el-form-item>
          <el-form-item label="分辨率(dpi)" required>
            <el-input v-model="px2cmFormData.dpi" />
          </el-form-item>
          <el-form-item label="英寸(inch)">
            <span>{{ inch }}</span>
          </el-form-item>
          <el-form-item label="厘米(cm)">
            <span>{{ cm }}</span>
          </el-form-item>
        </el-form>

        <div class="mb20">
          <div class="mb20">
            <mark>Tips：像素不能直接转厘米，在不同分辨率下，厘米值不同。</mark><br>
            像素px、分辨率dpi、英寸inch、磅pt、厘米cm<br>
          </div>

          <div class="mb20">
            基础公式：<br>
            1. 实际英寸 = 像素 / 分辨率<br>
            2. 1英寸 = 2.54厘米<br>
            3. 1英寸 = 72磅，磅又名点<br>
          </div>

          <div class="mb20">
            汇总公式：<br>
            1. 像素->厘米：像素 / 分辨率 * 2.54<br>
            2. 厘米->像素：厘米 / 2.54 * 分辨率
            3. 厘米->磅：  厘米 / 2.54 * 72
          </div>

        </div>
      </el-tab-pane>

      <el-tab-pane label="厘米转像素" name="inch2cm">
        <el-form ref="px2cmForm" :model="cm2pxFormData" label-width="100px">
          <el-form-item label="厘米(cm)" required>
            <el-input v-model="cm2pxFormData.cm" />
          </el-form-item>
          <el-form-item label="分辨率(dpi)" required>
            <el-input v-model="cm2pxFormData.dpi" />
          </el-form-item>
          <el-form-item label="像素(px)">
            <span>{{ cm2pxFormData.cm / 2.54 * cm2pxFormData.dpi }}</span>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane label="厘米转英寸转磅" name="cm2pt">
        <el-form ref="px2cmForm" :model="cm2ptFormData" label-width="100px">
          <el-form-item label="厘米(cm)" required>
            <el-input v-model="cm2ptFormData.cm" />
          </el-form-item>
          <el-form-item label="英寸(inch)">
            <span>{{ cm2ptFormData.cm / 2.54 }}</span>
          </el-form-item>
          <el-form-item label="磅(pt)">
            <span>{{ cm2ptFormData.cm / 2.54 * 72 }}</span>
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
            <span>{{ dpiFormData.px / (dpiFormData.cm / 2.54) }}</span>
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
      cm2ptFormData: {
        cm: '21'
      },
      dpiFormData: {
        cm: '3.5',
        px: '100'
      }
    }
  },
  computed: {
    // 计算的变量，不用在data里定义
    inch() {
      return this.px2cmFormData.px / this.px2cmFormData.dpi
    },
    cm() {
      return this.inch * 2.54
    }
  }
}
</script>

<style scoped>

</style>
