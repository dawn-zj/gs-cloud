<template>
  <div class="app-container">
    <form-create
      v-model="egData.formData"
      :value.sync="egData.formData"
      :rule="egData.rule"
      :option="egData.options"
      @submit="onSubmit"
      @gs-change="onChange"
      @gs-blur="onBlur"
    />
  </div>
</template>

<script>
import requestVue from '@/utils/requestLocal'
export default {
  data() {
    return {
      egData: {
        // 表单默认值，也作为双向绑定的表单数据
        formData: {},
        rule: [],
        options: {}
      }
    }
  },
  created() {
    this.getConfig()
  },
  methods: {
    getConfig() {
      requestVue.get('/json/form-create/eg.json').then(res => {
        this.egData = res
      })
    },
    onSubmit(formData, fapi) {
      console.log(fapi)
      // eslint-disable-next-line no-eval
      const func = eval(fapi.options.submitEvent)
      func(formData)
    },
    onChange(inject, val) {
      console.log(inject)
      // 具体脚本通过inject指定，格式为 "inject": ["(data, formData)=>{// 自定义}"]
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0])
      func(inject.$f.form, inject.self.value, inject)
    },
    onBlur(inject) {
      console.log(inject)
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0])
      func(inject.$f.form, inject.self.value, inject)
    }
  }
}
</script>

<style scoped>

</style>
