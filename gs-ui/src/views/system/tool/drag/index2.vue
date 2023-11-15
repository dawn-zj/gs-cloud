<template>
  <div>
    <draggable>
      <div class="components-body">拖拽组件</div>
    </draggable>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
export default {
  name: 'Index2',
  components: {
    draggable
  }
}
</script>

<style scoped lang='scss'>
$selectedColor: #f6f7ff;
$lighterBlue: #409EFF;

.components-body {
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
