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
      @gs-click="onClick"
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
      func(inject.$f.form, inject.self.value, inject)
    },
    onBlur(inject) {
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0])
      func(inject.$f.form, inject.self.value, inject)
    },
    onClick(inject) {
      // eslint-disable-next-line no-eval
      const func = eval(inject.inject[0])
      func(inject.$f.form, inject.self.value, inject)
    }
  }
}
</script>

<style scoped>

</style>
