<template>
  <div>
    <el-form :model="formData">
      <el-form-item
        v-for="(item, index) in fromCreateArr"
        :key="index"
        :hidden="item.hidden"
      >
        <span v-if="item.type != 'button'">{{ item.title }}</span>
        <el-input
          v-if="item.type == 'input' && item.props.type == 'text'"
          v-model="formData[item.field]"
          :type="item.props.type"
          :disabled="item.display"
          :show-word-limit="item.props.showWordLimit"
        />
        <el-input
          v-if="item.type == 'input' && item.props.type == 'textarea'"
          v-model="formData[item.field]"
          :type="item.props.type"
          :disabled="item.display"
          :show-word-limit="item.props.showWordLimit || false"
          :max-length="item.props.maxlength"
        />
        <el-button
          v-if="item.type == 'button'"
          :key="index"
          :type="(item.props && item.props.type) || 'primary'"
          :disabled="item.display"
          :hidden="item.hidden"
          @click="runBtnEvent(item)"
        >{{ item.title }}
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  props: {
    data: {
      type: Array,
      required: false
    }
  },
  data() {
    return {
      formData: {},
      fromCreateArr: [
        {
          type: 'input',
          field: 'f1',
          title: '输入框1',
          props: {
            type: 'text'
          },
          info: '',
          value: '111',
          hidden: false,
          display: false
        },
        {
          type: 'input',
          field: 'f2',
          title: '输入框2-禁用',
          props: {
            type: 'text'
          },
          info: '',
          hidden: false,
          display: true
        },
        {
          type: 'input',
          field: 'f3',
          title: '输入框3-隐藏',
          props: {
            type: 'text'
          },
          info: '',
          hidden: true,
          display: true
        },
        {
          type: 'input',
          field: 'f4',
          title: '多行输入框',
          props: {
            type: 'textarea',
            showWordLimit: true,
            maxlength: 20,
            minlength: 10
          },
          info: '',
          hidden: false,
          display: false
        },
        {
          type: 'button',
          title: '按钮',
          btnScript: "this.msgInfo('按钮1');console.log(this.formData)",
          hidden: false,
          display: false
        }
      ]
    }
  },
  created() {
    this.fromCreateArr = this.data
  },
  methods: {
    runBtnEvent(btn) {
      eval(btn.btnScript)
    }
  }
}
</script>

<style scoped>

</style>
