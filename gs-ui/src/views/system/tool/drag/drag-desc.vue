<template>
  <!-- 拖拽目标存储位置：group值与源group值一致   -->
  <draggable group="group1" class="components-item-copy" />
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
    }
  },
  methods: {
  }
}
</script>

<style scoped lang='scss'>
.components-item-copy {
  width: inherit;
  height: inherit;
  border: 1px solid blue;
}
</style>
