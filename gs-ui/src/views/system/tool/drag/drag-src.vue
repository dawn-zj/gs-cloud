<template>
  <div>
    <draggable
      :group="{ name: 'group1', pull: 'clone', put: false }"
      :clone="cloneComponent"
    >
      <div
        v-for="(element, index) in inputComponents"
        :key="index"
        class="components-item"
      >
        <div class="components-body">
          <svg-icon :icon-class="element.tagIcon" />
          {{ element.label }}
        </div>
      </div>
    </draggable>
  </div>
</template>

<script>
import draggable from 'vuedraggable'

export default {
  name: 'Index2',
  components: {
    draggable
  },
  data() {
    return {
      inputComponents: [
        {
          label: '单行文本',
          tag: 'el-input',
          tagIcon: 'input',
          placeholder: '请输入',
          defaultValue: undefined,
          span: 24,
          labelWidth: null,
          style: { width: '100%' },
          clearable: true,
          prepend: '',
          append: '',
          'prefix-icon': '',
          'suffix-icon': '',
          maxlength: null,
          'show-word-limit': false,
          readonly: false,
          disabled: false,
          required: true,
          regList: [],
          changeTag: true,
          document: 'https://element.eleme.cn/#/zh-CN/component/input'
        }
      ]
    }
  },
  methods: {
    cloneComponent(origin) {
      console.log(origin)
      const clone = JSON.parse(JSON.stringify(origin))
      return clone
    }
  }
}
</script>

<style scoped lang='scss'>
$selectedColor: #f6f7ff;
$lighterBlue: #409EFF;

.components-item {
  width: 120px;
  padding: 8px 10px;
  background: $selectedColor;
  font-size: 12px;
  cursor: move;
  border: 1px dashed $selectedColor;
  border-radius: 3px;
  .svg-icon{
    color: #777;
    font-size: 15px;
  }
  &:hover {
    border: 1px dashed #787be8;
    color: #787be8;
    .svg-icon {
      color: #787be8;
    }
  }
}
</style>
