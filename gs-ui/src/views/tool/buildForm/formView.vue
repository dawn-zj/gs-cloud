<template>
  <div class="app-container">
    <form-create
      v-model="data.formData"
      :value.sync="data.formData"
      :rule="data.rule"
      :option="data.options"
      @submit="onSubmit"
      @gs-change="onChange"
      @gs-blur="onBlur"
    />
  </div>
</template>

<script>
export default {
  props: {
    data: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
    }
  },
  created() {
  },
  methods: {
    onSubmit(formData, fapi) {
      // eslint-disable-next-line no-eval
      const func = eval(fapi.options.submitEvent)
      func(formData)
    },
    onChange(inject, val) {
      // 具体脚本通过inject指定，格式为 "inject": ["(data, formData)=>{// 自定义}"]
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0])
      func(inject.self.value, inject.$f.form)
    },
    onBlur(inject) {
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0])
      func(inject.self.value, inject.$f.form)
    }
  }
}
</script>

<style scoped>

</style>
